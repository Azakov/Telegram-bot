import java.text.SimpleDateFormat
import scala.util.Random

class Poll(lines : Seq[String])  {
  private val random =  new Random()
  val name = lines(1)
  var launch = false
  val id = name.length  * (random.nextInt(100) + random.nextInt(100)*random.nextInt(10))
  val used = false

  val anonymous = {
    val str = lines.lift(2)
    str.getOrElse("yes")
  }

  val visibility  = {
    val str = lines.lift(3)
    str.getOrElse("afterstop")
  }

  val timesUp  = {
    val time = lines.lift(4)
    if (time.isEmpty) None
    else
      new SimpleDateFormat("HH:mm:ss,yy:MM:dd").parse(time.get)
  }

  val timesDown = {
    val time = lines.lift(5)
    if (time.isEmpty) None
    else
      new SimpleDateFormat("HH:mm:ss,yy:MM:dd").parse(time.get)
  }
}