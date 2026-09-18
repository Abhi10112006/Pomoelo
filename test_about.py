with open('./app/src/main/java/com/example/ui/components/AboutScreen.kt', 'r') as f:
    content = f.read()

target = """                    isBreathing = !isCredits // Stop heavy breathing on credits for subtleness
                )
            }"""

repl = """                    isBreathing = !isCredits, // Stop heavy breathing on credits for subtleness
                    isSpeaking = (!isCredits && charsToDisplay < messages[step].length)
                )
            }"""

content = content.replace(target, repl)

with open('./app/src/main/java/com/example/ui/components/AboutScreen.kt', 'w') as f:
    f.write(content)
