# functional-shuffle

This package implements Heinrich Apfelmus’s “merge shuffle” algorithm from his blog post [Random Permutations and Sorting](https://apfelmus.nfshost.com/articles/random-permutations.html). The implementation is based on [Antal Spector-Zabusky's Haskell Package](https://github.com/antalsz/functional-shuffle).

This version produces a uniform shuffle of the stream with a single traversal.

Usage:
```
shuffle(Stream(1,2,3,4,5)).eval(new scala.util.Random(System.nanoTime)).toList
// List(3, 5, 1, 4, 2)

shuffle("Scala!".toStream).eval(new scala.util.Random(System.nanoTime)).mkString
// "aSa!lc"
```