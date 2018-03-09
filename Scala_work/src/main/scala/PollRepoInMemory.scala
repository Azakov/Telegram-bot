class PollRepoInMemory extends PollRepo {
  var polls: Map[Int, Poll] = Map.empty

  override def store(poll: Poll): Unit = polls += (poll.id -> poll)

  override def get(id: Int): Option[Poll] = polls.get(id)

  override def all(): Seq[Poll] = polls.values.toList

  override def delete(id: Int): Unit = polls -= id

  override def isLaunch(id: Int): Boolean = polls.get(id).get.launch


  override def isContains(id: Int): Boolean = polls.contains(id)

  override def hasStart(id: Int): Boolean = polls.get(id).get.timesUp != None

  override def start(id: Int): Unit = polls.get(id).get.launch = true

  override def hasEnd(id: Int): Boolean = polls.get(id).get.timesDown != None

  override def end(id: Int): Unit = polls.get(id).get.launch = false

}