package bootstrap.liftweb

import _root_.net.liftweb.http.{ LiftRules }
import _root_.net.liftweb.sitemap.{ SiteMap, Menu, Loc }
import _root_.net.liftweb.util.{ Props }
import _root_.net.liftweb.common.{ Full, Empty }
import _root_.net.liftweb.http.{ S }

class Boot {
  def boot {

    // where to search snippet
    LiftRules.addToPackages("com.sidewayscoding")

    // build sitemap
    val entries =
      Menu("Home") / "index" ::
      Menu("Results") / "search" ::
      Menu("Entries") / "entries" ::
      Menu("Add") / "add" ::
      Nil

    LiftRules.setSiteMap(SiteMap(entries: _*))

    // set character encoding
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

  }
}
