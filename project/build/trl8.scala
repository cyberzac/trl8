import sbt._

class trl8Project(info: ProjectInfo) extends DefaultProject(info) {

  val mavenLocal = "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"

  val junit = "junit" % "junit" % "4.7" % "test"
  val testng = "org.testng" % "testng" % "5.14" % "test"
  val mockito = "org.mockito" %  "mockito-all" % "1.8.5" % "test"
  val specs = "org.scala-tools.testing" % "specs_2.8.1" % "1.6.7" % "test"
  val scalatest = "org.scalatest" % "scalatest" % "1.2"

  val slf4j_version = "1.6.1"
  val slf4j = "org.slf4j" % "slf4j-api" % slf4j_version
  val slf4j_log4j14 = "org.slf4j" % "slf4j-log4j12" % slf4j_version
  //val slf4j_simple = "org.slf4j" % "slf4j-simple" % slf4j_version

  val commons_httpclient = "org.apache.httpcomponents" % "httpclient" % "4.0.1"  withSources()

  val lift_version = "2.2"
  val lift_json = "net.liftweb" % "lift-json_2.8.0" % lift_version
  val lift_util = "net.liftweb" % "lift-util_2.8.0" % lift_version

  val oauth = "oauth.signpost" % "signpost-commonshttp4" % "1.2"
}

