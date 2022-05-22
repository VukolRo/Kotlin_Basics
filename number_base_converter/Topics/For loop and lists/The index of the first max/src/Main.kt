fun main() {
    val size = readln().toInt()
    val list = mutableListOf<Int>()
    for (i in 0 until size) {
        list.add(readln().toInt())
    }
    for (index in list.indices) {
        if (list[index] == list.maxOrNull()) {
            println(index)
            break
        }
    }
}