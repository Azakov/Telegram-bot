package core
import java.text.SimpleDateFormat
import java.util.Date

import scala.util.Try
import scala.util.parsing.combinator._

object ParserCommand extends RegexParsers with Commands   {

  def applyParse(text:String) : Either[String,Commands] = {
    parse(parserCommand,text) match {
      case Success(a, _) => Right(a)
      case NoSuccess(msg, _) => Left(msg)
    }
  }

  def parserCommand: Parser[Commands] = {
    parserCommandCreatePoll|
    parserCommandStartPoll|
    parserCommandDeletePoll|
    parserCommandResult|
    parserCommandList|
    parserCommandStopPoll
  }

//
def parserCommandCreatePoll: Parser[CommandCreatePoll] = {
  for {
    _ <- literal("/create_poll").withFailureMessage("Unknown Command")
    _ <- space
    head <- name.withFailureMessage("CREATE POLL:Name not found")
    _ <- space
    anonO <- optional(anonymous.withFailureMessage("CREATE POLL:Expected yes/no but found sth shit"))
    anon = anonO.getOrElse(true)
    _ <-space
    visO <- optional(visibile.withFailureMessage("CREATE POLL:Expected afterstop/continuous but found sth shit"))
    vis = visO.getOrElse(true)
    _ <- space
    dateStartO <- optional(date.withFailureMessage("CREATE POLL:Expected Date HH:mm:ss,yy:MM:dd  but found sth shit"))
    dateStart = dateStartO.flatten
    _ <- space
    dateEndO <- optional(date.withFailureMessage("CREATE POLL:Expected Date HH:mm:ss,yy:MM:dd but found sth shit"))
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
      i <- id.withFailureMessage("START POLL:Expected number but found sth shit")
    }yield CommandStartPoll(i)
  }
  def parserCommandStopPoll: Parser[CommandStopPoll] = {
    for {
      _ <- literal("/stop_poll").withFailureMessage("Unknown Command")
      _ <- space
      i <- id.withFailureMessage("STOP POLL:Expected number but found sth shit")
    } yield CommandStopPoll(i)
  }
  def parserCommandResult: Parser[CommandResult] = {
    for {
      _ <- literal("/result").withFailureMessage("Unknown Command")
      _ <- space
      i <- id.withFailureMessage("RESULT:Expected number but found sth shit")
    } yield CommandResult(i)
  }
  def parserCommandDeletePoll: Parser[CommandDeletePoll] = {
    for {
      _ <- literal("/delete_poll").withFailureMessage("Unknown Command")
      _ <- space
      i <- id.withFailureMessage("DELETE POLL:Expected number but found sth shit")
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
}


