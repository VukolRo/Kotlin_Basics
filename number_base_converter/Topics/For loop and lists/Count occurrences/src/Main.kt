fun main() {
    val N = readln().toInt()
    val list = mutableListOf<Int>()
    var count = 0
    for (i in 0 until N) {
        list.add(readln().toInt())
    }
    val M = readln().toInt()
    for (num in list) {
        if (num == M) {
            count++
        }
    }
    println(count)
}