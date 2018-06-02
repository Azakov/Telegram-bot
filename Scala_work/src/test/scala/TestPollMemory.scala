package core
import info.mukel.telegrambot4s.models.User
import org.scalatest.{FlatSpec, Matchers}

class TestPollMemory  extends FlatSpec with Matchers {
  val user = User(55,false,"Vote_bot")
  val pollTest = Poll("test",admin = user,id = 0)


  "store and get" should "pollRepo" in {
    val pollRepo = new PollRepoInMemory
    pollRepo.store(pollTest)
    assert(pollRepo.get(0).get.name == pollTest.name)
  }

  "all" should "pollRepo" in {
    val pollRepo = new PollRepoInMemory
    pollRepo.store(pollTest)
    pollRepo.store(Poll("test1",admin = user,id = 10))
    assert(pollRepo.all() == List(pollTest,Poll("test1",admin = user, id = 10)) )
  }

  "delete" should "pollRepo" in {
    val pollRepo = new PollRepoInMemory
    pollRepo.store(pollTest)
    pollRepo.delete(pollTest.id)
    assert(pollRepo.polls.isEmpty)
  }
  "launch" should "pollRepo" in {
    val pollRepo = new PollRepoInMemory
    pollRepo.store(Poll("test1",launch = true,admin = user,id = 10))
    assert(pollRepo.isLaunch(10))
  }

  "contains" should "pollRepo" in {
    val pollRepo = new PollRepoInMemory
    pollRepo.store(Poll("test1",admin = user, id = 10))
    assert(pollRepo.isContains(10))
  }

  "hasStart" should  "pollRepo" in {
    val pollRepo = new PollRepoInMemory
    pollRepo.store(Poll("test1",admin = user, id = 10))
    assert(!pollRepo.hasStart(10))
  }

  "hasEnd" should  "pollRepo" in {
    val pollRepo = new PollRepoInMemory
    pollRepo.store(Poll("test1",admin = user, id = 10))
    assert(!pollRepo.hasEnd(10))
  }

  "start" should "pollRepo" in {
    val pollRepo = new PollRepoInMemory
    pollRepo.store(Poll("test1",admin = user,id = 10))
    pollRepo.start(10)
    assert(pollRepo.get(10).get.launch && !pollRepo.get(10).get.used)

  }

  "end" should "pollRepo" in {
    val pollRepo = new PollRepoInMemory
    pollRepo.store(Poll("test1",admin = user,id = 10))
    pollRepo.end(10)
    assert(!pollRepo.get(10).get.launch && pollRepo.get(10).get.used)
  }

}
