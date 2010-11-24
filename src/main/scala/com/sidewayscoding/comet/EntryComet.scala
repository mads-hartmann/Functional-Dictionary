package com.sidewayscoding.comet

import net.liftweb._
import http._
import SHtml._
import net.liftweb.common.{ Box, Full, Empty }
import net.liftweb.util._
import net.liftweb.actor._
import net.liftweb.util.Helpers._
import js._
import JsCmds._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.JE.Str
import com.sidewayscoding.model._
import scala.xml.{ NodeSeq }

// CometActors live outside the scope of an HTTP request. Because of this 
// this SessionVar is being set in boot in a statefull rewrite request
// so the comet actor knows which entry to display. 
object EntryName extends SessionVar[Option[String]](None)

class EntryComet extends CometActor with CometListener {

  // The following specifies a default prefix
  override def defaultPrefix = Full("entry")

  def registerWith = EntryServer    

  // Intial bindings
  def render = {
    (for (
      name <- EntryName.get;
      entry <- EntryServer.find(name)
    ) yield {
      val xml = bind(
        "name" -> entry.name,
        "description" -> entry.description.text,
        "descriptionCount" -> entry.descriptions.size,
        "altDescriptions" -> entry.descriptions.flatMap { description =>
          bind("altDescription", chooseTemplate(defaultPrefix.get, "altDescriptions", defaultXml),
            "descriptionCount" -> description.rank.toString,
            "description" -> description.text)
        })
      RenderOut(xml, Empty, Empty, Empty, false)
    }).getOrElse {
      S.error("Sorry, no entry found")
      RenderOut(Empty, Empty, Empty, Empty, false)
    }
  }

  override def lowPriority = {
    case list: List[Entry] => 
      println("test")
      partialUpdate(Noop)
  }
}

