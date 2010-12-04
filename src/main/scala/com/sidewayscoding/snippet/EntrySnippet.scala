package com.sidewayscoding
package snippet

import model._
import comet._

import net.liftweb._
import http._
import SHtml._
import util._
import Helpers._

object EntrySnippet {
  def create = {
    val name = ValueCell("")
    val desc = ValueCell("")
    val whence = S.referer openOr "/"

    "#name" #> textElem(name) &
    "#description" #> textareaElem(desc) &
    "type=submit" #> onSubmitUnit(() => {
      EntryServer ! AddMessage(Entry(name.get, List(Description(desc.get))))
      S.notice("Horray! The dictionary just grew bigger")
      S.redirectTo(whence)
    })
  }
}
