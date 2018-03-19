lazy val root = (project in file("."))
  .settings(
    name         := "Deck Builder",
    scalaVersion := "2.12.4",
    version      := "0.0.2"
  )
  .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)

scalaJSUseMainModuleInitializer := true

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.2",
  "com.github.japgolly.scalajs-react" %%% "core" % "1.2.0",
  "com.github.japgolly.scalacss" %%% "core" % "0.5.3",
  "com.github.japgolly.scalacss" %%% "ext-react" % "0.5.3"
)

npmDependencies in Compile ++= Seq(
    "react" -> "16.0.4",
    "react-dom" -> "16.0.4")
