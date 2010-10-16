package com.sidewayscoding.model

import scala.collection.{ SortedSet }

case class Entry(name: String, description: String) extends Ordered[Entry] {
  def compare(that: Entry) = name.compare(that.name)
}

object Dictionary {

  private var entries = SortedSet[Entry]()

  def add(e: Entry) = entries += e

  def findAll = entries.toList

  def findAllLike(str: String) =
    entries.toList.filter(_.name.toLowerCase.contains(str.toLowerCase))

}
