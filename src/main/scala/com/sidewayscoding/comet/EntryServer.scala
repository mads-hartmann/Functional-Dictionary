package com.sidewayscoding.comet

import net.liftweb._
import net.liftweb.actor._
import net.liftweb.http._
import scala.collection.mutable.{ HashMap }
import com.sidewayscoding.model._

// The messages that the actor can recieve 
case class AddMessage(entry: Entry) 
case class VoteMessage(description: Description) 

object EntryServer extends LiftActor with ListenerManager {
  
  private var entries = HashMap[String, Entry]()
  
  def createUpdate = findAll
  
  override def lowPriority = {
    case AddMessage(entry) => add(entry); updateListeners()
    // case VoteMessage(description) => 
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
  
  // reading related method - It's okay. we're just reading. 
  
  def find(name: String) = entries.get(name)

  def findAll = entries.values.toList

  def findAllLike(str: String) =
    entries.values.toList.filter(_.name.toLowerCase.contains(str.toLowerCase))
  
}