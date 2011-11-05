/*************************************************
//P.088
*/

// command: fsc ChecksumAccumulator.scala apptrait.scala
// command: scala FallWinterSpringSummer


//import ChecksumAccumulator.calculate
import com.example.scalapkg.ChecksumAccumulator._

object FourSeason {
  def main(args: Array[String]) {
    for(season <- List("fall", "winter", "spring"))
      println(season + ": " + calculate(season))
  }
}
