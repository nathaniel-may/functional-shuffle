package shuffle

import shuffle.{FunctionalShuffle => fs}

package object all {
  type Rand[A] = fs.Rand[A]
  def shuffle[A](s: Stream[A]): Rand[Stream[A]] = fs.shuffle(s)
  def rng(below: Int): Rand[Int] = fs.rng(below)
}
