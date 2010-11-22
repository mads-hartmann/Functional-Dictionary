package com.sidewayscoding.model

import scala.collection.mutable.{ HashMap }

case class Description(text: String, rank: Int) 

case class Entry(name: String, descriptions: List[Description]) extends Ordered[Entry] {
  def description = descriptions(0)
  def compare(that: Entry) = name.compare(that.name)
}

object Dictionary {

  @volatile
  private var entries = HashMap[String, Entry]()

  def add(e: Entry): Unit = synchronized { 
    if (entries.contains(e.name)) {
      val old = entries.get(e.name).get // we already checked
      val updatedEntry = old.copy(old.name,old.descriptions ::: e.descriptions)
      entries.update(old.name,updatedEntry)
    } else {
      entries.put(e.name,e)
    }
  }

  def find(name: String) = entries.get(name)

  def findAll = entries.values.toList

  def findAllLike(str: String) =
    entries.values.toList.filter(_.name.toLowerCase.contains(str.toLowerCase))

}
