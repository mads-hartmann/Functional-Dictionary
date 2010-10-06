package com.sidewayscoding.model

import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._

class Entry extends LongKeyedMapper[Entry] with IdPK {

  def getSingleton = Entry

  object name extends MappedString(this,256) {
    override def validations = notEmpty(this) _ :: Nil
  }
  
  object description extends MappedString(this, 256) {
    override def validations = notEmpty(this) _ :: Nil
  }

  private def notEmpty(field : FieldIdentifier)(str: String) = str.length match {
    case 0 => List(FieldError(field, "You have to write at least one character"))
    case _ => List[FieldError]()
  }

}
object Entry extends Entry with LongKeyedMetaMapper[Entry]
