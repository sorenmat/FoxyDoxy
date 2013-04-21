import com.google.gson.GsonBuilder
import com.scalaprog.foxydoxy.cli.CommandLineConfiguration
import com.thoughtworks.qdox.JavaDocBuilder
import org.apache.commons.io.IOUtils
import org.pegdown.PegDownProcessor
import org.sellmerfud.optparse.OptionParser
import java.io._
import scala.io._

/**
 * User: soren
 */
class FoxyDoxy {
 /*
  def parseUsingQDox(srcDir: String) = {

    val DOCUMENTATION: String = "documentation"
    val SECTION: String = "section"
    val TAGS: String = "tags"

    val builder = new JavaDocBuilder();
    builder.addSourceTree(new File(srcDir));
    val sections = builder.getSources.map(source => {

      val tag = source.getClasses.head.getTagByName(DOCUMENTATION)
      if (tag != null) {
        val section = source.getClasses.head.getTagByName(SECTION)
        val tags = source.getClasses.head.getTagsByName(TAGS)
        println(tag.getValue)
        val rawText = tag.getValue
        val html = new PegDownProcessor().markdownToHtml(rawText)
        Some(Section(section.getValue, tags.map(t => t.getValue).toList, html))
      } else
        None
    }).flatten

    Template(sections)
  }
   */
  def parseSourceDirectory(srcDir: String, foundSections: List[Section]): List[Section] = {
    val cwd = new File(srcDir)
    if (cwd.isDirectory) {
      val found = cwd.listFiles().map(file => {
        parseSourceDirectory(file.getCanonicalPath, foundSections)
      }).flatten.toList
      return found ::: foundSections
    }
    else {
      println(cwd.getCanonicalPath)
      val sourceFile = cwd.getCanonicalPath
      if (sourceFile.endsWith(".java")) {
        val f = SourceCodeParser.parseToSections(sourceFile)
        return f ::: foundSections

      } else foundSections
    }

  }
}


object FoxyDoxy {
  def main(args: Array[String]) {

    val cli: OptionParser[CommandLineConfiguration] = new OptionParser[CommandLineConfiguration] {
      reqd[String]("-s", "--source DIRECTORY", "Source directory to scan for documentation") {
        (value, cfg) => cfg.copy(sourceDirectory = value)
      }
      optl[String]("-t", "--template FILE", "Choose non default template") {
        (value, cfg) => cfg.copy(templateFile = value)
      }
      optl[String]("-o", "--output DIRECTORY", "Directory where output files will be placed") {
        (value, cfg) => cfg.copy(templateFile = value)
      }
    }

    val config = cli.parse(args.toList, CommandLineConfiguration())

    new File(config.outputDirectory).mkdir() // create output directory
    val outputDir = config.outputDirectory + File.separator

    val gson = new GsonBuilder().setPrettyPrinting().create()
    //val data = new FoxyDoxy().parseUsingQDox(config.sourceDirectory) //Template(sections)
    val data = Template(new FoxyDoxy().parseSourceDirectory(config.sourceDirectory, Nil).toArray)
    config.templateFile match {
      case Some(x) =>
      case None => IOUtils.copy(getClass.getResourceAsStream("template.html"), new FileOutputStream(outputDir + "template.html"))
    }
    val templateFile = new FileOutputStream(config.templateFile.getOrElse(outputDir + "template.json"))
    writeToFile(templateFile, gson.toJson(data))

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

case class Section(val fileName: String ,val section: String, val tags: List[String], val content: String)
