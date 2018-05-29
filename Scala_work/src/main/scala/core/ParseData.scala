package core

object ParseData {
    def parseDate(text: String): Either[String,CommandsBot] = {
      ParserCommand.applyParse(text)
    }
}