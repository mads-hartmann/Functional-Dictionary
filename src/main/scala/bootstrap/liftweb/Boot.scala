package bootstrap.liftweb
import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap.{ SiteMap, Menu, Loc }
import _root_.net.liftweb.common.{ Full }
import com.sidewayscoding.model.{ Entry, Description, EntryNameParam }
import com.sidewayscoding.comet.{ EntryServer, AddMessage }

/*
 * For more information about the Boot class: http://simply.liftweb.net/index-3.1.html#toc-Section-3.1
 */
class Boot {
  def boot {

    /* where to search snippet
     * For more information about LiftRules: http://simply.liftweb.net/index-3.1.html#toc-Subsection-3.1.1
     */
    LiftRules.addToPackages("com.sidewayscoding")

    /* Building the SiteMap
     * For information aoubt SiteMap: http://simply.liftweb.net/index-3.2.html#toc-Section-3.2
     * For information about Menu.param: http://stable.simply.liftweb.net/#toc-Subsection-3.2.3
     */
    val entries =
      Menu("Home") / "index" ::
        Menu("Results") / "search" ::
        Menu("Entries") / "entries" ::
        Menu.param[EntryNameParam]("Entry", "entry",
                                   s => Full(EntryNameParam(emptyIfIndex(s))),
                                   e => e.name) / "entry" ::
        Menu.param[EntryNameParam]("Add", "add",
                                   s => Full(EntryNameParam(emptyIfIndex(s))),
                                   e => e.name) / "add" :: Nil

    LiftRules.setSiteMap(SiteMap(entries: _*))

    // set character encoding
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // Simply adding some data to the Entry Server you have something to look at
    EntryServer ! AddMessage(Entry("Functor", List(Description("A functor is a commen mispelling of function"))))
    EntryServer ! AddMessage(Entry("Functor", List(Description("A functor is something that can be mapped over"))))
    EntryServer ! AddMessage(Entry("Higher order function", List(Description("A higher order function is a function that takes as argument or returns another function"))))
  }

  def emptyIfIndex(str: String) = str match {
    case "index" => ""
    case s => s
  }

}
