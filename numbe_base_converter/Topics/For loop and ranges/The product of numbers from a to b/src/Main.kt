fun main() {
    val a = readln().toLong()
    val b = readln().toLong()
    var prod = 1L
    for (i in a until b) {
        prod *= i
    }
    println(prod)
}