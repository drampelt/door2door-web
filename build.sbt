name := """door2door"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  javaCore,
  evolutions,
  "net.sf.flexjson" % "flexjson" % "3.3",
  "org.mindrot" % "jbcrypt" % "0.3m"
)

resolvers ++= Seq(
  "Apache" at "http://repo1.maven.org/maven2/",
  "Sonatype OSS Snasphots" at "http://oss.sonatype.org/content/repositories/snapshots"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
