package com.sidewayscoding.snippet

import com.sidewayscoding.model._
import xml.{ Text, NodeSeq }
import net.liftweb.util.Helpers._
import net.liftweb.http.{ S, RequestVar }
import net.liftweb.http.S._
import net.liftweb.http.SHtml._
import net.liftweb.common._

class EntrySnippet {

  object name extends RequestVar[String]("")
  object description extends RequestVar[String]("")

  def create(xhtml: NodeSeq): NodeSeq = {
    bind("entry", xhtml,
      "name" -> text(name, str => name(str)),
      "description" -> textarea(description, str => description(str)),
      "submit" -> submit("Save entry", () => {
        val e = new Entry(name, description)
        Dictionary.add(e)
        S.notice("Horray! The dictionary just grew bigger")
        redirectTo("/")
      }))
  }

}