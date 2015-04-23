name := "Play-Module-AD-SSO"

version := "0.2.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "net.jodah" % "expiringmap" % "0.4.2"
)

organization := "dk.slyng.play.module"

organizationName := "SlyngDK"

organizationHomepage := Some(new URL("http://slyng.dk"))