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

class FunctionalDictionary {

  object word extends RequestVar("")

  def render =
    "#search" #> text(word.is, s => word(s)) &
    "#submit" #> submit("", () => {
      redirectTo("/search?query=" + word)
    })

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
    val entries = EntryServer.findAllLike(query)
    
    "#count" #> entries.size.toString &
    ".result" #> entries.map { bindEntry _ }
  }
  
  private def bindEntry(entry: Entry) = 
    ".name" #> entry.name &
    ".description" #> entry.description.text & 
    ".link [href]" #> "/entry/%s".format(entry.name)
  
}
