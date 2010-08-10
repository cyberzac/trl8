import sbt._

class TwitterTrl8Project(info: ProjectInfo) extends DefaultProject(info) {
  //val mavenLocal = "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"

  val junit = "junit" % "junit" % "4.7" % "test"
  val testng = "org.testng" % "testng" % "5.12.1" % "test"
  val specs = "org.scala-tools.testing" % "specs" % "1.6.2.1" % "test"
  val slf4j_version = "1.5.11"
  val slf4j = "org.slf4j" % "slf4j-api" % slf4j_version
  val slf4j_log4j14 = "org.slf4j" % "slf4j-log4j12" % slf4j_version
  //val slf4j_simple = "org.slf4j" % "slf4j-simple" % slf4j_version

  val commons_httpclient = "org.apache.httpcomponents" % "httpclient" % "4.0.1"
  val lift_version = "2.0"
  val lift_json = "net.liftweb" % "lift-json" % lift_version
  val lift_util = "net.liftweb" % "lift-util" % lift_version

  val oauth = "oauth.signpost" % "signpost-commonshttp4" % "1.2"
}

