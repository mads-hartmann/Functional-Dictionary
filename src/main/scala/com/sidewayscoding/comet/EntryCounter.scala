package com.sidewayscoding.comet

import com.sidewayscoding.comet._
import com.sidewayscoding.model._
import net.liftweb.actor._
import net.liftweb.http._
import scala.xml.{ Text }

class EntryCounter extends CometActor with CometListener {

  private var couter = 0

  def render = Text(couter.toString)

  def registerWith = EntryServer

  override def lowPriority = {
    case EntryTable(m) => couter = m.size; reRender()
    case (EntryTable(m), _) => couter = m.size; reRender()
  }

}
