package core
import scala.util.{Failure, Success, Try}

object Processor {

  def process(cmd: Commands, rep: PollRepoInMemory): Either[String, String] = {
    cmd match {
      case cmd: CommandCreatePoll => cmdCreatePoll(cmd, rep)
      case cmd: CommandList => cmdList(cmd, rep)
      case cmd: CommandStartPoll => cmdStartPoll(cmd, rep)
      case cmd: CommandResult => cmdResult(cmd, rep)
      case cmd: CommandDeletePoll => cmdDeletePoll(cmd, rep)
      case cmd: CommandStopPoll => cmdStopPoll(cmd, rep)
    }

  }

  def cmdCreatePoll(cmd: CommandCreatePoll, PR: PollRepoInMemory): Either[String, String] = {

      val poll = Poll(cmd.name, cmd.anonymous, cmd.visible, cmd.startTime, cmd.stopTime)
    Try(PR.store(poll)) match {
      case Success(_)=> Right(poll.id.toString)
      case Failure(_) => Left(Report.add("CREATE FAIL"))
    }
   }

  def cmdStartPoll(cmd: CommandStartPoll, PR: PollRepoInMemory): Either[String, String] = {
    PR.isContains(cmd.id) match {
      case false => Left(Report.add("ERROR BY ID - NOT EXIST!"))
      case true => PR.hasStart(cmd.id) match {
        case true => Left(Report.add("ERROR BY TIME - NOT START!"))
        case false =>
          PR.start(cmd.id)
          Right(Report.add("START OK!"))
      }
    }

  }

  def cmdStopPoll(cmd: CommandStopPoll, PR: PollRepoInMemory): Either[String, String] = {
    PR.isContains(cmd.id) match {
      case false => Left(Report.add("ERROR BY ID - NOT EXIST!"))
      case true => PR.hasEnd(cmd.id) match {
        case true => Left(Report.add("ERROR BY TIME - NOT END!"))
        case false =>
          PR.end(cmd.id)
          Right(Report.add("END OK!"))
      }
    }
  }

  def cmdResult(cmd: CommandResult, PR: PollRepoInMemory): Either[String, String] = {
    PR.isContains(cmd.id) match {
      case true =>
        Right(Report.add("RESULT OK: " + PR.get(cmd.id).get.name +
          {PR.get(cmd.id).get.used match {
          case true => " USED "
          case false => " NOT USED "
        }} +
          {PR.get(cmd.id).get.launch match {
          case true => "STARTED "
          case false => "NOT STARTED"
        }}
        ))
      case false => Left(Report.add("ERROR BY ID - NOT EXIST"))
    }
  }

  def cmdDeletePoll(cmd: CommandDeletePoll, PR: PollRepoInMemory): Either[String, String] = {
    PR.isContains(cmd.id) match {
      case false => Left(Report.add("ERROR BY ID - NOT EXIST!"))
      case true =>
        PR.isLaunch(cmd.id) match {
          case true => Left(Report.add("ERROR BY LAUNCH - NOT DELETE!"))
          case false => {
            PR.delete(cmd.id)
            Right(Report.add("DELETE OK!"))
          }
        }
    }
  }

  def cmdList(cmd: CommandList, PR: PollRepoInMemory): Either[String, String] = {
    PR.polls.isEmpty match {
      case true => Left(Report.add("SORRY, LIST IS EMPTY"))
      case false => {
        Right(PR.all().map(i => i.name + " имеет id = " + i.id).mkString("\n"))


      }
    }
  }
}



