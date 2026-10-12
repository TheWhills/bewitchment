"""Convert eager `public static final T NAME = create("id", EXPR, ...)` registry fields to
Forge `RegistryObject<T>` fields registered through suppliers, and add `.get()` to every
read of those fields across the source tree.

Usage: python tools/deferred_register.py <src_root> <RegistryClass.java> [...] [--write]

Per field:   public static final T NAME = create("id", EXPR, rest)
becomes:     public static final RegistryObject<T> NAME = create("id", () -> EXPR, rest)
Fields whose create(...) has only the id argument (e.g. sounds, recipe types) just get
retyped. The create helpers and init methods themselves are rewritten by hand.
"""
import os, re, sys

WRITE = "--write" in sys.argv
args = [a for a in sys.argv[1:] if a != "--write"]
src_root, classes = args[0], args[1:]

FIELD = re.compile(r"^(\tpublic static final )([\w<>?,. \[\]]+?) ([A-Z][A-Z0-9_]*) = create\(", re.M)


def match_paren(s, i):
    """s[i] == '('; return index of the matching ')', skipping strings/chars/comments."""
    depth, j = 0, i
    while j < len(s):
        c = s[j]
        if c in "\"'":
            q = c; j += 1
            while s[j] != q:
                j += 2 if s[j] == "\\" else 1
        elif s.startswith("//", j):
            j = s.index("\n", j)
        elif s.startswith("/*", j):
            j = s.index("*/", j) + 1
        elif c in "([{":
            depth += 1
        elif c in ")]}":
            depth -= 1
            if depth == 0:
                return j
        j += 1
    raise ValueError("unbalanced")


def split_args(s):
    """Split a call's argument text at top-level commas."""
    out, depth, cur, j = [], 0, [], 0
    while j < len(s):
        c = s[j]
        if c in "\"'":
            k = j + 1
            while s[k] != c:
                k += 2 if s[k] == "\\" else 1
            cur.append(s[j:k + 1]); j = k + 1; continue
        if c in "([{<" and not (c == "<" and depth == 0 and False):
            depth += 1 if c != "<" else 0
        if c in ")]}":
            depth -= 1
        if c == "," and depth == 0:
            out.append("".join(cur)); cur = []
        else:
            cur.append(c)
        j += 1
    out.append("".join(cur))
    return out


converted = {}  # class simple name -> set of field names
for path in classes:
    cls = os.path.basename(path)[:-5]
    text = open(path, encoding="utf-8").read()
    names, out, pos = set(), [], 0
    for m in FIELD.finditer(text):
        open_i = m.end() - 1
        close_i = match_paren(text, open_i)
        a = split_args(text[open_i + 1:close_i])
        if len(a) >= 2 and a[0].strip().startswith('"'):
            a[1] = " () -> " + a[1].lstrip()
        new = f"{m.group(1)}RegistryObject<{m.group(2)}> {m.group(3)} = create(" + ",".join(a) + ")"
        out.append(text[pos:m.start()]); out.append(new); pos = close_i + 1
        names.add(m.group(3))
    out.append(text[pos:])
    text = "".join(out)
    # bare references inside the class itself (not the declarations, not after '.')
    decl = re.compile(r"RegistryObject<[^=]*> (" + "|".join(sorted(names, key=len, reverse=True)) + r") =")
    decl_spans = {m.start(1) for m in decl.finditer(text)}
    bare = re.compile(r"(?<![\w.])(" + "|".join(sorted(names, key=len, reverse=True)) + r")\b(?!\s*\.get\(\))(?!\s*=[^=])")
    text = bare.sub(lambda m: m.group(0) if m.start(1) in decl_spans else m.group(1) + ".get()", text)
    converted[cls] = names
    print(f"{cls}: {len(names)} fields")
    if WRITE:
        open(path, "w", encoding="utf-8").write(text)

# qualified references everywhere
total = 0
for root, _, files in os.walk(src_root):
    for fn in files:
        if not fn.endswith(".java"):
            continue
        p = os.path.join(root, fn)
        s = open(p, encoding="utf-8").read()
        n0 = s
        for cls, names in converted.items():
            if not names:
                continue
            rx = re.compile(r"\b" + cls + r"\.(" + "|".join(sorted(names, key=len, reverse=True)) + r")\b(?!\s*\.get\(\))")
            s, k = rx.subn(lambda m: m.group(0) + ".get()", s)
            total += k
        if s != n0 and WRITE:
            open(p, "w", encoding="utf-8").write(s)
print(f"qualified references rewritten: {total}, write={WRITE}")
