import sbt._

class TwitterTrl8Project(info: ProjectInfo) extends DefaultProject(info) {
  val mavenLocal = "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"
 // val junit = "junit" % "junit" % "4.7" % "test"
  //val testng = "org.testng" % "testng" % "5.8" % "test"
/*
  val slf4j_version = "1.6.0"
  val slf4j = "org.slf4j" % "slf4j-api" % slf4j_version % "runtime"
  val slf4j_simple = "org.slf4j" % "slf4j-simple" % slf4j_version % "runtime"
  */
//  val slf4j_log4j = "org.slf4j" % "slf4j-log4j" % slf4j_version % "runtime"
}

