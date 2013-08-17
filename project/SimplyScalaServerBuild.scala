import sbt._
import sbt.Keys._

object SimplyScalaServerBuild extends Build {

    // make library => 'sbt + package' & 'sbt + make-pom' & 'sbt + publish'

    lazy val root = Project(id = "simplyscala-server", base = file("."),
        settings = Project.defaultSettings ++ Seq(
            name := "simplyscala-server",
            organization := "com.github.simplyscala",
            description := "provides a fast & ultra-lightweight http server with an API dedicated to define server’s " +
                "routes (request-response), start & stop server",

            version := "0.6-SNAPSHOT",

            scalaVersion := "2.10.2",

            crossScalaVersions := Seq("2.10.0", "2.10.1", "2.10.2"),

            libraryDependencies ++= Seq(
                "org.simpleframework" % "simple" % "5.1.5",

                "org.scalatest" %% "scalatest" % "1.9.1" % "test",
                "com.jayway.restassured" % "rest-assured" % "1.7.2" % "test"
            ),

            publishMavenStyle := true,
            publishArtifact in Test := false,
            pomIncludeRepository := { _ => false },

            pomExtra := (
                <url>https://github.com/SimplyScala/simplyscala-server</url>
                <licenses>
                    <license>
                        <name>GPLv3</name>
                        <url>http://www.gnu.org/licenses/gpl-3.0.html</url>
                        <distribution>repo</distribution>
                    </license>
                </licenses>
                <scm>
                    <url>git@github.com:SimplyScala/simplyscala-server.git</url>
                    <connection>scm:git:git@github.com:SimplyScala/simplyscala-server.git</connection>
                </scm>
                <developers>
                    <developer>
                        <id>ugobourdon</id>
                        <name>bourdon.ugo@gmail.com</name>
                        <url>https://github.com/ubourdon</url>
                    </developer>
                </developers>
            ),

            publishTo <<= version { v: String =>
                val nexus = "https://oss.sonatype.org/"
                if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
                else                             Some("releases" at nexus + "service/local/staging/deploy/maven2")
            }
        )
    )
}