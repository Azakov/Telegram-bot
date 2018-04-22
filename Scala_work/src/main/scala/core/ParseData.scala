package core

object ParseData {
    def parseDate(text: String): Either[String,Commands] = {
      ParserCommand.applyParse(text)
    }
}