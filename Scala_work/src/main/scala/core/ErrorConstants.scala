package core

object ErrorConstants {
  val failCreate = "CREATE FAIL"
  val errorIdExist = "ERROR BY ID - NOT EXIST!"
  val errorTimeStart = "ERROR BY TIME - NOT START!"
  val errorTimeEnd = "ERROR BY TIME - NOT END!"
  val errorLaunch = "ERROR BY LAUNCH - NOT DELETE!"
  val sorryEmpty = "SORRY, LIST IS EMPTY"
  val sorryContextAlready = "SORRY, CONTEXT NOT EXIST OR ALREADY BEGIN"
  val sorryContextNotBegin ="SORRY, CONTEXT NOT BEGIN"
  val sorryStarted = "SORRY, BUT POLL WAS STARTED"
  val youNotAdmin = "YOU ARE NOT ADMIN!"
  val errorVote = "ALREADY VOTE!"
  val errorEarly = "POLL WAS NOT STARTED"
  val sorryVisible = "VISIBLE ONLY AFTER STOPPING POLL"
  val sorryUsed = "SORRY, BUT POLL IS USED"
  val errorQuestion = "ERROR BY ANSWER - QUESTION NOT EXIST"
  val errorLaunchOrUsed = "ERROR BY POLL - POLL IS USED OR POLL IS LAUNCHED"
  val errorLaunchOrUsed2 = "ERROR BY POLL - POLL IS USED OR POLL IS NOT LAUNCHED"
}
