package test.scala.com.sidewayscoding.highlevel


import org.scalatest.{ FunSuite, SuperSuite, BeforeAndAfterAll}
import org.mortbay.jetty.{ Connector, Server}
import org.mortbay.jetty.webapp.{ WebAppContext }
import org.openqa.selenium.server.RemoteControlConfiguration
import org.openqa.selenium.server.SeleniumServer
import com.thoughtworks.selenium._
import com.sidewayscoding.model._

class AddEntityTest extends FunSuite with BeforeAndAfterAll {

  private var server         : Server          = null
  private var selenium       : DefaultSelenium = null
  private var seleniumserver : SeleniumServer  = null
  private val entry          = Entry("MyEntry", Description("My cute little addition to this dictionary") :: Nil)

  override def beforeAll() {
    /*  This code takes care of the following:

        1. Start an instance of your web application
        2. Start an instance of the Selenium backend
        3. Start an instance of the Selenium client
    */
    val GUI_PORT             = 8080
    val SELENIUM_SERVER_PORT = 4444

    // Setting up the jetty instance which will be running the
    // GUI for the duration of the tests
    server  = new Server(GUI_PORT)
    val context = new WebAppContext()
    context.setServer(server)
    context.setContextPath("/")
    context.setWar("src/main/webapp")
    server.addHandler(context)
    server.start()

    // Setting up the Selenium Server for the duration of the tests
    val rc = new RemoteControlConfiguration()
    rc.setPort(SELENIUM_SERVER_PORT)
    seleniumserver = new SeleniumServer(rc)
    seleniumserver.boot()
    seleniumserver.start()
    seleniumserver.getPort()

    // Setting up the Selenium Client for the duration of the tests
    selenium = new DefaultSelenium("localhost", SELENIUM_SERVER_PORT, "*firefox", "http://localhost:"+GUI_PORT+"/")
    selenium.start()
  }

  override def afterAll() {
    // Close everyhing when done
    selenium.close()
    server.stop()
    seleniumserver.stop()
  }

  test("Test that searching doesn't show anything when empty") {
    selenium.open("/")
    selenium.`type`("search",entry.name )
    selenium.click("submit")
    selenium.waitForPageToLoad("30000")
    assert(selenium.isTextPresent("Found 0 matches to your criteria"), true)
  }

  test("""Test that you can create an entry and it will increment 
          counter and show up in search results""") {
    selenium.open("/")
		selenium.click("//x:a[contains(@href, '/add')]")
		selenium.waitForPageToLoad("30000")
		selenium.`type`("name", "MyEntry")
		selenium.`type`("description", "My cute little addition to this dictionary")
		selenium.click("EntrySubmissionButton")
		selenium.waitForPageToLoad("30000")
		assert(selenium.isTextPresent("Horray! The dictionary just grew bigger"), true)
		assert(selenium.isTextPresent("1 // // entries"), true)

    selenium.open("/")
    selenium.`type`("search", "MyEntry")
    selenium.click("submit")
    selenium.waitForPageToLoad("30000")
    assert(selenium.isTextPresent("Found 1 matches to your criteria"), true)
    assert(selenium.isTextPresent("MyEntry"), true)
    assert(selenium.isTextPresent("MyEntry My cute little addition to this dictionary"), true)
  }
}
