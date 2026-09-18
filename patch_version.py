import re

with open('./app/src/main/java/com/example/UpdateChecker.kt', 'r') as f:
    content = f.read()

content = content.replace('private const val CURRENT_VERSION = "2.0"', 'private const val CURRENT_VERSION = "3.0"')

with open('./app/src/main/java/com/example/UpdateChecker.kt', 'w') as f:
    f.write(content)
