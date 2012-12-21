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
            response = ServerResponse("text/plain", "yo", 200),
            params = Map("param1" -> "toto")
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


    // TODO pattern pour le path
    // TODO plage de port dispo et retour à l'utilisateur
                // TODO si port pas dispo prend le prochain etc...

    /*//val connection = startServer(8080)

    // server(8080) et passer aussi les routes
    /**
     * routes :
     *  val route = GET {
     *          path = /rest/url            // si rien alors n'importe quel path
     *          param1 = value1
     *          param2 = value2
     *          response {                            // mandatory ??
     *              contentType = text/plain
     *              body = "Hello World !"
     *          }
     *      }
     *
     *      POST {
     *
     *      }
     */

    val route = GET (
        path = "/test",                 // pattern pour les paths
        response = ServerResponse("text/plain", "yo ça marche !!", 200),
        params = Map("param1" -> "toto", "param2" -> "tata")
    )

    new StubServer(8080, route).start		// il faut pouvoir requeter une plage de port dispo si un ne passe pas et que l'utilisateur puisse le réccup pour la conf de son code de prod
    // val stubServer = stubServer(8080, route).defaultResponse(contentType, body).start
    // stubServer.stop
    // stubServer.addRoute(route).dropRoute(route)   //restart ??? avant start ???*/
}