package com.sidewayscoding.snippet

import com.sidewayscoding.model._
import xml.{ Text, NodeSeq }
import net.liftweb.http.{ RequestVar }
import net.liftweb.util.Helpers._
import net.liftweb.http.S
import net.liftweb.http.S._
import net.liftweb.http.SHtml._
import net.liftweb.common._

class FunctionalDictionary {

  object word extends RequestVar("")

  def render(xhtml: NodeSeq): NodeSeq =
    bind("dictionary", xhtml,
      "search" -%> text(word.is, s => word(s)),
      "submit" -%> submit("", () => {
        redirectTo("/search?query=" + word)
      }))

  def results(xhtml: NodeSeq): NodeSeq = {
    (for (query <- S.param("query")) yield {
      bindResults(query, xhtml)
    }) getOrElse {
      S.error("Please supply a query")
      redirectTo("/")
    }
  }

  def entries(xhtml: NodeSeq): NodeSeq = {
    val all = Dictionary.findAll
    val entryMap = all.groupBy { entry => entry.name.charAt(0).toUpperCase }

    bind("entries", xhtml,
      "size" -> all.size.toString,
      "letters" -> entryMap.keys.toList.flatMap { letter =>
        val list = entryMap.getOrElse(letter, Nil)
        val nodeseq = chooseTemplate("entries", "letters", xhtml)
        bindLetter(letter, list, nodeseq)
      })
  }

  def entry(xhtml: NodeSeq): NodeSeq = {
    (for (
      name <- S.param("name");
      entry <- Dictionary.find(name)
    ) yield {
      bind("entry", xhtml,
        "name" -> entry.name,
        "description" -> entry.description.text,
        "descriptionCount" -> entry.descriptions.size,
        "altDescriptions" -> entry.descriptions.flatMap { description =>
          bind("altDescription", chooseTemplate("entry", "altDescriptions", xhtml),
            "descriptionCount" -> description.rank.toString,
            "description" -> description.text)
        })
    }) getOrElse {
      S.error("Sorry, no entry found")
      NodeSeq.Empty
    }
  }

  private def bindLetter(letter: Char, list: List[Entry], xhtml: NodeSeq): NodeSeq = {
    bind("letter", xhtml,
      "letter" -> letter.toString,
      "list" -> list.flatMap { entry =>
        bind("entry", chooseTemplate("letter", "list", xhtml),
          "name" -> entry.name,
          "description" -> entry.description.text,
          AttrBindParam("link", "/entry/%s".format(entry.name), "href"))
      })
  }

  private def bindResults(query: String, xhtml: NodeSeq): NodeSeq = {
    val entries = Dictionary.findAllLike(query)

    bind("results", xhtml,
      "querySize" -> Text(entries.size.toString),
      "list" -> entries.flatMap { entry =>
        bind("result", chooseTemplate("results", "list", xhtml),
          "name" -> entry.name,
          "description" -> entry.description.text,
          AttrBindParam("link", "/entry/%s".format(entry.name), "href"))
      })
  }

}
