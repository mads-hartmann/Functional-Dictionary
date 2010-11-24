package com.sidewayscoding.model

case class Description(text: String, id: String) extends Ordered[Description] {
  var rank = 0 // It's okay - these classes are only sent in immutable data structues.
  def compare(that: Description) = -(rank.compare(that.rank)) // Want it to be sorted descending. 
  override def toString(): String =  "(%s %s , %s)".format(text, id, rank.toString) 
}

case class Entry(name: String, descriptions: List[Description]) extends Ordered[Entry] {
  def description = descriptions.sorted.head
  def sortedDescriptions = descriptions.sorted.tail
  def compare(that: Entry) = name.compare(that.name)
}