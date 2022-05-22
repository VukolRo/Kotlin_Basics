fun main() {
    val age = readln().toInt()
    if (age in 18..59) {
        println(true)
    } else {
        println(false)
    }
}