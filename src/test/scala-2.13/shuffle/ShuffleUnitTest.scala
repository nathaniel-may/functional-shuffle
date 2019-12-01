package shuffle

import scala.annotation.tailrec
import scala.math.abs
import scala.util.Random
import shuffle.all._
import org.scalatest._


class ShuffleUnitTest extends FlatSpec with Matchers {

  "A Shuffled LazyList" should "have a roughly even distribution" in {
    val size      = 3
    val tolerance = .05
    val samples   = scala.math.pow(2 * size / tolerance, 2).toInt
    val goal      = samples.toDouble/factorial(size)

    // randomly generated seeds. Using the same static seeds makes the test deterministic.
    val seeds = List(
      6481974435664621795L,
      4791688558456143830L,
      293875002857805281L,
      7733768347870080636L,
      6871359988984629593L,
      6629083326245445978L,
      1967586611012001563L,
      6860531089457037514L,
      1552289256667894481L,
      2553103043961892635L
    )

    val failedSeeds = seeds.view
      .map(maxValue(samples, size, tolerance, goal))
      .zip(seeds)
      .filter(x => notWithin(goal, tolerance)(x._1))
      .map(_._2)
      .toList

    withClue(s"all values must be within (${min(goal, tolerance)}, ${max(goal, tolerance)})\nThe following seeds failed: ${failedSeeds.mkString(", ")}") {
      failedSeeds shouldBe empty
    }
  }

  def maxValue(samples: Int, size: Int, tolerance: Double, goal: Double)(seed: Long): Int = {
    // prevents the stack overflow that comes with the State monad sequence & eval approach.
    // definitely a better solution out there than relying on external mutation.
    val mutR = new Random(seed)

    @tailrec
    def agg[A](input: LazyList[Rand[LazyList[A]]], r: Random, m: Map[List[A], Int]): Map[List[A], Int] = input match {
      case LazyList() => m

      case x #:: xs =>
        val k = x.eval(r).toList
        agg(xs, r, m.updated(k, m.getOrElse(k, 0) + 1))
    }

    val input =
      LazyList.tabulate(samples) { _ => (0 until size).iterator.to(LazyList) }
        .map(shuffle)

    agg[Int](input, mutR, Map()).values.max
  }

  def min(goal: Double, tol: Double): Double = goal - goal*abs(tol)
  def max(goal: Double, tol: Double): Double = goal + goal*abs(tol)

  def within(goal: Double, tol: Double)(value: Int): Boolean =
    if (value >= goal) max(goal, tol) > value
    else               min(goal, tol) < value

  def notWithin(goal: Double, tol: Double)(value: Int): Boolean = !within(goal, tol)(value)

  def factorial(n: Long): Long = (2L to n).product

}
