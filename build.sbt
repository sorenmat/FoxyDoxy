import sbt._
import Process._
import Keys._
import AssemblyKeys._ // put this at the top of the file

name := "foxydoxy"

version := "0.1"

scalaVersion := "2.10.1"

libraryDependencies += "qdox" % "qdox" % "1.6.1"

libraryDependencies += "org.webjars" % "mustachejs" % "0.7.0"

libraryDependencies += "org.antlr" % "antlr" % "3.5"

libraryDependencies += "org.pegdown" % "pegdown" % "1.2.1"

libraryDependencies += "com.google.code.gson" % "gson" % "2.2.2"

libraryDependencies += "org.sellmerfud" % "optparse_2.10" % "2.0"

libraryDependencies += "commons-io" % "commons-io" % "2.4"

assemblySettings
