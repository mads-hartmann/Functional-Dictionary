package bootstrap.liftweb
import _root_.net.liftweb.http.{ LiftRules }
import _root_.net.liftweb.sitemap.{ SiteMap, Menu, Loc }
import _root_.net.liftweb.http._

class Boot {
  def boot {

    // where to search snippet
    LiftRules.addToPackages("com.sidewayscoding")

    LiftRules.statefulRewrite.append {
      case RewriteRequest( 
        ParsePath("entry" :: name :: _, _, _, _), _, _) => {
          RewriteResponse("entry" :: Nil, Map("name" -> name))
        }
    }

    // build sitemap
    val entries =
      Menu("Home") / "index" ::
        Menu("Results") / "search" ::
        Menu("Entries") / "entries" ::
        Menu("Entry") / "entry" ::
        Menu("test") / "test" ::
        Menu("Add") / "add" ::
        Nil

    LiftRules.setSiteMap(SiteMap(entries: _*))

    // set character encoding
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

  }
}
