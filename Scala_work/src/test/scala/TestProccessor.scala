package core

import info.mukel.telegrambot4s.models.User
import org.scalatest.{FlatSpec, Matchers}

class TestProccessor  extends FlatSpec with Matchers {
  val user = User(55,false,"Vote_bot")

  "createPollSuccess" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",admin = user)
    val cmd =   CommandCreatePoll(poll.name,
      poll.anon,poll.visible,poll.firstTime,
      poll.secondTime)
    val testId =  Processor.cmdCreatePoll(cmd,pollRepo,user)
    assert(testId.right.get.toInt > 10101 )
  }

  "createPollFail" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",admin = user)
    val cmd = CommandCreatePoll(poll.name,
      poll.anon,poll.visible,poll.firstTime,poll.secondTime)
    val testId2 = Processor.cmdCreatePoll(cmd,pollRepo,user)
    assert(testId2.right.get != "CREATE FAIL")
  }

  "startPollSuccess" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",admin = user,id = 10)
    pollRepo.store(poll)
    val cmd = CommandStartPoll(10)
    val testStart = Processor.cmdStartPoll(cmd,pollRepo)
    assert(testStart.right.get == "START OK!")
  }
  "startPollFail" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",admin = user,id = 10)
    val cmd = CommandStartPoll(10)
    val testStart = Processor.cmdStartPoll(cmd,pollRepo)
    assert(testStart.left.get == "ERROR BY ID - NOT EXIST!")
  }

  "startPollFail 2" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",firstTime = ParserCommand.toDate("12:28:30,18:02:03"),admin = user,id = 10)
    pollRepo.store(poll)
    val cmd = CommandStartPoll(10)
    val testStart = Processor.cmdStartPoll(cmd,pollRepo)
    assert(testStart.left.get == "ERROR BY TIME - NOT START!")
  }

  "stopPollSuccess" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",admin = user,id = 10)
    pollRepo.store(poll)
    val cmd = CommandStopPoll(10)
    val testStop = Processor.cmdStopPoll(cmd,pollRepo)
    assert(testStop.right.get == "END OK!")
  }
  "stopPollFail" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",admin = user,id = 10)
    val cmd = CommandStopPoll(10)
    val testStop = Processor.cmdStopPoll(cmd,pollRepo)
    assert(testStop.left.get == "ERROR BY ID - NOT EXIST!")
  }

  "stopPollFail 2" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",secondTime = ParserCommand.toDate("12:28:30,18:02:03"),admin = user,id = 10)
    pollRepo.store(poll)
    val cmd = CommandStopPoll(10)
    val testStop = Processor.cmdStopPoll(cmd,pollRepo)
    assert(testStop.left.get == "ERROR BY TIME - NOT END!")
  }

  "deleteSuccess" should "process" in {
      val pollRepo = new PollRepoInMemory
      val poll = Poll("test1",admin = user,id = 10)
      pollRepo.store(poll)
      val cmd = CommandDeletePoll(10)
      val testDelete = Processor.cmdDeletePoll(cmd,pollRepo)
      assert(testDelete.right.get == "DELETE OK!")
    }

  "deleteFail" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",admin = user,id = 10)
    val cmd = CommandDeletePoll(10)
    val testDelete = Processor.cmdDeletePoll(cmd,pollRepo)
    assert(testDelete.left.get == "ERROR BY ID - NOT EXIST!")
  }

  "deleteFail 2" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",launch = true,admin = user,id = 10)
    pollRepo.store(poll)
    val cmd = CommandDeletePoll(10)
    val testDelete = Processor.cmdDeletePoll(cmd,pollRepo)
    assert(testDelete.left.get == "ERROR BY LAUNCH - NOT DELETE!")
  }


  "listSuccess" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",admin = user,id = 10)
    pollRepo.store(poll)
    val cmd = CommandList()
    val testList = Processor.cmdList(cmd,pollRepo)
    assert(testList.right.get == "test1 имеет id = 10" )
  }
  "listFail" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",admin = user,id = 10)
    val cmd = CommandList()
    val testList = Processor.cmdList(cmd,pollRepo)
    assert(testList.left.get == "SORRY, LIST IS EMPTY" )
  }
  private val currentContext = new VoteContext

  "beginSuccess" should "proccess" in {
    val pollRepo = new PollRepoInMemory
    val cmd = CommandBegin(55)
    val testBegin = Processor.cmdBegin(cmd,pollRepo,user)
    assert(testBegin.right.get == "SUCCESS BEGIN")
  }

  "beginFail" should "proccess" in {
    val pollRepo = new PollRepoInMemory
    val cmd = CommandBegin(55)
    val testBegin = Processor.cmdBegin(cmd,pollRepo,user)
    assert(testBegin.left.get == "SORRY, CONTEXT ALREADY BEGIN!")
  }

  "endSuccess" should "proccess" in {
    val pollRepo = new PollRepoInMemory
    val cmdB = CommandBegin(55)
    Processor.cmdBegin(cmdB,pollRepo,user)
    val cmd = CommandEnd(55)
    val testBegin = Processor.cmdEnd(cmd,pollRepo,user)
    assert(testBegin.right.get == "SUCCESS END")
  }

  "endFail" should "proccess" in {
    val pollRepo = new PollRepoInMemory
    val cmd = CommandEnd(55)
    val testBegin = Processor.cmdEnd(cmd,pollRepo,user)
    assert(testBegin.left.get == "SORRY, CONTEXT NOT BEGIN")
  }

  "viewSuccess" should "proccess" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",admin = user,id = 55)
    val pollCmd = CommandCreatePoll(poll.name,
      poll.anon,poll.visible,poll.firstTime,poll.secondTime)
    val pollC = Processor.cmdCreatePoll(pollCmd,pollRepo,user)
    val cmdB = CommandBegin(55)
    Processor.cmdBegin(cmdB,pollRepo,user)
    val cmdE = CommandEnd(55)
    Processor.cmdEnd(cmdE,pollRepo,user)
    val cmd = CommandView()
    val testView = Processor.cmdView(cmd,pollRepo,user)
    assert(testView.right.get == "user name - id poll \n type question \n question \n answers")
  }

  "viewFail" should "proccess" in {
    val pollRepo = new PollRepoInMemory
    val cmd = CommandView()
    val testBegin = Processor.cmdView(cmd,pollRepo,user)
    assert(testBegin.left.get == "SORRY, CONTEXT NOT BEGIN")
  }

  "addQuestionSuccess" should "proccess" in {
    val pollRepo = new PollRepoInMemory
    val cmdB = CommandBegin(55)
    assert(Processor.cmdBegin(cmdB,pollRepo,user).right.get == "SUCCESS BEGIN")
    val cmd = CommandAddQuestion("hello",QuestionType.open)
    val testAdd = Processor.cmdAddQuestion(cmd,pollRepo,user)
    assert(Processor.currentContext.checkContext(user,pollRepo) == "0")
  }

}
