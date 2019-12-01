package other

import org.scalatest._


class ShuffleUnitTest extends FlatSpec with Matchers {

  "README usage examples" should "work outside the package scope" in {
    import scala.util.Random
    import shuffle.all._

    shuffle(LazyList(1,2,3,4,5))
      .eval(new Random(0L))
      .toList shouldBe List(5, 1, 4, 3, 2)
    // List(5, 1, 4, 3, 2)

    shuffle("Scala!".iterator.to(LazyList))
      .eval(new Random(0L))
      .mkString shouldBe "laSa!c"
    //"laSa!c"

    //impure example:
    shuffle("Scala!".iterator.to(LazyList))
      .eval(new Random(System.nanoTime))
      .mkString
    // different output every time
  }

  "README other functions examples" should "work outside the package scope" in {
    import scala.util.Random
    import scalaz._, Scalaz._ // for sequence
    import shuffle.all._

    List.tabulate(10)(rng).sequence.eval(new Random(0L)) shouldBe
      List(0, 0, 0, 2, 2, 3, 5, 1, 4, 2)
  }
}
