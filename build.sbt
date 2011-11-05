name := "default"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.8.1"

organization := "example.com"

libraryDependencies += "org.scala-lang" % "scala-swing" % "2.8.1"

libraryDependencies += "org.scalatest" % "scalatest_2.8.1" % "1.5.1"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases"

libraryDependencies += "se.scalablesolutions.akka" % "akka-actor" % "1.2"

libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.1.2"

libraryDependencies += "org.twitter4j" % "twitter4j-core" % "2.2.5"

libraryDependencies += "org.twitter4j" % "twitter4j-stream" % "2.2.5"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-Xmigration")