name := "thai-breakiterator"

organization := "com.twitter"

version := "1.0-SNAPSHOT"

publishMavenStyle := true

crossPaths := false

autoScalaLibrary := false

libraryDependencies ++= Seq(
  "com.novocode" % "junit-interface" % "0.10" % "test",
  "com.ibm.icu" % "icu4j" % "52.1" % "test"
)
