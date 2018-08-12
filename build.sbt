name := "simple-search"

val versions = new {
  val app = "1.0.0"
  val scala = "2.12.6"
  val sbtAssembly = "0.14.6"
  val scalaTest = "3.0.5"
}

version := versions.app

scalaVersion := versions.scala

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % versions.scalaTest % Test
)

assemblyJarName in assembly := "SimpleSearch.jar"
mainClass in assembly := Some("com.simplesearch.Main")
