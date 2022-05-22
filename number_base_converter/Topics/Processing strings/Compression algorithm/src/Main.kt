fun main() {
    val str = readln()
    var count = 1
    var ret = ""
    for (i in 0..str.lastIndex) {
        if (i != str.lastIndex) {
            if (str[i] == str[i + 1]) {
                count++
            } else {
                ret += "" + str[i] + count
                count = 1
            }
        } else {
            ret += "" + str[i] + count 
        }
    }
    println(ret)
}
