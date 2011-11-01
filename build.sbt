name := "default"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.8.1"

organization := "example.com"

libraryDependencies += "org.scalatest" % "scalatest_2.8.1" % "1.5.1"

libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.1.2"

libraryDependencies += "org.twitter4j" % "twitter4j-core" % "2.2.5"

libraryDependencies += "org.twitter4j" % "twitter4j-stream" % "2.2.5"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-Xmigration")