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

class EntryCounter extends CometActor with CometListener {
  
  private var couter = 0
  
  def render = Text(couter.toString)
  
  def registerWith = EntryServer
  
  override def lowPriority = {
    case list: List[Entry] => couter = list.size; reRender(true)
  }
  
}