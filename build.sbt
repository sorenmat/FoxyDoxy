import sbt._
import Process._
import Keys._

name := "foxydoxy"

version := "0.1"

scalaVersion := "2.10.1"

libraryDependencies += "qdox" % "qdox" % "1.6.1"

libraryDependencies += "org.webjars" % "mustachejs" % "0.7.0"

libraryDependencies += "org.antlr" % "antlr" % "3.5"

libraryDependencies += "org.pegdown" % "pegdown" % "1.2.1"

libraryDependencies += "com.google.code.gson" % "gson" % "2.2.2"

libraryDependencies += "org.sellmerfud" % "optparse_2.10" % "2.0"
