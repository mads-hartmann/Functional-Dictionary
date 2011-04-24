package com.sidewayscoding
package snippet

import net.liftweb._
import http._
import common._

import com.sidewayscoding.model.EntryNameParam

import scala.xml.NodeSeq

/*
 * This is simply a snippet that injects the XML needed to create a named Comet component.
 *
 * For more information about Comet: http://simply.liftweb.net/index-7.13.html#toc-Section-7.13
 * For more information about snippets that take url parameters: http://simply.liftweb.net/index-3.4.html#sub:Param-Example
 */
class MakeComet(entryName: EntryNameParam) {

  def render(in: NodeSeq): NodeSeq =
   <lift:comet type="EntryComet" name={entryName.name}>{in}</lift:comet>

}
