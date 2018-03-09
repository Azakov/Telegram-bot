import java.util.Calendar

class PollWorker(pollRepo: PollRepo) {
  def main_function(commands : String) = choose_function(parse_text(commands))

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
    for {line <- lines}{
      line(0) match {
        case "create_poll" => create_poll(line)
        case "list" => for (i <- pollRepo.all) println(i.name, i.id)
        case "delete_poll" => delete_poll(line(1).toInt)
        case "start_poll" => start_poll(line(1).toInt)
        case "stop_poll" => stop_poll(line(1).toInt)
        case "result" => result(line(1).toInt)
        case _ => print("ERROR BY UNKNOWN COMMAND")
      }
    }
  }

  def create_poll(lines: Seq[String]): Unit ={
    val poll = new Poll(lines)
    pollRepo.store(poll)
    println(poll.id)
  }

  def delete_poll(id: Int) = {
    pollRepo.isContains(id) match {
      case false => println("ERROR BY ID - NOT DELETE!")
      case true =>
        pollRepo.isLaunch(id) match {
          case true => println("ERROR BY LAUNCH - NOT DELETE!")
          case false => {
            pollRepo.delete(id)
            println("DELETE OK!")
          }
        }
      }
    }

  def start_poll(id: Int): Unit = {
    pollRepo.isContains(id) match {
      case false => println("ERROR BY ID - NOT START!")
      case true => pollRepo.hasStart(id) match {
        case true => println("ERROR BY TIME - NOT START!")
        case false => {
          pollRepo.start(id)
          println("START OK!")
        }
      }
    }
  }
  def stop_poll(id: Int): Unit = {
    pollRepo.isContains(id) match {
      case false => println("ERROR BY ID - NOT END!")
      case true => pollRepo.hasEnd(id) match {
        case true => println("ERROR BY TIME - NOT END!")
        case false => {
          pollRepo.end(id)
          println("END OK!")
        }
      }
    }
  }

  def result(id: Int): Unit = {
    pollRepo.isContains(id) match {
      case true =>{ print(pollRepo.get(id).get.name)
        pollRepo.get(id).get.used match {
        case true => print(" PASSED ")
        case false => print(" NOT PASSED ")
        }
        pollRepo.get(id).get.launch match{
          case true => println("STARTED ")
          case false => println("NOT STARTED")
        }
      }
      case false => println("ERROR BY ID")
    }

  }
}
