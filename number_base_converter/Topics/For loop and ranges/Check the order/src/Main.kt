fun main() {
    val len = readln().toInt()
    var a = readln().toInt()
    var b = 0
    var i = 0
    var flag = false
    while (i++ < len - 1 && !flag) {
        b = readln().toInt()
        if (a > b) {
            flag = true
        }
        a = b
    }
    if (flag) {
        println("NO")
    } else {
        println("YES")
    }
}