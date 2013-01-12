import AssemblyKeys._

name := "raspberry-gpio"

version := "0.1"

organization := "com.fmpwizard"

scalaVersion := "2.9.1"

seq(webSettings :_*)

assemblySettings

mainClass in assembly := Some("com.fmpwizard.jetty.Main")

resourceGenerators in Compile <+= (resourceManaged, baseDirectory) map
  { (managedBase, base) =>
    val webappBase = base / "src" / "main" / "webapp"
    for {
      (from, to) <- webappBase ** "*" x rebase(webappBase, managedBase /
        "main" / "webapp")
    } yield {
      Sync.copy(from, to)
      to
    }
  }


resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                  "releases"  at "http://oss.sonatype.org/content/repositories/releases")

libraryDependencies ++= {
val liftVersion = "2.5-SNAPSHOT"
  Seq(
  "net.liftweb"       %% "lift-webkit" % liftVersion % "compile",
  "javax.servlet"     % "servlet-api"  % "2.5" % "provided",
  "org.eclipse.jetty" % "jetty-webapp" % "8.1.0.v20120127" % "container, compile",
  "org.eclipse.jetty" % "jetty-plus"   % "8.1.0.v20120127" % "container, compile",
  "ch.qos.logback"    % "logback-classic" % "0.9.26",
  "net.liftmodules"   %% "fobo"        % (liftVersion+"-0.7.7-SNAPSHOT"),
  "com.pi4j"          % "pi4j-core"    % "0.0.5-SNAPSHOT" % "compile",
  "com.pi4j"          % "pi4j-device" % "0.0.5-SNAPSHOT" % "compile"
  )
}

// append -deprecation to the options passed to the Scala compiler
scalacOptions += "-deprecation"


