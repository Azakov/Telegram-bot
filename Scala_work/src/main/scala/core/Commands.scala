package core

import java.util.Date

trait Commands

final case class CommandList() extends Commands

final case class CommandDeletePoll(id:Int) extends Commands

final case class CommandResult(id:Int) extends Commands

final case class CommandStopPoll(id:Int) extends Commands

final case class CommandStartPoll(id:Int) extends Commands

final case class CommandCreatePoll(name: String, anonymous: Boolean = true,visible: Boolean = true,
                                   startTime: Option[Date] = None,
                                   stopTime: Option[Date]  =None
                                  ) extends Commands