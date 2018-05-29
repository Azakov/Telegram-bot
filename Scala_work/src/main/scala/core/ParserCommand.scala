package core
import java.text.SimpleDateFormat
import java.util.Date
import scala.util.Try
import scala.util.parsing.combinator._
import core.QuestionType.QuestionType


object ParserCommand extends RegexParsers with CommandsBot   {

  def applyParse(text:String) : Either[String,CommandsBot] = {
    parse(parserCommand,text) match {
      case Success(a, _) => Right(a)
      case NoSuccess(msg, _) => Left(msg)
    }
  }

  def parserCommand: Parser[CommandsBot] = {
    parserCommandCreatePoll|
    parserCommandStartPoll|
    parserCommandDeletePoll|
    parserCommandResult|
    parserCommandList|
    parserCommandStopPoll|
    parserCommandBegin|
    parserCommandEnd|
    parserCommandAddQuestion|
    parserCommandDeleteQuestion|
    parserCommandView|
    parserCommandAnswer
  }

def parserCommandBegin: Parser[CommandBegin] = {
  for {
    _ <-literal("/begin").withFailureMessage("Unknown Command")
    _ <-space
    i <- id.withFailureMessage("BEGIN:Expected number but found sth")
  }yield CommandBegin(i)
}

  def parserCommandEnd: Parser[CommandEnd] = {
    for {
      _ <- literal("/end").withFailureMessage("Unknown Command")
      _ <-space
      i <- id.withFailureMessage("END:Expected number but found sth")
    }yield CommandEnd(i)
  }

def parserCommandView:  Parser[CommandView] = {
  for{
    _ <- literal("/view").withFailureMessage("Unknown Command")
  } yield CommandView()
}
  def parserCommandDeleteQuestion: Parser[CommandDeleteQuestion] = {
    for {
      _ <- literal("/delete_question").withFailureMessage("Unknown Command")
      _ <- space
      i <- id.withFailureMessage("DELETE QUESTION:Expected number but found sth")
    }yield CommandDeleteQuestion(i)
  }

  def parserCommandAnswer: Parser[CommandAnswer] = {
    for {
      _ <- literal("/answer").withFailureMessage("Unknown Command")
      _ <- space
      i <- id.withFailureMessage("ANSWER:Expected number but found sth")
      _ <- space
      answer <- name.withFailureMessage("ANSWER:answer not found")
    } yield  CommandAnswer(i,answer)
  }

  def parserCommandAddQuestion: Parser[CommandAddQuestion] = {
    for {
      _ <- literal("/add_question").withFailureMessage("Unknown Command")
      _ <- space
      quest <- name.withFailureMessage("ADD QUESTION:Expected name but found sth")
      _ <- space
      typeQuestionO <- optional(questionType.
        withFailureMessage("ADD QUESTION:Expected open/choice/multi but found sth"))
      typeQuestion = typeQuestionO.getOrElse(QuestionType.open)
      _ <- space
      answersO <- optional(argument.withFailureMessage("ADD QUESTION:Expected questions  but found sth"))
      answers = answersO.getOrElse("")
    }yield  CommandAddQuestion(quest,typeQuestion,answers)
  }

  def parserCommandCreatePoll: Parser[CommandCreatePoll] = {
    for {
      _ <- literal("/create_poll").withFailureMessage("Unknown Command")
      _ <- space
      head <- name.withFailureMessage("CREATE POLL:Name not found")
      _ <- space
      anonO <- optional(anonymous.withFailureMessage("CREATE POLL:Expected yes/no but found sth"))
      anon = anonO.getOrElse(true)
      _ <-space
      visO <- optional(visibile.withFailureMessage("CREATE POLL:Expected afterstop/continuous but found sth"))
      vis = visO.getOrElse(true)
      _ <- space
      dateStartO <- optional(date.withFailureMessage("CREATE POLL:Expected Date HH:mm:ss,yy:MM:dd  but found sth"))
      dateStart = dateStartO.flatten
      _ <- space
      dateEndO <- optional(date.withFailureMessage("CREATE POLL:Expected Date HH:mm:ss,yy:MM:dd but found sth"))
      dataEnd = dateEndO.flatten
    } yield  CommandCreatePoll(head,anon,vis,dateStart,dataEnd)
  }


  def parserCommandList: Parser[CommandList] = {
    for {
      _ <-literal("/list").withFailureMessage("Unknown Command")
    } yield CommandList()
  }
  def parserCommandStartPoll: Parser[CommandStartPoll] = {
    for {
      _ <- literal("/start_poll").withFailureMessage("Unknown Command")
      _ <- space
      i <- id.withFailureMessage("START POLL:Expected number but found sth")
    }yield CommandStartPoll(i)
  }
  def parserCommandStopPoll: Parser[CommandStopPoll] = {
    for {
      _ <- literal("/stop_poll").withFailureMessage("Unknown Command")
      _ <- space
      i <- id.withFailureMessage("STOP POLL:Expected number but found sth")
    } yield CommandStopPoll(i)
  }
  def parserCommandResult: Parser[CommandResult] = {
    for {
      _ <- literal("/result").withFailureMessage("Unknown Command")
      _ <- space
      i <- id.withFailureMessage("RESULT:Expected number but found sth")
    } yield CommandResult(i)
  }
  def parserCommandDeletePoll: Parser[CommandDeletePoll] = {
    for {
      _ <- literal("/delete_poll").withFailureMessage("Unknown Command")
      _ <- space
      i <- id.withFailureMessage("DELETE POLL:Expected number but found sth")
    } yield CommandDeletePoll(i)
  }


  def optional[T](p: Parser[T]): Parser[Option[T]] =
    """$""".r ^^^ None | p ^^ (x => Some(x))

  def anonymous: Parser[Boolean] =
    "(" ~> "yes" <~ ")" ^^^ true |  "(" ~> "no" <~ ")"  ^^^ false

  def visibile: Parser[Boolean] =
    "(" ~> "afterstop" <~ ")" ^^^ true | "(" ~>"continuous" <~ ")" ^^^ false

  def  date: Parser[Option[Date]] = "(" ~> """[^()]+""".r <~ ")" ^?
    {case date => toDate(date)}

  def toDate(s:String): Option[Date] = Try(new SimpleDateFormat("HH:mm:ss,yy:MM:dd").parse(s)).toOption

  def id: Parser[Int] = "(" ~ """\d+""".r ~ ")" ^^ {case _ ~ id ~_ => id.toInt}
  def space: Parser[String] = """\s*""".r

  def leftBracket:  Parser[String] = "((" ^^ {_ => "("}
  def rightBracket: Parser[String] = "))" ^^ {_ => ")"}

  def name: Parser[String] =  "(" ~ argument ~ ")" ^^{case _ ~ arg ~_ => arg}
  def argument: Parser[String] = rep1(leftBracket|rightBracket|argumentName) ^^ {_.mkString}
  def argumentName: Parser[String] = """[^()]+""".r ^^ {_.mkString}

  def questionType: Parser[QuestionType] =
    "(" ~> "open" <~ ")" ^^^ QuestionType.open|
    "(" ~> "choice" <~ ")" ^^^ QuestionType.choice|
    "(" ~> "multi" <~ ")" ^^^ QuestionType.multi
}


