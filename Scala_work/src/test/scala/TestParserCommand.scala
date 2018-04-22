package core
import org.scalatest.{FlatSpec, Matchers}

class TestParserCommand extends FlatSpec with Matchers {

  def assertParseArg(string:String,cmd:Commands): Unit ={
    assert(ParserCommand.applyParse(string).right.get  ==  cmd)
  }
  def assertParseArg(pairs: (String,Commands)*): Unit ={
    for (i <- pairs) assertParseArg(i._1,i._2)
  }
  "parser" should "list" in{
    assertParseArg("/list",CommandList())
  }
  it should "startPoll"in {
    assertParseArg("/start_poll (55)", CommandStartPoll(55))
  }
  it should "result" in {
    assertParseArg("/result (55)",CommandResult(55))
  }
  it should "delete" in {
    assertParseArg("/delete_poll (55)",CommandDeletePoll(55))
  }
  it should "stop" in {
    assertParseArg("/stop_poll (55)", CommandStopPoll(55))
  }
  val start = "12:28:30,18:02:03"
  val finish = "12:28:30,18:04:03"

  it should "create" in {
      assertParseArg(
        ("/create_poll (Hello world!)", CommandCreatePoll("Hello world!")),
        ("/create_poll (Hello world!) (yes)", CommandCreatePoll("Hello world!")),

        ("/create_poll (Hello world!) (yes) (afterstop)", CommandCreatePoll("Hello world!")),

        ("/create_poll (Hello world!) (no) (afterstop) (12:28:30,18:02:03) ",
          CommandCreatePoll("Hello world!",false,true,ParserCommand.toDate(start))),

        ("/create_poll (Hello world!) (no) (afterstop) (12:28:30,18:02:03) (12:28:30,18:04:03)",
          CommandCreatePoll("Hello world!",false,true, ParserCommand.toDate(start),ParserCommand.toDate(finish)))

      )
  }
}
