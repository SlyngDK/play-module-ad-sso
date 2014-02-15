import play.Project._

name := "sample"

version := "1.0"

libraryDependencies ++= Seq(
  "dk.slyng.play.module" % "play-module-ad-sso_2.10" % "0.1.0"
)

resolvers += Resolver.url("Play Module AD SSO Repository", url("http://SlyngDK.github.com/releases/"))(Resolver.ivyStylePatterns)


playJavaSettings