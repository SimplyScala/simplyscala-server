package fr.simply

import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.matchers.ShouldMatchers
import net.sourceforge.jwebunit.junit.JWebUnit._
import com.jayway.restassured.RestAssured
import org.hamcrest.Matchers._
import util.{ContentType, Text_Plain}

class StubServerTest extends FunSuite with ShouldMatchers with BeforeAndAfter {

    var server: StubServer = _

    after { if (server != null) server.stop }

    test("test server default response") {
        server = new StubServer(8080).start

        RestAssured
            .expect()
                .statusCode(404)
                .content(containsString("error"))
            .when()
                .get("http://localhost:8080/")
    }

    test("test user default response") {
        server = new StubServer(8080).defaultResponse(Text_Plain, "default", 400).start

        RestAssured
            .expect()
                .statusCode(400)
                .content(containsString("default"))
            .when()
                .get("http://localhost:8080/")
    }

    test("[GET] simple GET request with one param") {
        val route = GET (
            path = "/test",                 // pattern pour les paths
            params = Map("param1" -> "toto"),
            response = ServerResponse(Text_Plain, "yo", 200)
        )

        server = new StubServer(8080, route).start

        setBaseUrl("http://localhost:8080")
        beginAt("test?param1=toto")

        assertResponseCode(200)
        assertTextPresent("yo")
    }

    test("[GET] test pattern uses for path route") {
        val route = GET (
            path = "/test*",
            response = ServerResponse(Text_Plain, "yo", 200)
        )

        server = new StubServer(8080, route).start

        RestAssured
            .expect()
                .statusCode(200)
                .content(containsString("yo"))
            .when()
                .get("http://localhost:8080/testMe")
    }

    test("use available port to start server") {
        server = new StubServer(8080).start
        server.portInUse should be (8080)

        val otherServer = new StubServer(8080).start
        otherServer.portInUse should be (8081)
        otherServer.stop
    }

    test("[POST] simple post request with 1 param") {
        val route = POST (
            path = "/test",
            params = Map("param1" -> "toto"),
            response = ServerResponse(Text_Plain, "yo", 200)
        )

        server = new StubServer(8080, route).start

        RestAssured
            .given()
                .parameters("param1", "toto")
            .expect()
                .statusCode(200)
                .content(containsString("yo"))
            .when()
                .post("http://localhost:8080/test")
    }

    test("content type builder") {
        ServerResponse(ContentType("text/plain"), "yo", 200).contentType.toString should be ("text/plain")
    }
}