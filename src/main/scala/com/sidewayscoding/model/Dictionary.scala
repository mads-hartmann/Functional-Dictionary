package com.sidewayscoding.model

import net.liftweb._
import util._

final case class Description(text: String, id: String = Helpers.nextFuncName, rank: Int = 0) extends Ordered[Description] {
  def compare(that: Description) = -(rank.compare(that.rank)) // Want it to be sorted descending. 
  override def toString(): String = "(%s %s , %s)".format(text, id, rank.toString)
}

final case class Entry(name: String, descriptions: List[Description] = Nil) extends Ordered[Entry] {
  def description = descriptions.sorted.head
  def descriptionTail = descriptions.sorted.tail
  def merge(other: Entry): Entry =
    copy(descriptions = this.descriptions ::: other.descriptions)

  def update(id: String)(f: Description => Description): Entry =
    copy(descriptions = this.descriptions.map {
      case d @ Description(_, theId, _) if id == theId => f(d)
      case d => d
    })

  def compare(that: Entry) = name.compare(that.name)
}

final case class EntryTable(entries: Map[String, Entry] = Map()) {
  def add(entry: Entry): EntryTable = 
    EntryTable(entries + (entry.name -> 
                          entry.merge(entries.
                                      getOrElse(entry.name,
                                                Entry(entry.name)))))

  def update(ent: Entry)(f: Entry => Entry): EntryTable =
    entries.get(ent.name) match {
      case Some(entry) => EntryTable(entries + (ent.name -> f(entry)))
      case _ => this
    }

}
