fun main() {
    val (name, sName, age) = readln().split(" ")
    if (age.toInt() == 1) {
        println("${name.first()}. $sName, $age year old")
    } else {
        println("${name.first()}. $sName, $age years old")
    }
}
