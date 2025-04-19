val scala3Version = "3.6.4"

lazy val root = project
  .in(file("."))
  .settings(
    name := "expense-bot",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalatest" %% "scalatest" % "latest.integration" % Test,
    libraryDependencies += "com.lihaoyi" %% "mainargs" % "0.7.6",
    libraryDependencies += "com.indoorvivants" %%  "toml" % "0.3.0",
    libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.49.1.0"
  )
