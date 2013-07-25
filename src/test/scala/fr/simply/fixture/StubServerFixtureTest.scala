package fr.simply.fixture

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import fr.simply.{StaticServerResponse, GET}
import fr.simply.util.Text_Plain
import com.jayway.restassured.RestAssured
import org.hamcrest.Matchers._

class StubServerFixtureTest extends FunSuite with ShouldMatchers with StubServerFixture {

    test("test stub server fixture") {
        val route = GET (
            path = "/test",
            params = Map("param1" -> "toto"),
            response = StaticServerResponse(Text_Plain, "yo", 200)
        )

        withStubServerFixture(8080, route) { server =>
            RestAssured
                .given()
                    .parameters("param1", "toto")
                .expect()
                    .statusCode(200)
                    .content(containsString("yo"))
                .when()
                    .get("http://localhost:%s/test".format(server.portInUse))
        }
    }

    test("test stub server fixture with 2nd tests") {
        val route = GET (
            path = "/test",
            params = Map("param1" -> "titi"),
            response = StaticServerResponse(Text_Plain, "ya", 200)
        )

        withStubServerFixture(8080, route) { server =>
            RestAssured
                .given()
                    .parameters("param1", "titi")
                .expect()
                    .statusCode(200)
                .   content(containsString("ya"))
                .when()
                    .get("http://localhost:%s/test".format(server.portInUse))
        }
    }
}