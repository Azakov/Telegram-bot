package core

import info.mukel.telegrambot4s.models.User

case class Answers( idQuest: Int ,
                   answer: String , user: User , isAnon: Boolean,poll:Poll) {

  def openAnswer():Poll ={
      poll.storeQuestAnswer(poll.questions(idQuest),user,answer,isAnon)
  }

  def choiceAnswer():Poll ={
      poll.storeQuestAnswer(poll.questions(idQuest),user,answer.toInt,isAnon)
  }

  def multiAnswer():Poll ={
      poll.storeQuestAnswer(poll.questions(idQuest),user,answer.split("\n").mkString(" "),isAnon)
  }
}