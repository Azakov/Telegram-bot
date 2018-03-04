object Main {
  def main(args: Array[String]) {
    val r = new PollRepoInMemory
    val p = new FileReader(r)
    p.read("C:\\Users\\Go-go\\Desktop\\Scala_work\\src\\main\\scala\\in.txt")
  }
}