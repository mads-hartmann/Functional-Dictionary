package com.sidewayscoding.snippet

import com.sidewayscoding.model._
import com.sidewayscoding.comet._
import xml.{ Text, NodeSeq }
import net.liftweb.http.{ RequestVar }
import net.liftweb.util.Helpers._
import net.liftweb.http.S
import net.liftweb.http.S._
import net.liftweb.http.SHtml._
import net.liftweb.common._

/*
 * This is the snippet that takes care of display the search results and the full list of entries
 *
 * For more information about snippets: http://simply.liftweb.net/index-3.4.html#toc-Section-3.4
 */
class FunctionalDictionary {

  /* This snippet uses CSS Selector Transforms to bind the xml to content. See:
   * http://simply.liftweb.net/index-7.10.html#sec:CSS-Selector-Transforms
   */

  /* This is the search form:
   *
   * For more information about forms in lift: http://simply.liftweb.net/index-Chapter-4.html#toc-Chapter-4
   */
  def render = {
    var word = ""
    "#search" #> text(word, word = _) &
    "#submit" #> onSubmitUnit(() => {
      redirectTo("/search?query=" + urlEncode(word))
    })
  }

  def results = {
    (for (query <- S.param("query")) yield {
      bindResults(query)
    }) getOrElse {
      S.error("Please supply a query")
      redirectTo("/")
    }
  }

  def entries = {
    val all = EntryServer.findAll
    val entryMap = all.groupBy { entry => entry.name.charAt(0).toUpper }

    ".letter-result" #> entryMap.keys.toList.map { letter =>
      val entries = entryMap.getOrElse(letter, Nil)
      ".letter *" #> letter.toString &
      ".item" #> entries.map { bindEntry _ }
    }
  }

  private def bindResults(query: String) = {
    val entries =
      EntryServer.findAll.filter(_.name.toLowerCase.
                                 contains(query.toLowerCase))

    "#count" #> entries.size.toString &
    ".result" #> entries.map { bindEntry _ }
  }

  private def bindEntry(entry: Entry) = {
    ".name" #> entry.name &
    ".description *" #> entry.description.text &
    ".link [href]" #> "/entry/%s".format(urlEncode(entry.name))
  }

}
