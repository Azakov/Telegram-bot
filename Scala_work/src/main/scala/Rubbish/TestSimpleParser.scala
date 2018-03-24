import scala.util.parsing.combinator._

case class WordFreq(word: String) {
  override def toString = "Word <" + word + "> " +
    "occurs with frequency "

}

class SimplePaarser extends RegexParsers {
  def clear: Parser[String] = "[^(]+".r | "[^/]+".r ^^ {_.toString}
//  def some: Parser[String] = """[]+""".r
//  def word: Parser[String] =  "(" ~> """""" <~ ")" ^^ { _.toString }
  def bracket: Parser[String] = "(("|"))" ^^ {case "((" => "("
  case "))" => ")"}
  def word :Parser[String] = "(" ~> ".+".r <~ ")" ^^ (_.toString)
  def text: Parser[String] = ".*?".r ^^ {_.toString}
 // def m : Parser[String] = """((\(.+?\)?<" ")""".r
  def name: Parser[String] = "/[a-z]+(_[a-z]+)?".r ^^ { a => {println(a)
    a.toString}}
//  def number: Parser[Int]    = (0|[1-9]\d*).r ^^ { _.toInt }
def freq: Parser[String] =  word          ^^ {_.toString()}

  //def freq: Parser[String]  = name
}

object TestSimpleParser extends SimplePaarser {
  def main(args: Array[String]) = {
    var line  = "(how are you?)"
      parse(word,line) match {
        case Success(matched,_) => {
          println(matched)

        }
        case Failure(msg,_) => println("FAILURE: " + msg)
        case Error(msg,_) => println("ERROR: " + msg)
    }

    }
}