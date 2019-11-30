package shuffle

import shuffle.{FunctionalShuffle => fs}

package object all {
  type Rand[A] = fs.Rand[A]
  def shuffle[A](s: LazyList[A]): Rand[LazyList[A]] = fs.shuffle(s)
  def rng(below: Int): Rand[Int] = fs.rng(below)
}
