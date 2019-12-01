lazy val scala213 = "2.13.1"
lazy val scala212 = "2.12.10"
lazy val supportedScalaVersions = List(scala213, scala212)

ThisBuild / organization := "com.nathanielmay"
ThisBuild / version      := "2.0.0"
ThisBuild / scalaVersion := scala213

lazy val root = (project in file("."))
  .aggregate(shuffle)
  .settings(
    crossScalaVersions := Nil, // must be set to Nil on the aggregating project
    publish / skip := true
  )

lazy val shuffle = (project in file("."))
  .settings(
    crossScalaVersions := supportedScalaVersions,

    libraryDependencies += "org.scalaz"     %% "scalaz-core" % "7.2.27",
    libraryDependencies += "org.scalatest"  %% "scalatest"   % "3.0.8"  % "test",
    libraryDependencies += "org.scalacheck" %% "scalacheck"  % "1.14.0" % "test",

    libraryDependencies += "org.scoverage" %% "scalac-scoverage-plugin" % "1.4.0" % "provided"
  )