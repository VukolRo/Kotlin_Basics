fun main() {
    val a = readln().toInt()
    print(
        if (a > 0) {
            "positive"
        } else if (a == 0) {
            "zero"
        } else {
            "negative"
        }
    )
}
