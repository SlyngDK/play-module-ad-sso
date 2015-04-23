name := "sample"

version := "1.2"

lazy val root = (project in file(".")).enablePlugins(PlayJava).dependsOn(dep)

lazy val dep = file("..")

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "dk.slyng.play.module" % "play-module-ad-sso_2.11" % "0.2.0"
)