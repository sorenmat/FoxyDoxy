import sbt._
import Process._
import Keys._
import AssemblyKeys._ // put this at the top of the file

name := "foxydoxy"

version := "0.1"

scalaVersion := "2.10.1"

libraryDependencies += "org.pegdown" % "pegdown" % "1.2.1"

libraryDependencies += "com.google.code.gson" % "gson" % "2.2.2"

libraryDependencies += "org.sellmerfud" % "optparse_2.10" % "2.0"

assemblySettings
