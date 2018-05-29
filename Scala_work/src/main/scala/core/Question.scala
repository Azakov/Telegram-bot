package core

import core.QuestionType.QuestionType
import info.mukel.telegrambot4s.models.User

case class Question (user: User,idPoll:Int, question:String,answers:String,
                    qType:QuestionType,idQuest:Int,voted:Map[User,Any] =  Map.empty){

  def store(user:User,any: Any): Question ={
    val newVoted = voted + (user -> any)
    val newQuestion = copy(voted = newVoted)
    newQuestion
  }
}