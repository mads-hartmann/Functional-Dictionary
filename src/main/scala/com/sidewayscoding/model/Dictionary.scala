package com.sidewayscoding.model

import scala.collection.{SortedSet}

case class Entry(name: String, description: String) 

object Dictionary {
  
  private var entries: List[Entry] = Nil
  
  def add(e: Entry) = entries = e :: entries
  
  def findAll = entries
  
  def findAllLike(str: String) = entries.filter( _.name.contains(str))
  
}
