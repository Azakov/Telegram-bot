package core
import org.scalatest.{FlatSpec, Matchers}

class TestParserCommand extends FlatSpec with Matchers {

  def assertParseArg(string:String,cmd:CommandsBot): Unit ={
    assert(ParserCommand.applyParse(string).right.get  ==  cmd)
  }
  def assertParseArg(pairs: (String,CommandsBot)*): Unit ={
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

  it should "begin" in {
    assertParseArg("/begin (55)",CommandBegin(55))
  }

  it should "end" in {
    assertParseArg("/end (55)", CommandEnd(55))
  }

  it should "view" in {
    assertParseArg("/view", CommandView())
  }

  it should "delete question" in {
    assertParseArg("/delete_question (55)",CommandDeleteQuestion(55))
  }

  it should "answer" in {
    assertParseArg("/answer (1) (no)",CommandAnswer(1,"no"))
  }

  it should "add question" in {
    assertParseArg(
      ("/add_question (Как дела?) (open) good bad", CommandAddQuestion("Как дела?",QuestionType.open,"good bad")),

      ("/add_question (Как дела?) (choice) good bad", CommandAddQuestion("Как дела?",QuestionType.choice,"good bad")),

      ("/add_question (Как дела?) (multi) good bad", CommandAddQuestion("Как дела?",QuestionType.multi,"good bad"))
    )
  }

}
