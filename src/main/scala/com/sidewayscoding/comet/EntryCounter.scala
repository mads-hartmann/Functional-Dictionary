package com.sidewayscoding.comet

import com.sidewayscoding.comet._
import com.sidewayscoding.model._
import net.liftweb.actor._
import net.liftweb.http._
import scala.xml.{ Text }

/*
 * This is the CometActor that displays the total number of entries in the dictionary. It's a comet
 * actor because we want to update the number dynamically on all pages if another user adds a new
 * entry.
 *
 * For more information about Comet: http://simply.liftweb.net/index-7.13.html#toc-Section-7.13
 */
class EntryCounter extends CometActor with CometListener {

  private var couter = 0

  def render = Text(couter.toString)

  def registerWith = EntryServer

  override def lowPriority = {
    case EntryTable(m) => couter = m.size; reRender()
    case (EntryTable(m), _) => couter = m.size; reRender()
  }

}
