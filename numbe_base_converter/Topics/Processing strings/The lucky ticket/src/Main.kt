fun main() {
    val str = readln()
    var first = 0
    var second = 0
    var count = 0
    for (ch in str) {
        if (count < 3) {
            first += ch.toInt()
        } else {
            second += ch.toInt()
        }
        count++
    }
    if (first == second) {
        println("Lucky")
    } else {
        println("Regular")
    }
}
