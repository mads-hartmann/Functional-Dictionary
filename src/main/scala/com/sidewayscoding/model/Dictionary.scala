package com.sidewayscoding.model

case class Description(text: String, rank: Int, id: String)

case class Entry(name: String, descriptions: List[Description]) extends Ordered[Entry] {
  def description = descriptions(0)
  def compare(that: Entry) = name.compare(that.name)
}