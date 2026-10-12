"""Remap Yarn-named mixin strings (method=, @At target=, @Invoker/@Accessor) to Mojmap.

Usage: python remap_mixins.py <mappings_dir> <src_root> [--write]
Chain: yarn named -> official (yarn.tiny) -> mojmap (client.txt).
Prints every change; anything unresolved or ambiguous is reported, never guessed.
"""
import os, re, sys
from collections import defaultdict

mdir, src = sys.argv[1], sys.argv[2]
WRITE = "--write" in sys.argv

# ---------- yarn.tiny (v2): official intermediary named ----------
y_cls_n2o = {}                        # named class -> official
y_meth = defaultdict(list)            # official owner -> [(off_name, off_desc, named_name)]
y_field = defaultdict(list)           # official owner -> [(off_name, off_desc, named_name)]
cur = None
with open(os.path.join(mdir, "yarn.tiny")) as f:
    next(f)
    for line in f:
        p = line.rstrip("\n").split("\t")
        if p[0] == "c":
            cur = p[1]; y_cls_n2o[p[3]] = p[1]
        elif len(p) > 1 and p[0] == "" and p[1] in ("m", "f"):
            # \tm\tdesc\toff\tinter\tnamed  (desc in official names)
            desc, off, named = p[2], p[3], p[5]
            (y_meth if p[1] == "m" else y_field)[cur].append((off, desc, named))
y_cls_o2n = {v: k for k, v in y_cls_n2o.items()}

# ---------- Mojang client.txt (proguard): mojmap -> official ----------
PRIM = {"void": "V", "boolean": "Z", "byte": "B", "char": "C", "short": "S", "int": "I",
        "long": "J", "float": "F", "double": "D"}
m_cls_m2o = {}
raw_members = []                      # (moj_owner, kind, moj_name, ret_type, params or None)
cur = None
with open(os.path.join(mdir, "client.txt")) as f:
    for line in f:
        if line.startswith("#"):
            continue
        if not line.startswith(" "):
            moj, off = line.strip().rstrip(":").split(" -> ")
            cur = moj.replace(".", "/"); m_cls_m2o[cur] = off.replace(".", "/")
            continue
        left, obf = line.strip().split(" -> ")
        left = re.sub(r"^\d+:\d+:", "", left)
        typ, rest = left.split(" ", 1)
        if "(" in rest:
            name, params = rest.split("(", 1)
            params = params.rstrip(")")
            raw_members.append((cur, "m", name, typ, params, obf))
        else:
            raw_members.append((cur, "f", rest, typ, None, obf))
m_cls_o2m = {v: k for k, v in m_cls_m2o.items()}

def jtype_to_desc(t, cmap):
    dims = t.count("[]"); t = t.replace("[]", "")
    if t in PRIM:
        d = PRIM[t]
    else:
        t = t.replace(".", "/"); d = "L" + cmap.get(t, t) + ";"
    return "[" * dims + d

m_meth = defaultdict(dict)            # official owner -> {(obf, official desc): moj name}
m_field = defaultdict(dict)           # official owner -> {obf: moj name}
for owner, kind, name, typ, params, obf in raw_members:
    oo = m_cls_m2o[owner]
    if kind == "m":
        ps = [p for p in params.split(",") if p] if params else []
        desc = "(" + "".join(jtype_to_desc(p, m_cls_m2o) for p in ps) + ")" + jtype_to_desc(typ, m_cls_m2o)
        m_meth[oo][(obf, desc)] = name
    else:
        m_field[oo][obf] = name

# ---------- helpers ----------
DESC_CLS = re.compile(r"L([^;]+);")
def desc_map(desc, cmap):
    return DESC_CLS.sub(lambda m: "L" + cmap.get(m.group(1), m.group(1)) + ";", desc)
def y2o_desc(d): return desc_map(d, y_cls_n2o)
def o2m_desc(d): return desc_map(d, m_cls_o2m)

def moj_method(off_owner, off_name, off_desc):
    return m_meth.get(off_owner, {}).get((off_name, off_desc))

y_by_name = defaultdict(list)         # yarn named method name -> [(declaring off owner, off name, off desc)]
for ow, lst in y_meth.items():
    for off, d, nm in lst:
        y_by_name[nm].append((ow, off, d))

def resolve_method(off_owner, yname, ydesc=None, declared_only=True):
    """Return (moj_name, moj_desc_or_None, note).

    Yarn lists a method only on its declaring class; Mojang lists it on every class that
    declares or overrides it. A mixin can only target methods declared in the target class,
    so: take every yarn declaration of `yname` (desc-filtered), and keep the ones whose
    official (name, desc) is declared in `off_owner` per Mojang AND whose Mojang name there
    equals the Mojang name at the yarn declaring class (overrides keep their name).
    """
    odesc = y2o_desc(ydesc) if ydesc else None
    decls = [(ow, o, d) for ow, o, d in y_by_name.get(yname, []) if odesc is None or d == odesc]
    hits = set()
    if off_owner:
        own = m_meth.get(off_owner, {})
        for ow, o, d in decls:
            here = own.get((o, d)); there = moj_method(ow, o, d)
            if here and here == there:
                hits.add((here, d))
    note = ""
    if not hits and not declared_only:
        # invocation targets may reference an inherited method; desc-qualified global match
        hits = {(moj_method(ow, o, d), d) for ow, o, d in decls if moj_method(ow, o, d)}
        note = "inherited"
    names = {n for n, _ in hits}
    if len(names) == 1 and (odesc or len({d for _, d in hits}) >= 1):
        n = names.pop()
        if odesc is None and len({d for _, d in hits}) > 1:
            note = (note + " overloads").strip()
        return n, (o2m_desc(odesc) if odesc else None), note
    return None, None, ("unresolved" if not names else "ambiguous " + str(sorted(names)))

def resolve_field(off_owner, yname):
    for off, d, nm in y_field.get(off_owner, []):
        if nm == yname:
            return m_field.get(off_owner, {}).get(off)
    return None

def moj_to_off(fqcn_slash):
    return m_cls_m2o.get(fqcn_slash)

# ---------- per-file processing ----------
IMPORT = re.compile(r"^import\s+([\w.]+);", re.M)
MIXIN = re.compile(r"@Mixin\((.*?)\)\s*(?:public|abstract|class|interface|@)", re.S)
CLS_REF = re.compile(r"([\w.]+)\.class")
METHOD_ATTR = re.compile(r'(method\s*=\s*)(\{[^}]*\}|"[^"]*")')
TARGET_ATTR = re.compile(r'(target\s*=\s*)"([^"]+)"')
INVOKER = re.compile(r'@(Invoker|Accessor)\("([^"]+)"\)')
STR = re.compile(r'"([^"]*)"')
SKIP_NAMES = {"<init>", "<clinit>"}

def resolve_class_ref(ref, imports, pkg):
    parts = ref.split(".")
    if parts[0] in imports:
        base = imports[parts[0]]
        fq = base + ("$" + "$".join(parts[1:]) if len(parts) > 1 else "")
    else:
        fq = ref
    return fq.replace(".", "/")

report = []
changed_files = 0
for root, _, files in os.walk(src):
    for fn in files:
        if not fn.endswith(".java"):
            continue
        path = os.path.join(root, fn)
        text = open(path, encoding="utf-8").read()
        mm = MIXIN.search(text)
        if not mm or "remap = false" in mm.group(1):
            continue
        imports = {i.split(".")[-1]: i for i in IMPORT.findall(text)}
        owners = []
        for ref in CLS_REF.findall(mm.group(1)):
            off = moj_to_off(resolve_class_ref(ref, imports, None))
            owners.append(off)
        rel = os.path.relpath(path, src)
        new = text

        def fix_method_string(s):
            if s in SKIP_NAMES or s.startswith("<init>("):
                return s
            m = re.match(r"^([\w$]+)(\(.*)?$", s)
            if not m:
                report.append((rel, s, "?", "unparsed")); return s
            name, desc = m.group(1), m.group(2)
            results = set(); notes = []
            for ow in owners or [None]:
                n, d, note = resolve_method(ow, name, desc)
                if n: results.add((n, d))
                notes.append(note)
            if len(results) == 1:
                n, d = results.pop()
                out = n + (d or "")
                report.append((rel, s, out, ",".join(x for x in notes if x)))
                return out
            report.append((rel, s, "!!", "; ".join(notes) or f"multiple {results}"))
            return s

        def fix_method_attr(m):
            val = m.group(2)
            return m.group(1) + STR.sub(lambda s: '"' + fix_method_string(s.group(1)) + '"', val)

        def fix_target(m):
            t = m.group(2)
            mt = re.match(r"^L([^;]+);([\w$<>]+)(\(.*)$", t) or re.match(r"^([\w/$]+)\.([\w$<>]+)(\(.*)$", t)
            if mt:
                yowner, name, desc = mt.groups()
                off_owner = y_cls_n2o.get(yowner)
                if off_owner is None:            # java/... etc.
                    out = "L" + yowner + ";" + name + o2m_desc(y2o_desc(desc))
                    if out != t: report.append((rel, t, out, "non-mc owner"))
                    return m.group(1) + '"' + out + '"'
                mo = m_cls_o2m.get(off_owner, off_owner)
                if name in SKIP_NAMES:
                    n = name; d = o2m_desc(y2o_desc(desc)); note = ""
                else:
                    n, d, note = resolve_method(off_owner, name, desc, declared_only=False)
                if not n:
                    report.append((rel, t, "!!", note)); return m.group(0)
                out = "L" + mo + ";" + n + d
                report.append((rel, t, out, note))
                return m.group(1) + '"' + out + '"'
            report.append((rel, t, "!!", "unparsed target")); return m.group(0)

        def fix_invoker(m):
            kind, name = m.groups()
            for ow in owners:
                if kind == "Invoker":
                    n, _, note = resolve_method(ow, name)
                else:
                    n, note = resolve_field(ow, name), ""
                if n:
                    report.append((rel, name, n, f"@{kind}")); return f'@{kind}("{n}")'
            report.append((rel, name, "!!", f"@{kind} unresolved")); return m.group(0)

        new = METHOD_ATTR.sub(fix_method_attr, new)
        new = TARGET_ATTR.sub(fix_target, new)
        new = INVOKER.sub(fix_invoker, new)
        if new != text:
            changed_files += 1
            if WRITE:
                open(path, "w", encoding="utf-8").write(new)

bad = [r for r in report if r[2] == "!!"]
for r in report:
    print(("FAIL " if r[2] == "!!" else "ok   ") + f"{r[0]}: {r[1]}  ->  {r[2]}  {r[3]}")
print(f"\n{len(report)} strings, {len(bad)} unresolved, {changed_files} files changed, write={WRITE}")
