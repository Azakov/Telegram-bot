package core

import org.scalatest.{FlatSpec, Matchers}

class TestProccessor  extends FlatSpec with Matchers {


  "createPollSuccess" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1")
    val cmd =   CommandCreatePoll(poll.name,
      poll.anon,poll.visible,poll.firstTime,
      poll.secondTime)
    val testId =  Processor.cmdCreatePoll(cmd,pollRepo)
    assert(testId.right.get.toInt > 10101 )
  }

  "createPollFail" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1")
    val cmd = CommandCreatePoll(poll.name,
      poll.anon,poll.visible,poll.firstTime,poll.secondTime)
    val testId2 = Processor.cmdCreatePoll(cmd,pollRepo)
    assert(testId2.right.get != "CREATE FAIL")
  }

  "startPollSuccess" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",id = 10)
    pollRepo.store(poll)
    val cmd = CommandStartPoll(10)
    val testStart = Processor.cmdStartPoll(cmd,pollRepo)
    assert(testStart.right.get == "START OK!")
  }
  "startPollFail" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",id = 10)
    val cmd = CommandStartPoll(10)
    val testStart = Processor.cmdStartPoll(cmd,pollRepo)
    assert(testStart.left.get == "ERROR BY ID - NOT EXIST!")
  }

  "startPollFail 2" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",firstTime = ParserCommand.toDate("12:28:30,18:02:03"),id = 10)
    pollRepo.store(poll)
    val cmd = CommandStartPoll(10)
    val testStart = Processor.cmdStartPoll(cmd,pollRepo)
    assert(testStart.left.get == "ERROR BY TIME - NOT START!")
  }

  "stopPollSuccess" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",id = 10)
    pollRepo.store(poll)
    val cmd = CommandStopPoll(10)
    val testStop = Processor.cmdStopPoll(cmd,pollRepo)
    assert(testStop.right.get == "END OK!")
  }
  "stopPollFail" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",id = 10)
    val cmd = CommandStopPoll(10)
    val testStop = Processor.cmdStopPoll(cmd,pollRepo)
    assert(testStop.left.get == "ERROR BY ID - NOT EXIST!")
  }

  "stopPollFail 2" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",secondTime = ParserCommand.toDate("12:28:30,18:02:03"),id = 10)
    pollRepo.store(poll)
    val cmd = CommandStopPoll(10)
    val testStop = Processor.cmdStopPoll(cmd,pollRepo)
    assert(testStop.left.get == "ERROR BY TIME - NOT END!")
  }

  "resultSuccess" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",id = 10)
    pollRepo.store(poll)
    val cmd = CommandResult(10)
    val testResult = Processor.cmdResult(cmd,pollRepo)
    assert(testResult.right.get == "RESULT OK: test1 NOT USED NOT STARTED")
  }

  "resultFail" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",id = 10)
    val cmd = CommandResult(10)
    val testResult = Processor.cmdResult(cmd,pollRepo)
    assert(testResult.left.get == "ERROR BY ID - NOT EXIST")
  }


  "deleteSuccess" should "process" in {
      val pollRepo = new PollRepoInMemory
      val poll = Poll("test1",id = 10)
      pollRepo.store(poll)
      val cmd = CommandDeletePoll(10)
      val testDelete = Processor.cmdDeletePoll(cmd,pollRepo)
      assert(testDelete.right.get == "DELETE OK!")
    }

  "deleteFail" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",id = 10)
    val cmd = CommandDeletePoll(10)
    val testDelete = Processor.cmdDeletePoll(cmd,pollRepo)
    assert(testDelete.left.get == "ERROR BY ID - NOT EXIST!")
  }

  "deleteFail 2" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",launch = true,id = 10)
    pollRepo.store(poll)
    val cmd = CommandDeletePoll(10)
    val testDelete = Processor.cmdDeletePoll(cmd,pollRepo)
    assert(testDelete.left.get == "ERROR BY LAUNCH - NOT DELETE!")
  }


  "listSuccess" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",id = 10)
    pollRepo.store(poll)
    val cmd = CommandList()
    val testList = Processor.cmdList(cmd,pollRepo)
    assert(testList.right.get == "test1 имеет id = 10" )
  }
  "listFail" should "process" in {
    val pollRepo = new PollRepoInMemory
    val poll = Poll("test1",id = 10)
    val cmd = CommandList()
    val testList = Processor.cmdList(cmd,pollRepo)
    assert(testList.left.get == "SORRY, LIST IS EMPTY" )
  }


}
