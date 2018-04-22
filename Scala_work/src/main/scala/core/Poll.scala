package core
import java.util.Date
import scala.util.{Random}


case  class Poll(name : String,anon : Boolean = true,visible : Boolean = true,
           firstTime : Option[Date] = None,secondTime : Option[Date] = None,
           id:Int = 10101 + Random.nextInt(100)+(Random.nextInt(100)+Random.nextInt(100)*Random.nextInt(10)),
           launch :Boolean = false,
           used:Boolean = false)
