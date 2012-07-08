name := "Lift Clustered Comet"

version := "0.1"

organization := "com.fmpwizard"

scalaVersion := "2.9.1"

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                  "releases"  at "http://oss.sonatype.org/content/repositories/releases",
                  "twitter"   at "http://maven.twttr.com"
)

libraryDependencies ++= {
val liftVersion = "2.5-SNAPSHOT"
  Seq(
    "net.liftweb" %% "lift-actor" % liftVersion % "compile",
    "ch.qos.logback" % "logback-classic" % "0.9.26",
    "com.twitter"         %% "finagle-core" % "4.0.2",
    "com.twitter"         %% "finagle-stream" % "4.0.2",
    "com.twitter"         %% "finagle-http" % "4.0.2",
    "com.yammer.metrics"  % "metrics-core" % "2.1.2",
    "com.yammer.metrics"  % "metrics-scala_2.9.1" % "2.1.2"
  )
}



// append -deprecation to the options passed to the Scala compiler
scalacOptions += "-deprecation"


