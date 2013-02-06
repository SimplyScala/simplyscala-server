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

            version := "0.3-SNAPSHOT",

            scalaVersion := "2.9.2",

            crossScalaVersions := Seq("2.9.0", "2.9.1", "2.9.2"),

            libraryDependencies ++= Seq(
                "org.simpleframework" % "simple" % "4.1.21",

                "org.scalatest" %% "scalatest" % "1.8" % "test",
                "net.sourceforge.jwebunit" % "jwebunit-core" % "3.1" % "test",
                "net.sourceforge.jwebunit" % "jwebunit-htmlunit-plugin" % "3.1" % "test",
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