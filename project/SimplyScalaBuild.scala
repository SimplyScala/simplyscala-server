import sbt._
import sbt.Keys._

object SimplyScalaBuild extends Build {

    lazy val root = Project(id = "simplyScala", base = file("."),
        settings = Project.defaultSettings ++ Seq(
            name := "simplyScala",

            version := "0.2-SNAPSHOT",

            scalaVersion := "2.9.2",

            crossScalaVersions := Seq("2.9.0", "2.9.1", "2.9.2"),

            libraryDependencies ++= Seq(
                "org.simpleframework" % "simple" % "4.1.21",

                "org.scalatest" %% "scalatest" % "1.8" % "test",
                "net.sourceforge.jwebunit" % "jwebunit-core" % "3.1" % "test",
                "net.sourceforge.jwebunit" % "jwebunit-htmlunit-plugin" % "3.1" % "test",
                "com.jayway.restassured" % "rest-assured" % "1.7.2" % "test"
            )
        )
    )
}