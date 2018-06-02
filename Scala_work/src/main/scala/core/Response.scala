package core

import info.mukel.telegrambot4s.models.User

object Response {
  def pollCreate(id:Int):String = s"${id}"
  def  startOk = "START OK!"
  def  endOk = "END OK!"
  def deleteOk() = "DELETE OK!"
  def cmdList(PR:PollRepoInMemory) =  PR.all().map(i => i.name + " имеет id = " + i.id).mkString("\n")
  def beginOk = "SUCCESS BEGIN"
  def endContextOk = "SUCCESS END"
  def addOk(question:Question) = question.idQuest.toString()
  def deleteQuestOk() = "DELETE SUCCESS"
  def answerOk() = "ANSWER SUCCESS"
  def resultOK(string: String) = s"RESULT:\n ${string}"
  def viewOk(poll: Poll,user: User):String = s"""name : ${user.firstName} id: ${user.id}
   poll: ${poll.id} visible: ${poll.visible} anonymous: ${poll.anon}"""
}
