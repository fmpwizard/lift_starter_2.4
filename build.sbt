name := "starter"

version := "0.1"

organization := "com.fmpwizard"

scalaVersion := "2.9.1"

seq(webSettings :_*)

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= {
val liftVersion = "2.4"
  Seq(
  "net.liftweb" %% "lift-webkit" % liftVersion % "compile",
  "org.eclipse.jetty" % "jetty-webapp" % "8.0.1.v20110908" % "container",
  "com.h2database" % "h2" % "1.2.138"
  )
}



// append -deprecation to the options passed to the Scala compiler
scalacOptions += "-deprecation"


