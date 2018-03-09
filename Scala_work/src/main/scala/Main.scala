object Main {
  def main(args: Array[String]) {
    val DateOfPoll = new PollRepoInMemory
    val PW = new PollWorker(DateOfPoll)
    val filename = "C:\\Users\\Go-go\\Desktop\\bot telega\\Scala_work\\src\\main\\scala\\in.txt"
    PW.main_function(scala.io.Source.fromFile(filename).mkString.trim)
  }
}