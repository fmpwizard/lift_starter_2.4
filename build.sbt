name := "starter"

version := "0.1"

organization := "com.fmpwizard"

scalaVersion := "2.9.1"

seq(webSettings :_*)

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                  "releases"  at "http://oss.sonatype.org/content/repositories/releases",
                  "twitter"   at "http://maven.twttr.com"
                  )


libraryDependencies ++= {
val liftVersion = "2.5-SNAPSHOT"
val vers = "0.8.7"
  Seq(
      "net.liftweb"         %% "lift-webkit" % liftVersion % "compile",
      "org.eclipse.jetty"   %  "jetty-webapp" % "8.0.1.v20110908" % "container",
      "ch.qos.logback"      %  "logback-classic" % "0.9.26",
      "org.scalaz"          %% "scalaz-core" % "6.0.4",
      "com.twitter"         %% "finagle-core" % "4.0.2",
      "com.twitter"         %% "finagle-stream" % "4.0.2",
      "com.twitter"         %% "finagle-http" % "4.0.2",
      "com.twitter"         %  "joauth" % "3.0.0",
      "net.databinder"      %% "dispatch-core" % vers,
      "net.databinder"      %% "dispatch-oauth" % vers,
      "net.databinder"      %% "dispatch-nio" % vers,
      "net.databinder"      %% "dispatch-json" % vers intransitive(),
      "com.yammer.metrics"  % "metrics-core" % "2.1.2",
      "com.yammer.metrics"  % "metrics-scala_2.9.1" % "2.1.2"
  )
}



// append -deprecation to the options passed to the Scala compiler
scalacOptions += "-deprecation"


