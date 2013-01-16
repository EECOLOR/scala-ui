import sbt._
import Keys._

object LibraryBuild extends Build {

  val appName = "uiFramework"
  val appVersion = "1.0"

  val appDependencies = Seq(
      "org.scala-lang" % "scala-actors" % "2.10.0",
      "org.specs2" % "specs2_2.10.0" % "1.12.1.1" % "test",
      "org.mockito" % "mockito-all" % "1.9.5" % "test")

  lazy val root = Project(id = appName,
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
        libraryDependencies ++= appDependencies,
        scalaVersion := "2.10.0",
        scalacOptions += "-feature",
        resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                    "releases"  at "http://oss.sonatype.org/content/repositories/releases")
     ))
}

