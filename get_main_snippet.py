import re

with open('./app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

# Let's locate the "if (tabIndex == 2)" block
pattern = re.compile(r"(if \(tabIndex == 2\) \{)(.*?)(\} // Close if \(selectedTabIndex == 2\))", re.DOTALL)
match = pattern.search(content)
if match:
    print("MATCH FOUND")
    print(match.group(2))
else:
    print("NO MATCH")
