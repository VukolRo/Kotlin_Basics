fun main() {

    val a = readLine()!!.toInt()
    val b = readLine()!!.toInt()
    val c = readLine()!!.toInt()

    if (a > c && a > b) {
        println(a)
    } else if (b > c && b > a) {
        println(b)
    } else if (c > a && c > b) {
        println(c)
    }
}
