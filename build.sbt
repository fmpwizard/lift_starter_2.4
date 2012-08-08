name := "starter"

version := "0.1"

organization := "com.fmpwizard"

scalaVersion := "2.9.1"

seq(webSettings :_*)

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                  "releases"  at "http://oss.sonatype.org/content/repositories/releases")

resolvers += "Media4u101 SNAPSHOT Repository" at "http://www.media4u101.se:8081/nexus/content/repositories/snapshots/"

resolvers += "Media4u101 Repository" at "http://www.media4u101.se:8081/nexus/content/repositories/releases/"

libraryDependencies ++= {
val liftVersion = "2.5-SNAPSHOT"
  Seq(
  "net.liftweb" %% "lift-webkit" % liftVersion % "compile",
  "org.eclipse.jetty" % "jetty-webapp" % "8.0.1.v20110908" % "container",
  "com.h2database" % "h2" % "1.2.138",
  "org.specs2" %% "specs2" % "1.10" % "test",
  "ch.qos.logback" % "logback-classic" % "0.9.26",
  "net.liftmodules" %% "fobo" % (liftVersion+"-0.5.2-SNAPSHOT")
  )
}



// append -deprecation to the options passed to the Scala compiler
scalacOptions += "-deprecation"


