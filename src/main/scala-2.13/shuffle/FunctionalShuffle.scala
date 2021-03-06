package shuffle

import scalaz.{Monad, State}
import scala.util.Random

object FunctionalShuffle {
  type Rand[A] = State[Random, A]

  val Empty = LazyList()

  /**
    * Shuffles a LazyList uniformly with one pass.
    * Usage:
    *   `shuffle(LazyList(1,2,3,4,5)).eval(new scala.util.Random(System.nanoTime)).toList`
    *   `List[Int] = List(3, 5, 1, 4, 2)`
    *
    *   `shuffle("Scala!".toLazyList).eval(new scala.util.Random(System.nanoTime)).mkString`
    *   `String = aSa!lc`
    */
  def shuffle[A](s: LazyList[A]): Rand[LazyList[A]] = shuffleLength(s).map(_._1)

  private[shuffle] def shuffleLength[A](s: LazyList[A]): Rand[(LazyList[A], Int)] = s match {
    case Empty       => Monad[Rand].point((Empty,     0))
    case x #:: Empty => Monad[Rand].point((LazyList(x), 1))
    case _ #:: _     => halve(s) match { case (lHalf, rHalf) => for {
      left           <- shuffleLength(lHalf)
      right          <- shuffleLength(rHalf)
      (lSize, rSize) =  (left._2, right._2)
      shuffled       <- riffle(left, right)
    } yield (shuffled, lSize + rSize) }
  }

  private[shuffle] def riffle[A](l: (LazyList[A], Int), r: (LazyList[A], Int)): Rand[LazyList[A]] = (l, r) match {
    case ((xs,       _ ), (Empty,    _ )) => Monad[Rand].point(xs)
    case ((Empty,    _ ), (ys,       _ )) => Monad[Rand].point(ys)
    case ((x #:: xs, nx), (y #:: ys, ny)) => rng(below = nx+ny).flatMap { k =>
      if (k < nx) riffle((xs,       nx-1), (y #:: ys, ny))   map { x #:: _ }
      else        riffle((x #:: xs, nx),   (ys,       ny-1)) map { y #:: _ }
    }
  }

  private[shuffle] def halve[A](s: LazyList[A]): (LazyList[A], LazyList[A]) = s match {
    case Empty    => (Empty, Empty)
    case z #:: zs => halve(zs) match { case (xs, ys) => (z #:: ys, xs) }
  }

  /**
    * Generates an Int in the range [0, below)
    * Usage:
    *   `rng(3).eval(new scala.util.Random(System.nanoTime()))`
    *   `scalaz.Id.Id[Int] = 1`
    *
    *   `List.fill(5)(rng(3)).sequence.eval(new scala.util.Random(System.nanoTime()))`
    *   `scalaz.Id.Id[List[Int]] = List(0, 2, 1, 1, 0)`
    */
  def rng(below: Int): Rand[Int] = for {
    r <- State.get[Random]
    i =  if (below <= 0) 1 else below // guards against bad input
    b =  r.nextInt(i) // throws when i <= zero
  } yield b
}