package fr.simply

import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.matchers.ShouldMatchers
import net.sourceforge.jwebunit.junit.JWebUnit._
import com.jayway.restassured.RestAssured
import org.hamcrest.Matchers._

class StubServerTest extends FunSuite with ShouldMatchers with BeforeAndAfter {

    var server: StubServer = _

    after { if (server != null) server.stop }

    test("[GET] test server default response") {
        server = new StubServer(8080).start

        RestAssured
            .expect()
                .statusCode(404)
                .body(containsString("error"))
            .when()
                .get("http://localhost:8080/")
    }

    test("[GET] test user default response") {
        server = new StubServer(8080).defaultResponse("text/plain", "default", 400).start

        RestAssured
            .expect()
                .statusCode(400)
                .body(containsString("default"))
            .when()
                .get("http://localhost:8080/")
    }

    test("[GET] simple GET request with one param") {
        val route = GET (
            path = "/test",                 // pattern pour les paths
            params = Map("param1" -> "toto"),
            response = ServerResponse("text/plain", "yo", 200)
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
            response = ServerResponse("text/plain", "yo", 200)
        )

        server = new StubServer(8080, route).start

        RestAssured
            .expect()
                .statusCode(200)
                .body(containsString("yo"))
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
}