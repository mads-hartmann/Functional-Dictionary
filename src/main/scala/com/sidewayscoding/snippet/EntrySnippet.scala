package com.sidewayscoding.snippet

import xml.{Text, NodeSeq}
import net.liftweb.util.Helpers._
import net.liftweb.http.S
import net.liftweb.http.S._
import net.liftweb.http.SHtml._
import net.liftweb.common._
import com.sidewayscoding.model.{ Entry }

class EntrySnippet {

	def create(xhtml: NodeSeq): NodeSeq = 
		Entry.create.toForm(Full("Save"), { entry =>
		  entry.validate match {
		    case Nil => 
		      entry.save
		      S.notice("Successfully created the entry: " + entry.name)
		      redirectTo("/")
		    case errors => 
		      errors.foreach { error => 
		        S.error(error.field.uniqueFieldId.openOr("") + ": " + error.msg) }
		  }
		})

}