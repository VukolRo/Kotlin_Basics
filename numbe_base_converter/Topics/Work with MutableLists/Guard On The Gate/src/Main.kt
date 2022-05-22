fun main() {
    val backToTheWall = readLine()!!.split(", ").map { it }.toMutableList()
    val returnedWatchman = readLine()!!
    backToTheWall.add(returnedWatchman)
    println(backToTheWall.joinToString())
}