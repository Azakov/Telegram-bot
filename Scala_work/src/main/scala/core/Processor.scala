package core

import scala.util.{Failure, Success, Try}
import info.mukel.telegrambot4s.models.User

object Processor {

 val currentContext = new VoteContext

  def process(cmd: CommandsBot, rep: PollRepoInMemory, user: User): Either[String, String] = {
    cmd match {
      case cmd: CommandCreatePoll => cmdCreatePoll(cmd, rep,user)
      case cmd: CommandList => cmdList(cmd, rep)
      case cmd: CommandStartPoll => cmdStartPoll(cmd, rep)
      case cmd: CommandResult => cmdResult(cmd, rep,user)
      case cmd: CommandDeletePoll => cmdDeletePoll(cmd, rep)
      case cmd: CommandStopPoll => cmdStopPoll(cmd, rep)
      case cmd: CommandBegin => cmdBegin(cmd, rep, user)
      case cmd: CommandEnd => cmdEnd(cmd, rep, user)
      case cmd: CommandView => cmdView(cmd,rep,user)
      case cmd: CommandDeleteQuestion => cmdDeleteQuestion(cmd,rep,user)
      case cmd: CommandAddQuestion => cmdAddQuestion(cmd,rep,user)
      case cmd: CommandAnswer => cmdAnswer(cmd,rep,user)
    }

  }

  def cmdCreatePoll(cmd: CommandCreatePoll, PR: PollRepoInMemory,user: User): Either[String, String] = {
      val poll = Poll(cmd.name, cmd.anonymous, cmd.visible, cmd.startTime, cmd.stopTime,user)
    PR.store(poll)
    Try(PR.store(poll)) match {
      case Success(_)=> Right(Response.pollCreate(poll.id))
      case Failure(_) => Left(ErrorConstants.failCreate)
    }
   }

  def cmdStartPoll(cmd: CommandStartPoll, PR: PollRepoInMemory): Either[String, String] = {
    if (PR.isContains(cmd.id)){
      if (!PR.hasStart(cmd.id)){
        if (!PR.isLaunch(cmd.id) && !PR.isUsed(cmd.id)){
          println(PR.isLaunch(cmd.id) )
          PR.start(cmd.id)
          Right(Response.startOk)
        }else Left(ErrorConstants.errorLaunchOrUsed)
      }else Left(ErrorConstants.errorTimeStart)
    }else Left(ErrorConstants.errorIdExist)
  }

  def cmdStopPoll(cmd: CommandStopPoll, PR: PollRepoInMemory): Either[String, String] = {
    PR.isContains(cmd.id) match {
      case false => Left(ErrorConstants.errorIdExist)
      case true => PR.hasEnd(cmd.id) match {
        case true => Left(ErrorConstants.errorTimeEnd)
        case false =>
          if (PR.isLaunch(cmd.id) || !PR.isUsed(cmd.id)){
            PR.end(cmd.id)
            Right(Response.endOk)
          }else Left(ErrorConstants.errorLaunchOrUsed2)
      }
    }
  }

  def cmdResult(cmd: CommandResult, PR: PollRepoInMemory, user: User): Either[String, String] = {
    PR.get(cmd.id).map(poll =>{
      if (PR.checkTimeStart(poll.id)){
        if (!poll.visible || (poll.visible && poll.used)){
          val listResult = poll.questions.values.
            map(q =>s" Id :${q.idQuest} " +
              s"${if (!poll.anon)q.voted.map(u => s"User: ${u._1.firstName} Answer: ${u._2.toString}" ).mkString(" ")
              else q.voted.map(u => s"User: Anonymous Answer: ${u._2.toString}" ).mkString(" ")}").mkString("\n")
          Right(Response.resultOK(listResult))
        } else  Left (ErrorConstants.sorryVisible)
      }else Left(ErrorConstants.errorEarly)
    }).getOrElse(Left(ErrorConstants.errorIdExist))
  }

  def cmdDeletePoll(cmd: CommandDeletePoll, PR: PollRepoInMemory): Either[String, String] = {
    PR.isContains(cmd.id) match {
      case false => Left(ErrorConstants.errorIdExist)
      case true =>
        PR.isLaunch(cmd.id) match {
          case true => Left(ErrorConstants.errorLaunch)
          case false => {
            PR.delete(cmd.id)
            Right(Response.deleteOk)
          }
        }
    }
  }

  def cmdList(cmd: CommandList, PR: PollRepoInMemory): Either[String, String] = {
    PR.polls.isEmpty match {
      case true => Left(ErrorConstants.sorryEmpty)
      case false => {
        Right(Response.cmdList(PR))


      }
    }
  }

  def cmdBegin(cmd: CommandBegin,PR: PollRepoInMemory,user: User):Either[String,String] = {
    currentContext.checkOccupy(user,cmd.id) match {
      case true => Left(ErrorConstants.sorryContextAlready)
        case false => {
          currentContext.occupy(user,cmd.id)
          Right(Response.beginOk)
        }
    }
  }

  def cmdEnd(cmd:CommandEnd,PR: PollRepoInMemory,user: User):Either[String,String] = {
    currentContext.checkOccupy(user,cmd.id) match {
      case false => Left(ErrorConstants.sorryContextNotBegin)
      case true => {
        currentContext.release(user,cmd.id)
        Right(Response.endContextOk)
      }
    }
  }

  def cmdView(cmd:CommandView,PR: PollRepoInMemory,user: User):Either[String,String] = {
    currentContext.getPollId(user).map(pollId => {
      PR.get(pollId).map(poll => {
            Right(Response.viewOk(poll,user))
      }).getOrElse(Left(ErrorConstants.errorIdExist))
    }).getOrElse(Left(ErrorConstants.sorryContextNotBegin))


  }

  def cmdAddQuestion (cmd:CommandAddQuestion,PR:PollRepoInMemory, user: User):Either[String,String] = {
    currentContext.getPollId(user).map(pollId => {
      PR.get(pollId).map(poll =>{
        if (poll.admin == user){
          if (!PR.checkTimeStart(pollId)){
            val quest  = Question(user,pollId,cmd.question,cmd.answers,cmd.typeQuest,poll.questions.size+1)
            PR.store(poll.store(quest))
            Right(Response.addOk(quest))
          }else  Left(ErrorConstants.sorryStarted)
        } else  Left(ErrorConstants.youNotAdmin)
        }).getOrElse(Left(ErrorConstants.errorIdExist))
    }).getOrElse(Left(ErrorConstants.sorryContextNotBegin))

  }

  def cmdDeleteQuestion (cmd:CommandDeleteQuestion,PR:PollRepoInMemory, user: User):Either[String,String] = {

    currentContext.getPollId(user).map(pollid => {
      PR.get(pollid).map(poll =>{
        if (poll.admin == user){
          if (!PR.checkTimeStart(pollid)){
            poll.delete(cmd.id)
            Right(Response.deleteQuestOk)
          }else  Left(ErrorConstants.sorryStarted)
        } else  Left(ErrorConstants.youNotAdmin)
      }).getOrElse(Left(ErrorConstants.errorIdExist))
    }).getOrElse(Left(ErrorConstants.sorryContextNotBegin))
  }

  def cmdAnswer(cmd:CommandAnswer, PR:PollRepoInMemory, user: User):Either[String,String] = {
    currentContext.getPollId(user).map(pollid => {
      PR.get(pollid).map(poll => {
        if (poll.launch){
          if(!poll.used){
            if(poll.questions.contains(cmd.id)) {
              if (!poll.questions(cmd.id).voted.keys.exists(u => user ==u)){
                val ans = Answers(cmd.id, cmd.answer, user, poll.anon,poll)
                println(poll)
                poll.getTypeQuestion(cmd.id) match {
                  case QuestionType.open => {
                    PR.store(ans.openAnswer())
                    println(PR.polls)
                    Right(Response.answerOk())
                  }
                  case QuestionType.choice => {
                    PR.store(ans.choiceAnswer())
                    Right(Response.answerOk())
                  }
                  case QuestionType.multi => {
                    PR.store(ans.multiAnswer())
                    Right(Response.answerOk())
                  }
                }
              } else Left(ErrorConstants.errorVote)
            } else Left(ErrorConstants.errorQuestion)
          }else Left(ErrorConstants.sorryUsed)
        } else  Left(ErrorConstants.errorEarly)
      }).getOrElse(Left(ErrorConstants.errorIdExist))
    }).getOrElse(Left(ErrorConstants.sorryContextNotBegin))
  }
}



