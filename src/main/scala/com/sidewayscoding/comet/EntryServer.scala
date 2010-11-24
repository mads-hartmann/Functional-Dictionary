package com.sidewayscoding.comet

import net.liftweb._
import net.liftweb.actor._
import net.liftweb.http._
import scala.collection.mutable.{ HashMap }
import com.sidewayscoding.model._

// The messages that the actor can recieve 

case class AddMessage(entry: Entry)
case class VoteMessage(entry: Entry, description: Description, vote: Vote.Value)

object Vote extends Enumeration {
  type Vote = Value
  val UP, DOWN = Value
}

object EntryServer extends LiftActor with ListenerManager {

  private val entries = HashMap[String, Entry]()

  def createUpdate = findAll

  override def lowPriority = {
    case AddMessage(entry) => add(entry); updateListeners()
    case VoteMessage(entry, description, v) => vote(entry, description, v); updateListeners()
  }

  private def add(entry: Entry) = {
    if (entries.contains(entry.name)) {
      val old = entries.get(entry.name).get // we already checked        
      val updatedEntry = old.copy(old.name, old.descriptions ::: entry.descriptions)
      entries.update(old.name, updatedEntry)
    } else {
      entries.put(entry.name, entry)
    }
  }

  private def vote(entry: Entry, description: Description, vote: Vote.Value) = {
    vote match {
      case Vote.UP => description.rank = description.rank + 1
      case Vote.DOWN => description.rank = description.rank - 1
    }
    entries.update(entry.name, entry)
  }

  // reading related method - It's okay. we're just reading. 

  def find(name: String) = entries.get(name)

  def findAll = entries.values.toList

  def findAllLike(str: String) =
    entries.values.toList.filter(_.name.toLowerCase.contains(str.toLowerCase))

}
