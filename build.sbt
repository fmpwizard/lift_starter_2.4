name := "starter"

version := "0.2"

organization := "com.fmpwizard"

scalaVersion := "2.10.0"

seq(webSettings :_*)

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                  "releases"  at "http://oss.sonatype.org/content/repositories/releases")

libraryDependencies ++= {
val liftVersion = "2.5-M4"
  Seq(
  "net.liftweb"       %% "lift-webkit"    % liftVersion       % "compile",
  "net.liftmodules"   %% "fobo"           % (liftVersion+"-0.8.0-SNAPSHOT"),
  "org.specs2"        %% "specs2"         % "1.10"            % "test",
  "org.eclipse.jetty" % "jetty-webapp"    % "8.0.1.v20110908" % "container",
  "com.h2database"    % "h2"              % "1.2.138",
  "ch.qos.logback"    % "logback-classic" % "0.9.26"
  )
}



// append -deprecation to the options passed to the Scala compiler
scalacOptions += "-deprecation"


