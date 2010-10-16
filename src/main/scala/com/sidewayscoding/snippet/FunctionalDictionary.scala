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
      "submit" -%> submit("", () => Unit))

  def results(xhtml: NodeSeq): NodeSeq = word.is match {
    case "" => NodeSeq.Empty
    case str => bindResults(str, xhtml)
  }

  def entries(xhtml: NodeSeq): NodeSeq = {
    val all = Dictionary.findAll
    val entryMap = all.groupBy { entry => entry.name.charAt(0) }

    bind("entries", xhtml,
      "size" -> all.size.toString,
      "letters" -> entryMap.keys.toList.flatMap { letter =>
        val list = entryMap.getOrElse(letter, Nil)
        val nodeseq = chooseTemplate("entries", "letters", xhtml)
        bindLetter(letter, list, nodeseq)
      })
  }

  private def bindLetter(letter: Char, list: List[Entry], xhtml: NodeSeq): NodeSeq = {
    bind("letter", xhtml,
      "letter" -> letter.toString,
      "list" -> list.flatMap { entry =>
        bind("entry", chooseTemplate("letter", "list", xhtml),
          "name" -> entry.name,
          "description" -> entry.description)
      })
  }

  private def bindResults(query: String, xhtml: NodeSeq): NodeSeq = {
    val entries = Dictionary.findAllLike(query)

    bind("results", xhtml,
      "querySize" -> Text(entries.size.toString),
      "list" -> entries.flatMap { entry =>
        bind("result", chooseTemplate("results", "list", xhtml),
          "name" -> entry.name,
          "description" -> entry.description)
      })
  }

}