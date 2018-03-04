import java.util.Calendar

import scala.collection.mutable.ListBuffer

trait PollRepo {
  def store(poll: Poll): Unit
  def get(id: Int):Option[Poll]
  def all():Seq[Poll]
  def delete(id:Int):Unit
}

class PollRepoInMemory extends PollRepo{
  var polls:Map[Int, Poll] = Map.empty
  override def store(poll: Poll): Unit =
    polls += (poll.id -> poll)

  override def get(id: Int): Option[Poll] =
    polls.get(id)

  override def all(): Seq[Poll] = polls.values.toList

  override def delete(id: Int): Unit = polls.
}

class FileReader(pollRepo: PollRepo) {
  def read(name:String) {
    val filename = "C:\\Users\\Go-go\\Desktop\\Scala_work\\src\\main\\scala\\in.txt"
    val output = scala.io.Source.fromFile(filename).mkString.trim
    val lines_array = parse_text(output) // массив строк
    choose_function(lines_array)


  }

  def parse_text(commands : String): Seq[Seq[String]] = {
    def parseCommand(text : String): Seq[String] = {
      val parts = text.split(" ")

      def command: String = parts.head.tail
      def params: Seq[String] = parts.tail.map(x => x.init.tail)

      command +: params
    }

    commands.split("\r\n\r\n").map(parseCommand)
  }

  def choose_function(lines : Seq[Seq[String]]): Unit = {
    println( Calendar.getInstance (). getTime ())
    for {line <- lines}{
      line(0) match {
          case "create_poll" => create_poll(line)
          case "list" => for (i <- pollRepo.all) println(i.name, i.id)
          case "delete_poll" => delete_poll(line(1).init.tail.toInt)
         // case "start_poll" => start_poll(Int(line(1)))
          // case "stop_poll" => stop_poll(Int(line(1)))
         // case "result" => result(Int(line(1)))
          case _ => print("hello")
      }
    }
  }

  def create_poll(lines: Seq[String]): Unit ={
    val poll = new Poll(lines)
    pollRepo.store(poll)
    println(poll.id)
  }

  def delete_poll(id: Int) = {
    /*
    for {i <- 0 to poll_list.length-1} {
      if (poll_list(i).id == id) {
        if (poll_list(i).launch)
          println("ERROR BY LAUNCH(NOT DELETE)")
        else{
          poll_list.remove(i)
          println("DELETE OK!")
        }
      }
    }
    */
    pollRepo.pollRepo.get(id)
    ???
  }

  def start_poll(id: Int): Unit ={

  }

  def stop_poll(id: Int): Unit ={

  }

  def result(id: Int): Unit ={

  }
}
// parser combinator