package com.sidewayscoding.comet

import com.sidewayscoding.comet._
import com.sidewayscoding.model._
import net.liftweb.actor._
import net.liftweb.common.{ Box, Full, Empty }
import net.liftweb.http._
import net.liftweb.http.SHtml._
import net.liftweb.http.js.JsCmds._
import net.liftweb.util._
import net.liftweb.util.Helpers._
import scala.xml.{ Text, NodeSeq }

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
    val ifFound = for (
      name <- EntryName.get;
      entry <- EntryServer.find(name)
    ) yield {
      ".title" #> entry.name &
      ".description" #> entry.description.text & 
      ".rank" #> entry.description.rank.toString & 
      ".up" #> a(() => vote(entry, entry.description, Vote.UP), Text("up")) &
      ".down" #> a(() => vote(entry, entry.description, Vote.DOWN), Text("down")) &
      ".alternative" #> (entry.sortedDescriptions.map { altDescription => 
        ".alt-description" #> altDescription.text & 
        ".alt-rank" #> altDescription.rank.toString & 
        ".alt-up" #> a(() => vote(entry, altDescription, Vote.UP), Text("up")) &
        ".alt-down" #> a(() => vote(entry, altDescription, Vote.DOWN), Text("down"))
      }) 
    }
    
    ifFound.getOrElse(".wrapper" #> NodeSeq.Empty)
  }

  override def lowPriority = {
    case list: List[Entry] => reRender(false)
  }

  private def vote(entry: Entry, description: Description, vote: Vote.Value) = {
    EntryServer ! VoteMessage(entry, description, vote)
    Noop
  }

}

