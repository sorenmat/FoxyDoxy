import scala.util.parsing.combinator.{RegexParsers, JavaTokenParsers}
import scala.util.parsing.input.Positional

/**
 * User: soren
 */
object Testing extends RegexParsers {

  trait SearchKeyword extends Positional

  case class KeywordComment(content: String) extends SearchKeyword

  def compilationUnit: Parser[List[SearchKeyword]] = rep(comment)

  def comment: Parser[SearchKeyword] = positioned(blockComment)

  // regex: http://stackoverflow.com/questions/462843/improving-fixing-a-regex-for-c-style-block-comments
  def blockComment: Parser[SearchKeyword] = "/*" ~> """(?>(?:(?>[^*]+)|\*(?!/))*)""".r <~ "*/" ^^ KeywordComment

  def parse(input: String) = parseAll(compilationUnit, input)

  def main(args: Array[String]) {

    val source =
      """
        /*
         * test
         */

        package org.watermint.sourcecolon.indexer;
        import org.watermint.sourcecolon.analyzer.*;
        import org.watermint.sourcecolon.Main;

      """.stripMargin

    //val parser = new JavaKeywordParsers
    val keywords = Testing.parse(source)
    keywords.get.foreach((k: SearchKeyword) => println(k.pos.line, k))
  }
}
