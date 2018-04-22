import scala.util.Try

import scala.util.parsing.combinator._
import scala.util.{Try, Success, Failure}

case class A() extends RegexParsers{
  def kekos(): String ={
    val a = "create poll "

    def optional[T](p: Parser[T]): Parser[Option[T]] =
      """$""".r ^^^ None | p ^^ (x => Some(x))

    def anonymous: Parser[Boolean] =
      "(" ~> "yes" <~ ")" ^^^ true |  "(" ~> "no" <~ ")"  ^^^ false

    def visibile: Parser[Boolean] =
    "afterstop" ^^^ true | "continuous" ^^^ false



    val string: Parser[String] = """(\(\(|\)\)|[^()])*""".r ^^
      (s => s.replaceAll("""\(\(""", "(").replaceAll("""\)\)""", ")"))

    def anon[T](p: Parser[T])= p ^^ {case Some(x) => println(x)+ "some"
    case Some(None) => None
    case msg => println(msg) + "fail"}
    def lol: Parser[String] =
      "create poll" ~  optional(anonymous) ~ optional(visibile) ^^ (_ toString())

    parse(lol,a) match {
      case Success(x,_) => x + "end"
      case NoSuccess(msg) => msg._1
    }
  }
}
val k = A()
val l = k.kekos()
println(l.toString)
val text = "hello world i`m krivedina"
val line = text.split(" ")
var ac = List()
val h =  line.map(i => List(i))
h(0)(0)


