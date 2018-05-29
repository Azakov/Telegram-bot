package core

import java.util.Date

trait CommandsBot

final case class CommandList() extends CommandsBot

final case class CommandDeletePoll(id:Int) extends CommandsBot

final case class CommandResult(id:Int) extends CommandsBot

final case class CommandStopPoll(id:Int) extends CommandsBot

final case class CommandStartPoll(id:Int) extends CommandsBot

final case class CommandCreatePoll(name: String, anonymous: Boolean = true,visible: Boolean = true,
                                   startTime: Option[Date] = None,
                                   stopTime: Option[Date]  =None
                                  ) extends CommandsBot

final case class CommandBegin(id:Int) extends CommandsBot

final case class CommandEnd(id:Int) extends CommandsBot

final case class CommandView() extends CommandsBot

final case class CommandDeleteQuestion(id:Int) extends CommandsBot

final case class CommandAddQuestion(question: String, typeQuest:QuestionType.QuestionType = QuestionType.open,
                                    answers:String = "") extends CommandsBot

final case class  CommandAnswer(id: Int,answer:String) extends CommandsBot