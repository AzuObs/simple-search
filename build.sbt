name := "event-sourcer"

val versions = new {
  val app = "1.0.0"
  val scala = "2.12.6"
  val scalaTest = "3.0.5"
  val sbtAssembly = "0.14.6"
}

version := versions.app

scalaVersion := versions.scala

assemblyJarName in assembly := "prog.jar"
mainClass in assembly := Some("com.orderbook.Main")
