package core
import info.mukel.telegrambot4s.api.{Polling, TelegramBot}
import info.mukel.telegrambot4s.api.declarative.Commands


object MyBot extends TelegramBot with Polling  with Commands {
  lazy val token: String =  "564259146:AAEEFvLzlcxP-tFKSDnqmYxH7T_n-exjafY"
  val DateOfPoll = new PollRepoInMemory
  onCommand('hello) { implicit msg => reply(s"""Hello ${msg.from.get.firstName}""")}

  val listCommand = List("create_poll","list","view","delete_poll","start_poll",
    "stop_poll","result","begin","end","add_question","answer" ,"delete_question")

for (c <- listCommand)
  onCommand(c) {
    implicit msg => reply(Main.botRun(msg.from.get, msg.text.get,DateOfPoll))
  }

}




