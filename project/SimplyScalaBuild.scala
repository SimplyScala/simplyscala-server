import sbt._
import Keys._

object SimplyScalaBuild extends Build {
    name := "simplyScala"

    version := "0.1-SNAPSHOT"

    scalaVersion := "2.9.2"

    lazy val root = Project(id = "simplyScala", base = file("."),
        settings = Project.defaultSettings ++ Seq(
            libraryDependencies ++= Seq(
                "org.simpleframework" % "simple" % "4.1.21",

                "org.scalatest" %% "scalatest" % "1.8" % "test",
                "net.sourceforge.jwebunit" % "jwebunit-core" % "3.1" % "test",
                "net.sourceforge.jwebunit" % "jwebunit-htmlunit-plugin" % "3.1" % "test",
                "com.jayway.restassured" % "rest-assured" % "1.7.2" % "test"
            )
        )
    )

    /*resolvers += Resolver.url("artifactory", url("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

    addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.8.5")*/
}