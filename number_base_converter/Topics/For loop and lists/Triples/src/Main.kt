fun main() {
    val size = readln().toInt()
    val list = mutableListOf<Int>()
    var count = 0
    for (i in 0 until size) {
        list.add(readln().toInt())
    }
    for (i in 0..list.lastIndex - 2) {
        if (list[i] == list[i + 1] - 1 && list[i] == list[i + 2] - 2) {
            count++
        }
    }
    println(count)
}
