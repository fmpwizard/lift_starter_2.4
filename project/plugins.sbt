//webplugin
resolvers += "Web plugin repo" at "http://siasia.github.com/maven2"

libraryDependencies <+= sbtVersion(v => "com.github.siasia" %% "xsbt-web-plugin" % (v+"-0.2.11"))

//Idea plugin
resolvers += "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.0.0")

resolvers += "sonatype.repo" at "https://oss.sonatype.org/content/groups/public"

addSbtPlugin("eu.getintheloop" %% "sbt-cloudbees-plugin" % "0.4.0")
