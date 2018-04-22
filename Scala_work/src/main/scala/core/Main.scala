package core


object Main {
  def main(args: Array[String]) {
    val DateOfPoll = new PollRepoInMemory
    val data = Request.request // для парсинга байтов в строку - реализуется чуть позже
    val s  = for {
      cmd <- ParseData.parseDate("/create_poll (yes) (afterstop) (12:28:30,18:02:03)")   //сюда data
      result <- Processor.process(cmd,DateOfPoll)
    } yield result
    s.right.getOrElse("Left") match {
      case "Left" => println(s.left.get)
      case  _ => println(s.right.get)
    }

  }
}