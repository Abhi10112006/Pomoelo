import java.io.File

fun main() {
    val lines = File("app/src/main/java/com/example/MainActivity.kt").readLines()
    var count = 0
    for ((index, line) in lines.withIndex()) {
        val cleanLine = line.replace("\".*?\"".toRegex(), "\"\"").replace("//.*".toRegex(), "")
        for (char in cleanLine) {
            if (char == '{') count++
            if (char == '}') count--
        }
        if (count == 0 && cleanLine.contains("}")) {
            println("Root closed at line ${index + 1}")
        } else if (count < 0) {
            println("Negative brace count at line ${index + 1}!")
            count = 0 // reset to continue
        }
    }
    println("Final count: $count")
}
main()
