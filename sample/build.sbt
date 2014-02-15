import play.Project._

name := "sample"

version := "1.0"

libraryDependencies ++= Seq(
  "dk.slyng.play.module" % "play-module-ad-sso_2.10" % "1.0"
)

playJavaSettings