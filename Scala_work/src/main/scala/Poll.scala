import java.text.SimpleDateFormat
import java.util.Date

import com.sun.net.httpserver.Authenticator.Failure

import scala.util.{Random, Success, Try}

class Poll(lines : Seq[String])  {
  private val random = new Random()
  val name = lines(1)
  var launch = false
  val id = name.length  * (random.nextInt(100) + random.nextInt(100)*random.nextInt(10))
  //val id = 55
  var used = false
  var createTrue = true


  val anonymous: String = {
    lines.lift(2).getOrElse("yes")  match {
      case "yes" => "yes"
      case "no" => "no"
      case _ =>{ createTrue = false
        " ERROR BY ANONYMOUS "}
    }

  }

  val visibility: String = {
    lines.lift(3).getOrElse("afterstop") match {
      case "afterstop" => "afterstop"
      case "continuous" => "continuous"
      case _ => {
        createTrue = false
        " ERROR BY VISIBILITY "}
    }
  }

  val timesUp: Try[Option[Date]]  = {
    val time = lines.lift(4)
    checkData(time)
  }


  val timesDown: Try[Option[Date]] =  {
    val time = lines.lift(5)
    checkData(time)
  }

  def checkData(time: Option[String]): Try[Option[Date]]={
    if (time.isEmpty) Success(None)
    else{
       Try(
        Some(new SimpleDateFormat("HH:mm:ss,yy:MM:dd").parse(time.get))
      )
       match {
        case Success(matched) => Success(matched)
        case _ => {
          createTrue = false
          Success(Some(new Date()))
        }
      }
    }
  }
}