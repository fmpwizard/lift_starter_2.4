//webplugin
resolvers += "Web plugin repo" at "http://siasia.github.com/maven2"

libraryDependencies <+= sbtVersion(v => v match {
  case "0.11.0" => "com.github.siasia" %% "xsbt-web-plugin" % "0.11.0-0.2.8"
  case "0.11.1" => "com.github.siasia" %% "xsbt-web-plugin" % "0.11.1-0.2.10"
  case "0.11.2" => "com.github.siasia" %% "xsbt-web-plugin" % "0.11.2-0.2.10"
  case "0.11.3" => "com.github.siasia" %% "xsbt-web-plugin" % "0.11.3-0.2.11.1"
  case "0.12.0" => "com.github.siasia" %% "xsbt-web-plugin" % "0.12.0-0.2.11.1"
  case "0.12.1" => "com.github.siasia" %% "xsbt-web-plugin" % "0.12.0-0.2.11.1"
  case _ => "com.github.siasia" %% "xsbt-web-plugin" % "0.12.0-0.2.11.1"
})

//Idea plugin
resolvers += "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.4.0")
