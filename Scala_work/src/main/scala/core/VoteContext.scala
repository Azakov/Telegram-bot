package core
import info.mukel.telegrambot4s.models.User

class VoteContext {

  private var currentVote: Map[User, Int] = Map.empty

  def occupy(user: User, id: Int): Unit = currentVote += (user -> id)

  def getPollId(user: User): Option[Int] = currentVote.get(user)

  def release(user: User, id: Int): Unit = currentVote -= user

  def checkOccupy(user: User, id: Int): Boolean = currentVote.exists(_ == (user, id))


  def checkContext(user: User, PR: PollRepoInMemory): Boolean = getPollId(user) match {
    case None => false
    case Some(a) => PR.get(a) match {
      case None => false
      case Some(a) => true
    }
  }
}


