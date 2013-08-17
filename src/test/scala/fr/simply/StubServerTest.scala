package fr.simply

import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.matchers.ShouldMatchers
import com.jayway.restassured.RestAssured
import org.hamcrest.Matchers._
import util.{ContentType, Text_Plain}
import org.simpleframework.http.Request
import fr.simply.fixture.StubServerFixture

class StubServerTest extends FunSuite with ShouldMatchers with BeforeAndAfter with StubServerFixture {

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
                .get("http://localhost:8080/test?param1=toto")
    }

    test("[GET] simple GET request with one param") {
        val route = GET (
            path = "/test",                 // pattern pour les paths
            params = Map("param1" -> "toto"),
            response = StaticServerResponse(Text_Plain, "yo", 200)
        )

        server = new StubServer(8080, route).start

        RestAssured
            .expect()
                .statusCode(200)
                .content(containsString("yo"))
            .when()
                .get("http://localhost:8080/test?param1=toto")
    }

    test("[GET] test pattern uses for path route") {
        val route = GET (
            path = "/test*",
            response = StaticServerResponse(Text_Plain, "yo", 200)
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
            response = StaticServerResponse(Text_Plain, "yo", 200)
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
        StaticServerResponse(ContentType("text/plain"), "yo", 200).contentType.toString should be ("text/plain")
    }

    test("[GET] dynamic server response") {
        val dynamicResponse: Request => StaticServerResponse = {
            request =>
                println("I use dynamic code !!!")
                StaticServerResponse(Text_Plain, "OK dynamic", 200)
        }

        val route = GET (
            path = "/test",
            response = DynamicServerResponse(dynamicResponse)
        )

        server = new StubServer(8080, route).start

        RestAssured
            .expect()
                .statusCode(200)
                .content(containsString("OK dynamic"))
            .when()
                .get("http://localhost:%s/test".format(server.portInUse))
    }

    test("[PUT] simple request") {
        val route = PUT (
            path = "/test",
            response = StaticServerResponse(Text_Plain, "OK PUT verb", 200),
            params = Map("toto" -> "titi")
        )

        withStubServerFixture(8080, route) { server =>
            RestAssured
                .given()
                    .parameter("toto", "titi")
                .expect()
                    .statusCode(200)
                    .content(containsString("OK PUT verb"))
                .when()
                    .put(s"http://localhost:${server.portInUse}/test")
        }
    }

    test("[DELETE] simple request") {
        val route = DELETE (
            path = "/test",
            response = StaticServerResponse(Text_Plain, "OK DELETE verb", 200),
            params = Map("toto" -> "titi")
        )

        withStubServerFixture(8080, route) { server =>
            RestAssured
                .given()
                    .parameter("toto", "titi")
                .expect()
                    .statusCode(200)
                    .content(containsString("OK DELETE verb"))
                .when()
                    .delete(s"http://localhost:${server.portInUse}/test")
        }
    }

    // Be carreful HEAD verb return only the same header response than GET verb, but non body response
    // http://www.pragmaticapi.com/2013/02/14/restful-patterns-for-the-head-verb/
    test("[HEAD] simple request") {
        val route = HEAD (
            path = "/test",
            response = StaticServerResponse(Text_Plain, "", 200),
            params = Map("toto" -> "titi")
        )

        withStubServerFixture(8080, route) { server =>
            RestAssured
                .given()
                    .parameter("toto", "titi")
                .expect()
                    .statusCode(200)
                .when()
                    .head(s"http://localhost:${server.portInUse}/test")
        }
    }

    test("[OPTIONS] simple request") {
        val route = OPTIONS (
            path = "/test",
            response = StaticServerResponse(Text_Plain, "OK OPTIONS verb", 200),
            params = Map("toto" -> "titi")
        )

        withStubServerFixture(8080, route) { server =>
            RestAssured
                .given()
                    .parameter("toto", "titi")
                .expect()
                    .statusCode(200)
                    .content(containsString("OK OPTIONS verb"))
                .when()
                    .options(s"http://localhost:${server.portInUse}/test")
        }
    }

    ignore("[TRACE] simple request") {   // TODO test trace
        val route = TRACE (
            path = "/test",
            response = StaticServerResponse(Text_Plain, "OK TRACE verb", 200),
            params = Map("toto" -> "titi")
        )

        withStubServerFixture(8080, route) { server =>
            RestAssured
                .given()
                    .parameter("toto", "titi")
                .expect()
                    .statusCode(200)
                    .content(containsString("OK TRACE verb"))
                .when()
                    //.trace(s"http://localhost:${server.portInUse}/test")
        }
    }

    test("[PATCH] simple request") {
        val route = PATCH (
            path = "/test",
            response = StaticServerResponse(Text_Plain, "OK PATCH verb", 200),
            params = Map("toto" -> "titi")
        )

        withStubServerFixture(8080, route) { server =>
            RestAssured
                .given()
                  .parameter("toto", "titi")
                .expect()
                    .statusCode(200)
                    .content(containsString("OK PATCH verb"))
                .when()
                    .patch(s"http://localhost:${server.portInUse}/test")
        }
    }

    test("[HEAD] add params in headers response") {
        val route = HEAD (
            path="/test",
            response = StaticServerResponse(Text_Plain, "", 200, Map("headerName" -> "value", "one" -> "two"))
        )

        withStubServerFixture(8080, route) { server =>
            RestAssured
                .given()
                   .parameter("toto", "titi")
                .expect()
                    .statusCode(200)
                    .header("headerName", "value")
                    .header("one", "two")
                    .when()
                .head(s"http://localhost:${server.portInUse}/test")
        }
    }
}