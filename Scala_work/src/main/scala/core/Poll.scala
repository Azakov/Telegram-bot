package core
import java.util.Date

import core.QuestionType.QuestionType

import scala.util.Random
import info.mukel.telegrambot4s.models.User


case  class Poll(name : String,anon : Boolean = true,visible : Boolean = true,
           firstTime : Option[Date] = None,secondTime : Option[Date] = None,admin:User,
                 id:Int = 10101 + Random.nextInt(100)+(Random.nextInt(100)+Random.nextInt(100)*Random.nextInt(10)),
           launch :Boolean = false,
           used:Boolean = false,
           questions:Map[Int,Question] = Map.empty){

  def storeQuestAnswer(question: Question,user: User,answer: Any,anon:Boolean):Poll = {
    val newQuest:Question = question.store(user, answer)
    println(newQuest)
    val newQuests = questions + (question.idQuest -> newQuest)
    println(newQuests)
    val poll = copy(questions = newQuests)
    println(" this"+ poll)
    poll
  }

  def  store(question: Question): Poll ={
    val newQuests = questions + (question.idQuest -> question)
    val poll = copy(questions = newQuests)

    poll
  }

  def delete(id: Int): Poll ={
    val newQuests =  questions - id
    val poll = copy(questions = newQuests)
    poll
  }

  def getType(id:Int): QuestionType = questions(id).qType

}



