import scala.util.Try

for {
  a <- List(1,2)
  b <- List (8,3)
 } yield (a,b)


for {
  a<-Try(1/0)
  b<-Try(2)
}yield (a,b)

for {
  a <- Some(4)
  b <- None

}yield (a,b)

