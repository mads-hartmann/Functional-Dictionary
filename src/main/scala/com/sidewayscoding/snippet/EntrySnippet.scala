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

/*
 * This is the snippet that takes crares of displaying and processing the data for en entry
 * submission form.
 *
 * For more information about snippets: http://simply.liftweb.net/index-3.4.html#toc-Section-3.4
 * For more information about forms in lift: http://simply.liftweb.net/index-Chapter-4.html#toc-Chapter-4
 * For more information about snippets that take url parameters: http://simply.liftweb.net/index-3.4.html#sub:Param-Example
 */
class EntrySnippet(entryName: EntryNameParam) {

  /* For more information about RequestVar: http://simply.liftweb.net/index-4.4.html#toc-Section-4.4 */
  object nameStr extends RequestVar(entryName.name)
  object descStr extends RequestVar("")

  def create = {
    val name = ValueCell(nameStr.is)
    val desc = ValueCell(descStr.is)
    val whence = S.referer openOr "/"

    /* CSS Selector Transforms: http://simply.liftweb.net/index-7.10.html#sec:CSS-Selector-Transforms */
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
