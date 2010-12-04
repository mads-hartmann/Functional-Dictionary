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

class EntryComet extends CometActor with CometListener {
  // The ListenerManager that it's listening on 
  def registerWith = EntryServer

  private var entry: Box[Entry] = Empty

  // Intial bindings 
  def render =
    this.entry match {
      case Full(entry) => {
        ".title" #> entry.name &
        ".description" #> entry.description.text & 
        ".rank" #> entry.description.rank.toString & 
        ".up" #> a(() => vote(entry.description, Vote.UP),
                   Text("up")) &
        ".down" #> a(() => vote(entry.description,
                                Vote.DOWN), Text("down")) &
        "#alternatives" #> (
          "li *" #> entry.descriptionTail.map (
            d => {
              "span *" #> d.rank.toString &
              "p *" #> d.text &
              ".alt-up *" #> a(() => vote(d, Vote.UP), 
                               Text("up")) &
              ".alt-down *" #> a(() => vote(d, Vote.DOWN), 
                                 Text("down"))
            }
            )
          )
      }

      case _ => ".wrapper" #> NodeSeq.Empty
    }


  override def lowPriority = {
    case EntryTable(m) => entry = name.flatMap(m.get); reRender()

    case (EntryTable(m), entName: String) if Full(entName) == name =>
      entry = m.get(entName); reRender()
  }

  private def vote(description: Description, vote: Vote.Value) = {
    entry.foreach {
      e => {
        EntryServer ! VoteMessage(e, description, vote)
      }
    }

    Noop
  }

}

