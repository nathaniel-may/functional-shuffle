package other

import org.scalatest._
import shuffle.all._
import scala.util.Random


class ShuffleUnitTest extends FlatSpec with Matchers {

  "Shuffle imports" should "be accessible to users" in {
    shuffle(LazyList(1,2,3,4,5))
      .eval(new Random(System.nanoTime))
      .toList
    // List(3, 5, 1, 4, 2)

    shuffle("Scala!".iterator.to(LazyList))
      .eval(new Random(System.nanoTime))
      .mkString
    // "aSa!lc"
  }
}
