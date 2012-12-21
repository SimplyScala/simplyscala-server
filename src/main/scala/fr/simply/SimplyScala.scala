package fr.simply

import org.simpleframework.http.core.Container
import org.simpleframework.http.Response
import org.simpleframework.http.Request

object SimplyScala extends App {
    //val connection = startServer(8080)

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

    new StubServer(8080/*, route*/).start		// il faut pouvoir requeter une plage de port dispo si un ne passe pas et que l'utilisateur puisse le réccup pour la conf de son code de prod
    // val stubServer = stubServer(8080, route).defaultResponse(contentType, body).start
    // stubServer.stop
    // stubServer.addRoute(route).dropRoute(route)   //restart ??? avant start ???
}

class SimplyScala(defaultResponse: ServerResponse, routes: List[ServerRoute]) extends Container {

    def handle(request: Request, response: Response) {
        val time = System.currentTimeMillis

        response.set("Server", "SimplyScala/1.0 (Simple 4.0)")
        response.setDate("Date", time)
        response.setDate("Last-Modified", time)

        if(requestMatchWithRoute(request, response, routes)) println("one route match with request")
        else defaultReponse(response, request)

        response.getPrintStream.close
        response.close()
    }

    private def requestMatchWithRoute(request: Request, response: Response, routes: List[ServerRoute]): Boolean = {
        routes.exists {
            route =>
                if(testRoute(request, route)) {
                    response.set("Content-Type", route.response.contentType)
                    response.setCode(route.response.code)
                    response.getPrintStream.println(route.response.body)
                    true
                } else false
        }
    }

    private def defaultReponse(response: Response, request: Request) {
        println("defaultResponse")
        response.set("Content-Type", defaultResponse.contentType)
        response.setCode(defaultResponse.code)
        response.getPrintStream.println(defaultResponse.body)
        /*response.getPrintStream.println("names : " + request.getNames)
        response.getPrintStream.println("attributes : " + request.getAttributes)
        response.getPrintStream.println("parameters : " + request.getParameter("param1"))
        response.getPrintStream.println("request path : " + request.getPath.getPath)
        response.getPrintStream.println("request verb : " + request.getMethod + "\n")*/

        /*response.getPrintStream.println("verb : " + routes.head.restVerb)
        response.getPrintStream.println("path : " + routes.head.path)*/
    }

    private def testRoute(request: Request, route: ServerRoute): Boolean = {
        request.getMethod.equalsIgnoreCase(route.restVerb.toString) &&
        request.getPath.getPath == route.path &&
        testParams(request, route.params)
    }

    private def testParams(request: Request, params: Map[String,String]): Boolean =
        params.forall { case (key,value) => request.getParameter(key) != null && request.getParameter(key) == value }
}