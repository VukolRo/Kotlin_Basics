const val MAX = 1000
const val MIN = 1
fun main() {
    val a = readln().toInt()
    val b = readln().toInt()
    val c = readln().toInt()
    val d = readln().toInt()
    for (i in MIN..MAX) {
        if (a * i * i * i + b * i * i + c * i + d == 0) {
            println(i)
        }
    }
}
