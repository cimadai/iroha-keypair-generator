name := "iroha-keypair-generator"

val APP_VERSION = "0.0.1-SNAPSHOT"

val PROJECT_SCALA_VERSION = "2.11.11"

version := APP_VERSION

scalaVersion := PROJECT_SCALA_VERSION

useGpg in GlobalScope := true

lazy val libraries = Seq(
  "org.hyperledger" %% "iroha-scala" % "0.95-SNAPSHOT",
  "com.github.scopt" %% "scopt" % "3.7.0"
)

lazy val settings = Seq(
  organization := "net.cimadai",
  version := APP_VERSION,
  scalaVersion := PROJECT_SCALA_VERSION,
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-encoding", "UTF-8"),
  javaOptions ++= Seq("-Xmx1G"),
  scalacOptions ++= Seq(
    "-target:jvm-1.8",
    "-encoding", "UTF-8",
    "-unchecked",
    "-deprecation",
    "-Xfuture",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Ywarn-unused"
  ),
  libraryDependencies ++= libraries,

  fork in Test := true,

  publishMavenStyle := true,

  publishArtifact in Test := false,

  pomIncludeRepository := { _ => false },

  publishTo <<= version { (v: String) =>
    val nexus = "https://oss.sonatype.org/"
    if (v.trim.endsWith("SNAPSHOT"))
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  }



)

lazy val irohaScala = (project in file("."))
  .settings(settings: _*)
  .settings(name := "iroha-scala")
  .settings(
    mainClass in assembly := Some("net.cimadai.iroha.EntryPoint"),
    assemblyMergeStrategy in assembly := {
      case "META-INF/io.netty.versions.properties" => MergeStrategy.concat
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
  )
