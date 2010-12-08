import sbt._

class LiftProject(info: ProjectInfo) extends DefaultWebProject(info) with stax.StaxPlugin {
  
  //stax deplot
	override def staxApplicationId = "functionaldictionary"
	override def staxUsername = "mads379"
  
  val mavenLocal = "Local Maven Repository" at
  "file://"+Path.userHome+"/.m2/repository"

   val scalatoolsSnapshot = "Scala Tools Snapshot" at
  "http://scala-tools.org/repo-snapshots/"

  val scalatoolsRelease = "Scala Tools Snapshot" at
  "http://scala-tools.org/repo-releases/"

  val liftVersion = "2.2-SNAPSHOT"

  override def libraryDependencies = Set(
    "net.liftweb" % "lift-webkit_2.8.1" % liftVersion % "compile->default",
    "net.liftweb" % "lift-testkit_2.8.1" % liftVersion % "compile->default",
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default",
    "junit" % "junit" % "4.5" % "test->default",
    "org.scala-tools.testing" % "specs" % "1.6.1" % "test->default"
  ) ++ super.libraryDependencies
}
