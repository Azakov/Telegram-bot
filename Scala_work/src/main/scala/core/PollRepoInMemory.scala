package core

import java.util.{Calendar, Date}

import info.mukel.telegrambot4s.models.User

class PollRepoInMemory extends PollRepo {
  var polls: Map[Int, Poll] = Map.empty



  override def store(poll: Poll): Unit = polls += (poll.id -> poll)

  override def get(id: Int): Option[Poll] = polls.get(id)

  override def all(): Seq[Poll] = polls.values.toList

  override def delete(id: Int): Unit = polls -= id

  override def isUsed(id: Int): Boolean = polls(id).used

  override def isLaunch(id: Int): Boolean = polls(id).launch

  override def isContains(id: Int): Boolean = polls.contains(id)

  override def hasStart(id: Int): Boolean = polls(id).firstTime.nonEmpty

  override def start(ids:Int): Unit = {
    val old = polls(ids)
    val poll = old.copy(launch = true)
    store(poll)
 }

   def checkTimeStart(id:Int): Boolean ={
    val timeNow = Calendar.getInstance().getTime
    if (hasStart(id)){
      if (!polls(id).firstTime.get.before(timeNow))
        start(id)
    }
     polls.get(id).get.used || polls.get(id).get.launch
  }

   def checkTimeEnd(id:Int): Boolean ={
    val timeNow = Calendar.getInstance().getTime
    if (hasEnd(id)){
      if (polls(id).secondTime.get.before(timeNow))
        end(id)
    }
     polls.get(id).get.used || polls.get(id).get.launch
  }


  override def hasEnd(id: Int): Boolean = polls(id).secondTime.nonEmpty

  override def end(ids: Int): Unit = {
    val old = polls(ids)
    val poll = old.copy(launch = false,used = true)
    store(poll)
  }
}