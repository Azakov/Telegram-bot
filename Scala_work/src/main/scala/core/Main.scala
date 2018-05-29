package core

import java.util.{Calendar, Date}

import info.mukel.telegrambot4s.models.User


object Main {
  def main(args: Array[String]) {

  MyBot.run()
  }

  def botRun(user:User,string: String,DateOfPoll:PollRepoInMemory): String ={
    val s  = for {
      cmd <- ParseData.parseDate(string)
      result <- Processor.process(cmd,DateOfPoll,user)
    } yield result
    s.right.getOrElse("Left") match {
      case "Left" => s.left.get
      case  _ => s.right.get
    }
  }
}