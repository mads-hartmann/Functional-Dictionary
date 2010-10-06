package bootstrap.liftweb

import _root_.net.liftweb.http.{ LiftRules }
import _root_.net.liftweb.sitemap.{ SiteMap, Menu, Loc }
// imports relevant for mapper & db usage
import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util.{Props}
import _root_.net.liftweb.common.{Full, Empty}
import _root_.net.liftweb.http.{S}
import com.sidewayscoding.model.{Entry}

class Boot {
  def boot {
    
    // Use embedded DB 
    if (!DB.jndiJdbcConnAvailable_?) {
      val vendor =
        new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
          Props.get("db.url") openOr
          "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
          Props.get("db.user"), Props.get("db.password"))
    
      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)
    
      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    }
    
    S.addAround(DB.buildLoanWrapper)
    
    Schemifier.schemify(true, Schemifier.infoF _, Entry)

    // where to search snippet
    LiftRules.addToPackages("com.sidewayscoding")

    // build sitemap
    val entries = 
      Menu("Home") / "index" ::
      Menu("Entries") / "entries" ::
      Menu("Add") / "add" :: 
      Nil

    LiftRules.setSiteMap(SiteMap(entries: _*))

    // set character encoding
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

  }
}
