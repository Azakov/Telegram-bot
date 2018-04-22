package core

class PollRepoInMemory extends PollRepo {
  var polls: Map[Int, Poll] = Map.empty

  override def store(poll: Poll): Unit = polls += (poll.id -> poll)

  override def get(id: Int): Option[Poll] = polls.get(id)

  override def all(): Seq[Poll] = polls.values.toList

  override def delete(id: Int): Unit = polls -= id

  override def isLaunch(id: Int): Boolean = polls(id).launch

  override def isContains(id: Int): Boolean = polls.contains(id)

  override def hasStart(id: Int): Boolean = polls(id).firstTime.nonEmpty

  override def start(ids:Int): Unit = {
    val old = polls(ids)
    val poll = old.copy(launch = true, used = true)
    store(poll)
 }

  override def hasEnd(id: Int): Boolean = polls(id).secondTime.nonEmpty

  override def end(ids: Int): Unit = {
    val old = polls(ids)
    val poll = old.copy(launch = false,used = true)
    store(poll)
  }
}