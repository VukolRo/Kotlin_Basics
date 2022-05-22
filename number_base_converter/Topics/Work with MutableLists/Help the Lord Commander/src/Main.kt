fun main() {  
    val beyondTheWall = readLine()!!.split(", ").map { it }.toMutableList()
    val backToTheWall = readLine()!!.split(", ").map { it }.toMutableList()   
    var flag = true
    for (patroller in beyondTheWall) {
        if (!backToTheWall.contains(patroller)) {
            flag = false
        }
    }
    if (flag) {
        println(true)
    } else {
        println(false)
    }
}