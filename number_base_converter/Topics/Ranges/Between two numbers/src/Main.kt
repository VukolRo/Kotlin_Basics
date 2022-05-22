fun main() {
    val num = readln().toInt()
    val range = readln().toInt()..readln().toInt()
    if (num in range) {
        println(true)
    } else {
        println(false)
    }
}