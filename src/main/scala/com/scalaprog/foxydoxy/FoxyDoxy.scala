package com.scalaprog.foxydoxy

import com.google.gson.GsonBuilder
import com.scalaprog.foxydoxy.cli.CommandLineConfiguration
import java.io.{OutputStream, InputStream, FileOutputStream}
import org.sellmerfud.optparse.OptionParser

import java.io.File

/**
 * User: soren
 */
class FoxyDoxy {

  def parseSourceDirectory(baseDir: File, srcDir: String, foundSections: List[Section]): List[Section] = {
    val cwd = new File(srcDir)
    if (cwd.isDirectory) {
      val found = cwd.listFiles().map(file => {
        parseSourceDirectory(baseDir, file.getCanonicalPath, foundSections)
      }).flatten.toList
      return found ::: foundSections
    }
    else {
      val sourceFile = cwd.getCanonicalPath
      if (sourceFile.endsWith(".java") || sourceFile.endsWith(".scala")) {
        val f = SourceCodeParser.parseToSections(baseDir, sourceFile)
        if (!f.isEmpty)
          println("Added documentation for " + sourceFile)
        return f ::: foundSections

      } else foundSections
    }

  }
}

object FoxyDoxy {
  def main(args: Array[String]) {

    println("Foxy Doxy version 0.1")
    val cli: OptionParser[CommandLineConfiguration] = new OptionParser[CommandLineConfiguration] {
      reqd[String]("-s", "--source DIRECTORY", "Source directory to scan for documentation") {
        (value, cfg) => cfg.copy(sourceDirectory = value)
      }
      optl[String]("-t", "--template FILE", "Choose non default template") {
        (value, cfg) => cfg.copy(templateFile = value)
      }
      optl[String]("-o", "--output DIRECTORY", "Directory where output files will be placed") {
        (value, cfg) => cfg.copy(outputDirectory = value.getOrElse("doc"))
      }
    }

    val config = cli.parse(args.toList, CommandLineConfiguration())

    println("Searching for documentation in "+new File(config.sourceDirectory).getCanonicalPath)
    println("Writing documentation in "+new File(config.outputDirectory).getCanonicalPath)
    new File(config.outputDirectory).mkdirs() // create output directory
    val outputDir = config.outputDirectory + File.separator

    val gson = new GsonBuilder().setPrettyPrinting().create()
    val baseDirectory = new File(config.sourceDirectory)
    val data = Template(new FoxyDoxy().parseSourceDirectory(baseDirectory, config.sourceDirectory, Nil).toArray)

    config.templateFile match {
      case Some(x) =>
      case None => {
        var inputFile: InputStream = Thread.currentThread().getContextClassLoader.getResourceAsStream("template.html")
        if (inputFile == null)
          inputFile = this.getClass.getResourceAsStream("template.html")
        if (inputFile == null)
          inputFile = ClassLoader.getSystemResourceAsStream("template.html");
        if (inputFile == null)
          inputFile = this.getClass.getClassLoader.getResourceAsStream("template.html")
        if (inputFile == null) {
          throw new RuntimeException("unable to find template file.")
        }
        copyFile(inputFile, new FileOutputStream(outputDir + "template.html"))
      }
    }
    val templateFile = new FileOutputStream(config.templateFile.getOrElse(outputDir + "template.json"))
    writeToFile(templateFile, gson.toJson(data))

  }

  def closeAfterUse[T <: { def close(): Unit }](closable: T)(block: T => Unit) {
    try {
      block(closable)
    }
    finally {
      closable.close()
    }
  }

  def copyFile(input: InputStream, output: OutputStream) {
    closeAfterUse(input) { in =>
      closeAfterUse(output) { out =>
        val buffer = new Array[Byte](1024)
        Iterator.continually(in.read(buffer))
          .takeWhile(_ != -1)
          .foreach { out.write(buffer, 0 , _) }
      }
    }
  }

  def writeToFile(p: OutputStream, s: String) {
    val pw = new java.io.PrintWriter(p)
    try {
      pw.write(s)
    } finally {
      pw.close()
    }
  }
}

case class Template(val sections: Array[Section])

case class Section(val id: String, val fileName: String, val section: String, val tags: List[String], val content: String)