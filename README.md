# functional-shuffle
[![CircleCI](https://circleci.com/gh/nathaniel-may/functional-shuffle.svg?style=svg)](https://circleci.com/gh/nathaniel-may/functional-shuffle)
[![Release](https://jitpack.io/v/nathaniel-may/functional-shuffle.svg)](https://jitpack.io/#User/Repo)

This package implements Heinrich Apfelmus’s “merge shuffle” algorithm from his blog post [Random Permutations and Sorting](https://apfelmus.nfshost.com/articles/random-permutations.html). The implementation is based on [Antal Spector-Zabusky's Haskell Package](https://github.com/antalsz/functional-shuffle).

This version produces a uniform shuffle of a Scala lazy list with a single traversal.

### Getting Started
In `build.sbt` add the jitpack resolver and library dependency:
```
resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies += "com.github.nathaniel-may" % "functional-shuffle" % "1.0.0"
```

### Usage:
```scala
import scala.util.Random
import shuffle.all._

shuffle(LazyList(1,2,3,4,5))
  .eval(new Random(0L))
  .toList
// List(5, 1, 4, 3, 2)

shuffle("Scala!".iterator.to(LazyList))
  .eval(new Random(0L))
  .mkString
// "laSa!c"

// impure example:
shuffle("Scala!".iterator.to(LazyList))
  .eval(new Random(System.nanoTime))
  .mkString
// output depends on when the function is called
```

### Other Functions
the `shuffle` package also provides a simple rng function:
```scala
import scala.util.Random
import scalaz._, Scalaz._ // for sequence
import shuffle.all._

List.tabulate(10)(rng).sequence.eval(new Random(0L))
// List(0, 0, 0, 2, 2, 3, 5, 1, 4, 2)
```