"""Remap @Shadow members the Yarn->Mojmap migration left in Yarn names.

Usage: python tools/remap_shadows.py <mappings_dir> <build.log> [--write]
Reads 'Cannot find target for @Shadow' warnings from a build log, resolves each shadow by
its Yarn name against the mixin's target class (fields: exact owner, falling back to a
unique global match; methods: same parameter count), renames the declaration and its
uses inside that mixin file only, and prints every change. Ambiguous ones are reported.
"""
import os, re, sys
mdir, log = sys.argv[1], sys.argv[2]
APPLY = "--write" in sys.argv
sys.argv = ["x", mdir, "/nonexistent"]
exec(open(os.path.join(os.path.dirname(__file__), "remap_mixins.py")).read().split("# ---------- per-file processing")[0])

y_field_by_name = {}
for ow, lst in y_field.items():
    for off, d, nm in lst:
        y_field_by_name.setdefault(nm, []).append((ow, off, d))

def param_count(desc):
    inner = desc[1:desc.index(")")]
    return len(re.findall(r"\[*(?:L[^;]+;|[ZBCSIJFD])", inner))

def resolve_field(owner, yname):
    hits = set()
    for ow, off, d in y_field_by_name.get(yname, []):
        if ow == owner:
            m = m_field.get(ow, {}).get(off)
            if m: return {m}
        m = m_field.get(ow, {}).get(off)
        if m: hits.add(m)
    return hits

def resolve_method(owner, yname, nparams):
    own, glob_hits = set(), set()
    for ow, off, d in y_by_name.get(yname, []):
        if param_count(d) != nparams: continue
        here = m_meth.get(owner, {}).get((off, d)); there = moj_method(ow, off, d)
        if here and here == there: own.add(here)
        if there: glob_hits.add(there)
    return own or glob_hits

warn = re.compile(r"src/main/java/(\S+\.java):(\d+): warning: Cannot find target for @Shadow (method|field) in (\S+)")
changes, problems = [], []
seen = set()
for path, line, kind, target in warn.findall(open(log).read()):
    key = (path, line)
    if key in seen: continue
    seen.add(key)
    f = "src/main/java/" + path
    lines = open(f).read().split("\n")
    i = int(line) - 1
    while i < len(lines) and (lines[i].strip().startswith("@") or not lines[i].strip().endswith(";")):
        i += 1
    decl = lines[i]
    owner = m_cls_m2o.get(target.replace(".", "/"))
    if kind == "field":
        m = re.search(r"(\w+)\s*(?:=[^;]*)?;\s*$", decl)
        name = m.group(1); cands = resolve_field(owner, name)
    else:
        m = re.search(r"(\w+)\((.*)\)\s*;", decl)
        name = m.group(1); params = m.group(2).strip()
        n = 0 if not params else len(re.split(r",(?![^<]*>)", params))
        cands = resolve_method(owner, name, n)
    if len(cands) == 1:
        changes.append((f, int(line), kind, name, cands.pop()))
    else:
        problems.append((path, line, kind, name, sorted(cands)))

by_file = {}
for f, l, kind, old, new in changes:
    by_file.setdefault(f, []).append((kind, old, new))
    print(f"ok   {os.path.basename(f)}:{l} {kind} {old} -> {new}")
for p in problems:
    print("FAIL", p)
for f, renames in by_file.items():
    s = open(f).read()
    for kind, old, new in renames:
        if old == new: continue
        if kind == "field":
            s = re.sub(r"(?<![\w.])" + old + r"\b(?!\s*\()", new, s)
            s = re.sub(r"\bthis\." + old + r"\b", "this." + new, s)
        else:
            s = re.sub(r"(?<![\w.])" + old + r"\(", new + "(", s)
            s = re.sub(r"\bthis\." + old + r"\(", "this." + new + "(", s)
    if APPLY:
        open(f, "w").write(s)
print(f"{len(changes)} resolved, {len(problems)} unresolved, write={APPLY}")
