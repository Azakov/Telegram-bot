package core

object Request {
  def request: String = {
    val filename = "C:\\Users\\Go-go\\Desktop\\bot telega\\Scala_work\\src\\main\\scala\\Rubbish\\in.txt"
    val result = scala.io.Source.fromFile(filename).mkString.trim
    result.toString
  }
}
