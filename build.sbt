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
  Seq(
      "net.liftweb"         %% "lift-webkit" % liftVersion % "compile",
      "org.eclipse.jetty"   %  "jetty-webapp" % "8.0.1.v20110908" % "container",
      "com.h2database"      %  "h2" % "1.2.138",
      "org.specs2"          %% "specs2" % "1.10" % "test",
      "ch.qos.logback"      %  "logback-classic" % "0.9.26",
      "org.scalaz"          %% "scalaz-core" % "6.0.4",
      "com.twitter"         %% "finagle-core" % "4.0.2",
      "com.twitter"         %% "finagle-stream" % "4.0.2",
      "com.twitter"         %% "finagle-http" % "4.0.2",
      "com.twitter"         %  "joauth" % "3.0.0",
      "com.twitter"         %% "util" % "3.0.0" excludeAll(
        ExclusionRule(organization = "com.sun.jdmk"),
        ExclusionRule(organization = "com.sun.jmx"),
        ExclusionRule(organization = "javax.jms")
    )
  )
}



// append -deprecation to the options passed to the Scala compiler
scalacOptions += "-deprecation"


