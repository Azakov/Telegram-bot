/*import com.sun.tools.javac.util.ListBuffer

import scala.util.parsing.combinator.RegexParsers

class testParser  extends  RegexParsers{
    var args = new ListBuffer[String]()
    def word :Parser[String] = """(\(+.*?\))""".r ^^ { arg => {
      this.args += arg
      arg
    } }

    def name: Parser[String] = "/[a-z]+(_[a-z]+)?".r ^^ { arg => {
      this.args += arg
      arg.toString
    } }

    def freq: Parser[String] =  name ~ word ~ word ~ word ~ word ~ word ^^ {_.toString()}


  def parser(text: String):ListBuffer[String] = {
      parse(freq,text)
      match {
        case Failure(msg,_) =>
          msg match {
            case "string matching regex `(\\(+.*?\\))' expected but end of source found" => args
            case _ => args = new ListBuffer[String]
          }
        case Error(msg,_) => println("ERROR: " + msg)
      }
      args
    }

}
*/