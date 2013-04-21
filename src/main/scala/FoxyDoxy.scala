import com.google.gson.GsonBuilder
import com.scalaprog.foxydoxy.cli.CommandLineConfiguration
import org.apache.commons.io.IOUtils
import org.sellmerfud.optparse.OptionParser
import java.io._

/**
 * User: soren
 */
class FoxyDoxy {

  def parseSourceDirectory(srcDir: String, foundSections: List[Section]): List[Section] = {
    val cwd = new File(srcDir)
    if (cwd.isDirectory) {
      val found = cwd.listFiles().map(file => {
        parseSourceDirectory(file.getCanonicalPath, foundSections)
      }).flatten.toList
      return found ::: foundSections
    }
    else {
      val sourceFile = cwd.getCanonicalPath
      if (sourceFile.endsWith(".java") || sourceFile.endsWith(".scala")) {
        val f = SourceCodeParser.parseToSections(sourceFile)
        if(!f.isEmpty)
          println("Added documentation for "+sourceFile)
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

case class Section(val fileName: String, val section: String, val tags: List[String], val content: String)
