package com.scalaprog.foxydoxy

import java.io.File
import org.pegdown.PegDownProcessor
import scala.io.Source
import java.util.UUID

/**
 * User: soren
 */

object SourceCodeParser {


  def removeStarsFromText(strings: Array[String]) = {
    strings.
      map(line => if (line.contains("/**")) line.substring(line.indexOf("/**") + 2, line.length) else line).
      map(line => if (line.contains("*/")) line.substring(0, line.lastIndexOf("*/")) else line).
      map(line => if (line.contains("*")) line.substring(line.indexOf("*") + 1, line.length) else line)
  }

  def parseToSections(baseDir: File, file: String) = {

    val sectionRegExp = """(@section )(.*)""".r
    val tagsRegExp = """(@tags )(.*)""".r
    val priorityRegExp = """(@priority )(.*)""".r

    val source = Source.fromFile(file).mkString
    val reg = "/\\*(.|[\\r\\n])*?\\*/".r
    val docs = reg.findAllMatchIn(source).filter(s => s.toString().contains("@documentation")) map (s => {
      val text = s.toString()
      val section = sectionRegExp.findFirstIn(text).getOrElse("").replaceAll("@section", "")
      val tagText = tagsRegExp.findFirstIn(text).getOrElse("")
      val tags = tagText.replaceAll("@tags", "").split(",").toList

      val textWithoutTags = text.split("\n").filterNot(line => line.contains("@documentation") || line.contains("@tags") || line.contains("@section"))
      val cleanText = removeStarsFromText(textWithoutTags).mkString("\n").trim
      val html = new PegDownProcessor().markdownToHtml(cleanText)
      val fileName = file.replaceAll(baseDir.getCanonicalPath, "") // make directory relative.
      Section(UUID.randomUUID().toString, fileName, section, tags, html)
    })
    docs.toList
  }
}