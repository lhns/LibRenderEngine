inThisBuild(Seq(
  name := "renderengine",
  organization := "de.lolhens",

  scalaVersion := "2.12.8",

  resolvers ++= Seq(
    "lolhens-maven" at "http://artifactory.lolhens.de/artifactory/maven-public/",
    Resolver.url("lolhens-ivy", url("http://artifactory.lolhens.de/artifactory/ivy-public/"))(Resolver.ivyStylePatterns)
  ),

  scalacOptions ++= Seq(
    "–encoding", "UTF-8",
    "-Xmax-classfile-name", "127"
  ),

  javacOptions ++= Seq("-encoding", "UTF-8")
))

name := (ThisBuild / name).value

def settings: Seq[SettingsDefinition] = Seq(
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.2.4"),

  assembly / assemblyJarName := s"${name.value}-${version.value}.sh.bat",

  assembly / assemblyMergeStrategy := {
    case PathList("module-info.class") =>
      MergeStrategy.discard

    case PathList("META-INF", "io.netty.versions.properties") =>
      MergeStrategy.first

    case x =>
      val oldStrategy = (assembly / assemblyMergeStrategy).value
      oldStrategy(x)
  }
)

lazy val renderengine = project.in(file("."))
  .settings(settings: _*)
  .settings(
    name := "renderengine",
    version := "0.0.0",

    libraryDependencies ++= Seq(
      "org.scodec" %% "scodec-bits" % "1.1.6",
      "org.jogamp.jogl" % "jogl-all" % "2.3.2",
      "org.jogamp.jogl" % "jogl-all" % "2.3.2" classifier "natives-windows-amd64",
      "org.jogamp.jogl" % "jogl-all" % "2.3.2" classifier "natives-windows-i586",
      "org.jogamp.jogl" % "jogl-all" % "2.3.2" classifier "natives-linux-amd64",
      "org.jogamp.jogl" % "jogl-all" % "2.3.2" classifier "natives-linux-i586",
      "org.jogamp.jogl" % "jogl-all" % "2.3.2" classifier "natives-macosx-universal",
      "org.jogamp.gluegen" % "gluegen-rt" % "2.3.2",
      "org.jogamp.gluegen" % "gluegen-rt" % "2.3.2" classifier "natives-windows-amd64",
      "org.jogamp.gluegen" % "gluegen-rt" % "2.3.2" classifier "natives-windows-i586",
      "org.jogamp.gluegen" % "gluegen-rt" % "2.3.2" classifier "natives-linux-amd64",
      "org.jogamp.gluegen" % "gluegen-rt" % "2.3.2" classifier "natives-linux-i586",
      "org.jogamp.gluegen" % "gluegen-rt" % "2.3.2" classifier "natives-macosx-universal",
      "ar.com.hjg" % "pngj" % "2.1.0"
      /*"org.lwjgl" % "lwjgl-opengl" % "3.2.1",
      "org.lwjgl" % "lwjgl-opengl" % "3.2.1" classifier "natives-windows",
      "org.lwjgl" % "lwjgl-opengl" % "3.2.1" classifier "natives-linux",
      "org.lwjgl" % "lwjgl-opengl" % "3.2.1" classifier "natives-macos"*/
    ),

    assembly / assemblyOption := (assembly / assemblyOption).value
      .copy(prependShellScript = Some(defaultUniversalScript(shebang = false)))
  )

def osName: String = System.getProperty("os.name") match {
  case name if name.startsWith("Linux") => "linux"
  case name if name.startsWith("Mac") => "macos"
  case name if name.startsWith("Windows") => "windows"
  case _ => throw new Exception("Unknown platform!")
}

def universalScript(shellCommands: String,
                    cmdCommands: String,
                    shebang: Boolean): String = {
  Seq(
    if (shebang) "#!/usr/bin/env sh" else "",
    "@ 2>/dev/null # 2>nul & echo off & goto BOF\r",
    ":",
    shellCommands.replaceAll("\r\n|\n", "\n"),
    "exit",
    Seq(
      "",
      ":BOF",
      cmdCommands.replaceAll("\r\n|\n", "\r\n"),
      "exit /B %errorlevel%",
      ""
    ).mkString("\r\n")
  ).filterNot(_.isEmpty).mkString("\n")
}

def defaultUniversalScript(shebang: Boolean,
                           javaOpts: Seq[String] = Seq.empty,
                           javaw: Boolean = false): Seq[String] = {
  val javaOptsString = javaOpts.map(_ + " ").mkString
  Seq(universalScript(
    shellCommands = {
      def javaCommand(args: String): String =
        s"exec java $args"

      "if [ -n \"$JAVA_HOME\" ]; then PATH=\"$JAVA_HOME/bin:$PATH\"; fi\n" +
        javaCommand(s"""-jar $javaOptsString$$JAVA_OPTS "$$0" "$$@"""")
    },
    cmdCommands = {
      def javaCommand(args: String): String =
        if (javaw)
          s"""start "" javaw $args"""
        else
          s"java $args"

      "if not \"%JAVA_HOME%\"==\"\" set \"PATH=%JAVA_HOME%\\bin;%PATH%\"\n" +
        javaCommand(s"""-jar $javaOptsString%JAVA_OPTS% "%~dpnx0" %*""")
    },
    shebang = shebang
  ))
}
