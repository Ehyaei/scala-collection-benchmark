package benchmark

object Utils {
  def timeElapsing(benchmarkFunction: => Unit, message:Boolean = false)(times:Int = 1): Double = {
    if(message) println("Benchmark: IS Starting ...")
    val startTime = System.nanoTime()
    for (_ <- 0 until times)
      benchmarkFunction
    val endTime = System.nanoTime()
    val timeElapsed = (endTime - startTime).toDouble / times.toDouble
    if(message) println(s"Operation Took $timeElapsed ms on average")
    timeElapsed
  }
}
