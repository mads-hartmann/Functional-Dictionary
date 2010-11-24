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
import com.sidewayscoding.comet._
import scala.xml.{ NodeSeq, Text }

// CometActors live outside the scope of an HTTP request. Because of this 
// this SessionVar is being set in boot in a statefull rewrite request
// so the comet actor knows which entry to display. 
object EntryName extends SessionVar[Option[String]](None)

class EntryComet extends CometActor with CometListener {

  // The following specifies a default prefix
  override def defaultPrefix = Full("entry")

  // The ListenerManager that it's listening on 
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
        "rank" -> entry.description.rank.toString,
        "voteUp" -> a(() => vote(entry, entry.description, Vote.UP), Text("up")),
        "voteDown" -> a(() => vote(entry, entry.description, Vote.DOWN), Text("down")),
        "altDescriptions" -> entry.sortedDescriptions.flatMap { description =>
          bind("altDescription", chooseTemplate(defaultPrefix.get, "altDescriptions", defaultXml),
            "rank" -> description.rank.toString,
            "description" -> description.text,
            "voteUp" -> a(() => vote(entry, description, Vote.UP), Text("up")),
            "voteDown" -> a(() => vote(entry, description, Vote.DOWN), Text("down")))
        })
      RenderOut(xml, Empty, Empty, Empty, false)
    }).getOrElse {
      S.error("Sorry, no entry found")
      RenderOut(Empty, Empty, Empty, Empty, false)
    }
  }

  override def lowPriority = {
    case list: List[Entry] => reRender(false)
  }
  
  private def vote(entry: Entry, description: Description, vote: Vote.Value) = {
    EntryServer ! VoteMessage(entry, description, vote)
    Noop
  }
  
}

