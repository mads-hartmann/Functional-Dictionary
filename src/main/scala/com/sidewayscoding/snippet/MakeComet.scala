package com.sidewayscoding
package snippet

import net.liftweb._
import http._
import common._

import scala.xml.NodeSeq

object MakeComet {
  def render(in: NodeSeq): NodeSeq = {
    val whence = S.referer openOr "/"

    S.param("name") match {
      case Full(name) => <lift:comet type="EntryComet" name={name}>{in}</lift:comet>
      case _ => S.error("No Name"); S.redirectTo(whence)
    }
  }
}
