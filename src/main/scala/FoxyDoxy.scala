import com.google.gson.GsonBuilder
import com.scalaprog.foxydoxy.cli.CommandLineConfiguration
import com.thoughtworks.qdox.JavaDocBuilder
import java.util
import javax.annotation.processing.AbstractProcessor
import javax.tools.ToolProvider
import org.apache.commons.io.IOUtils
import org.pegdown.PegDownProcessor
import org.sellmerfud.optparse.OptionParser
import scala.collection.JavaConversions._
import java.io._

/**
 * User: soren
 */
class FoxyDoxy {

  def parseUsingQDox(srcDir: String) = {
    val builder = new JavaDocBuilder();

    builder.addSourceTree(new File(srcDir));
    val sections = builder.getSources.map(source => {
      val tag = source.getClasses.head.getTagByName("Documentation")
      if (tag != null) {
        val section = source.getClasses.head.getTagByName("section")
        val tags = source.getClasses.head.getTagsByName("tags")
        println(tag.getValue)
        val rawText = tag.getValue
        val html = new PegDownProcessor().markdownToHtml(rawText)
        Some(Section(section.getValue, tags.map(t => t.getValue).toList, html))
      } else
      None
    }).flatten

    println(sections)
    Template(sections)
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
    val outputDir = config.outputDirectory+File.separator

    val gson = new GsonBuilder().setPrettyPrinting().create()
    val data =  new FoxyDoxy().parseUsingQDox(config.sourceDirectory) //Template(sections)
    println(data)
    println("----------------")
    println(gson.toJson(data))
    config.templateFile match {
      case Some(x) =>
      case None => IOUtils.copy(getClass.getResourceAsStream("template.html"), new FileOutputStream(outputDir+"template.html"))
    }
    val templateFile = new FileOutputStream(config.templateFile.getOrElse(outputDir+"template.json"))
    writeToFile(templateFile, gson.toJson(data))

    //new FoxyDoxy().testing
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

case class Section(val section: String, val tags: List[String], val content: String)
