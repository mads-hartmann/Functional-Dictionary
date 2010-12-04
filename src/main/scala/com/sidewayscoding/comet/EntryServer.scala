package com.sidewayscoding.comet

import net.liftweb._
import net.liftweb.actor._
import net.liftweb.http._
import com.sidewayscoding.model._

// The messages that the actor can recieve 

case class AddMessage(entry: Entry)
case class VoteMessage(entry: Entry, description: Description, vote: Vote.Value)

object Vote extends Enumeration {
  type Vote = Value
  val UP, DOWN = Value
}

object EntryServer extends LiftActor with ListenerManager {

  @volatile private var entries = EntryTable()

  def createUpdate = entries

  override def lowPriority = {
    case AddMessage(entry) => {
      entries = entries.add(entry)
      updateListeners(entries -> entry.name)
    }

    case VoteMessage(entry, description, v) => {
      entries = entries.update(entry) {
        _.update(description.id) {
          d => d.copy(rank = d.rank + (v match {
            case Vote.UP => 1
            case Vote.DOWN => -1
          }))
        }
      }

      updateListeners(entries -> entry.name)
    }
  }

  /**
   * In general, it's bad to mix concurrency paradigms,
   * but becasue the entries is an immutable data
   * structure and it's in a volatile var, we can access
   * it outside the Actor thread
   */
  def findAll: List[Entry] = entries.entries.values.toList
}
