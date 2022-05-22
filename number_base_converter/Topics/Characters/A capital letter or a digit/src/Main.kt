fun main() {
    val ch = readln().first()
    if ( ch.isUpperCase() || ch.isDigit() && ch != '0') {
        println(true)
    } else {
        println(false)
    }
}