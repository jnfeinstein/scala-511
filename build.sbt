name := "scala-511"

organization := "org.five11"

version := "0.0.6"

scalaVersion := "2.11.4"

assemblyJarName in assembly := "scala-511.jar"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % "1.0.3",
  "org.scalaj" %% "scalaj-http" % "1.1.4"
)
