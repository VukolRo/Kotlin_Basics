fun main() {
    val range1 = readln().toInt()..readln().toInt()
    val range2 = readln().toInt()..readln().toInt()
    val num = readln().toInt()
    if (num in range1 || num in range2) {
        println(true)
    } else {
        println(false)
    }
}