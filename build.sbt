val scala3Version = "3.6.4"

lazy val root = project
  .in(file("."))
  .settings(
    name := "expense-bot",
    version := "0.1.0",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalatest" %% "scalatest" % "latest.integration" % Test,
    libraryDependencies += "com.lihaoyi" %% "mainargs" % "0.7.6",
    libraryDependencies += "com.indoorvivants" %%  "toml" % "0.3.0",
    libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.49.1.0",
    libraryDependencies += "com.softwaremill.sttp.client4" %% "core" % "4.0.3",
    libraryDependencies += "readsgsheet" % "readsgsheet_3" % "0.1.0-SNAPSHOT",

    assembly / assemblyJarName := "expense-bot-" + version.value + ".jar",
    assembly / mainClass := Some("Init"),

    scalacOptions += "-deprecation"
  )
