class PollRepoInMemory extends PollRepo {
  var polls: Map[Int, Poll] = Map.empty

  override def store(poll: Poll): Unit = polls += (poll.id -> poll)

  override def get(id: Int): Option[Poll] = polls.get(id)

  override def all(): Seq[Poll] = polls.values.toList

  override def delete(id: Int): Unit = polls -= id

  override def isLaunch(id: Int): Boolean = polls(id).launch


  override def isContains(id: Int): Boolean = polls.contains(id)

  override def hasStart(id: Int): Boolean = polls(id).timesUp.getOrElse(None).nonEmpty

  override def start(id: Int): Unit = {
    polls(id).launch = true
    polls(id).used = true}

  override def hasEnd(id: Int): Boolean = polls(id).timesDown.getOrElse(None).nonEmpty

  override def end(id: Int): Unit = {
    polls(id).launch = false
    polls(id).used = true}

}