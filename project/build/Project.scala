import sbt._

class LiftProject(info: ProjectInfo) extends DefaultWebProject(info) with stax.StaxPlugin {
  
  val mavenLocal         = "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"
  val scalatoolsSnapshot = "Scala Tools Snapshot"   at "http://scala-tools.org/repo-releases/"
  val liftVersion        = "2.2"

  override def libraryDependencies = Set(
    "net.liftweb" % "lift-webkit_2.8.1" % liftVersion % "compile->default",
    "net.liftweb" % "lift-testkit_2.8.1" % liftVersion % "compile->default",
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default",
    "org.scala-tools.testing" % "specs" % "1.6.1" % "test->default",
    // selenium 
	  "org.scalatest" % "scalatest" % "1.3" % "test->default",
	  "org.seleniumhq.selenium" % "selenium" % "2.0b1" % "test->default",
	  "org.seleniumhq.selenium" % "selenium-server" % "2.0b1" % "test->default"
  ) ++ super.libraryDependencies
}
