package com.sidewayscoding
package snippet

import model._
import comet._

import net.liftweb._
import http._
import SHtml._
import util._
import Helpers._

import com.sidewayscoding.model.EntryNameParam

class EntrySnippet(entryName: EntryNameParam) {

  object nameStr extends RequestVar(entryName.name)
  object descStr extends RequestVar("")

  def create = {
    val name = ValueCell(nameStr.is)
    val desc = ValueCell(descStr.is)
    val whence = S.referer openOr "/"

    "#name" #> textElem(name) &
    "#description" #> textareaElem(desc) &
    "type=submit" #> onSubmitUnit(() => {
      validate(name.get, desc.get) match {
        case Left(msg) => {
          nameStr(name.get)
          descStr(desc.get)
          S.error(msg)
        }
        case Right((name,description)) => {
          EntryServer ! AddMessage(Entry(name, List(Description(desc))))
          S.notice("Horray! The dictionary just grew bigger")
          S.redirectTo(whence)
        }
      }
    }) &
    "type=submit [id]" #> "EntrySubmissionButton"
  }

  /**
   * Cute little helper function that can validate that we're satisfied with the name and
   * description of whatever entry the user is making
   */
  private def validate(name: String, description: String): Either[String, (String,String)] = {
     (name, description) match {
        case ("","")     => Left("You need to give me a name and a description")
        case ("", _)     => Left("Sorry, what did you say it was called again?")
        case (_ ,"")     => Left("Didn't catch your description, can you repeat it please")
        case t @ (x ,y ) => Right(t)
      }
  }

}
