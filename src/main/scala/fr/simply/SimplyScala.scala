package fr.simply

import org.simpleframework.http.core.Container
import org.simpleframework.http.Response
import org.simpleframework.http.Request
import org.simpleframework.transport.Server
import java.nio.charset.Charset

class SimplyScala(defaultResponse: StaticServerResponse, routes: List[ServerRoute]) extends Container {

    def handle(request: Request, response: Response) {
        val time = System.currentTimeMillis

        response.setValue("Server", "SimplyScalaServer/1.0 (Simple 4.0)")
        response.setDate("Date", time)
        response.setDate("Last-Modified", time)

        if(requestMatchWithRoute(request, response, routes)) println("one route match with request")
        else defaultReponse(response, request)

        response.getPrintStream.close()
        response.close()
    }

    private def requestMatchWithRoute(request: Request, response: Response, routes: List[ServerRoute]): Boolean = {
        routes.exists {
            route =>
                route.response match {
                    case staticResponse: StaticServerResponse => makeStaticResponse(request, response, route)
                    case dynamicResponse: DynamicServerResponse => makeDynamicResponse(request, response, route)
                }
        }
    }

    private def defaultReponse(response: Response, request: Request) {
        println(s"defaultResponse from request : ${request.getMethod} - ${request.getAddress}")

        response.setValue("Content-Type", defaultResponse.contentType.toString)
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

    private def makeStaticResponse(request: Request, response: Response, route: ServerRoute): Boolean = {
        if(testRoute(request, route)) {
            val staticResponse = route.response.asInstanceOf[StaticServerResponse]
            makeResponse(response, staticResponse)
            true
        } else false
    }

    private def makeDynamicResponse(request: Request, response: Response, route: ServerRoute): Boolean = {
        if(testRoute(request, route)) {
            val dynamicResponse = route.response.asInstanceOf[DynamicServerResponse].response(request)
            makeResponse(response, dynamicResponse)
            true
        } else false
    }

    private def testRoute(request: Request, route: ServerRoute): Boolean = {
        request.getMethod.equalsIgnoreCase(route.restVerb.toString) &&
        testPath(request, route) &&
        testParams(request, route.params)
    }

    private def makeResponse(response: Response, serverResponse: StaticServerResponse) {
        response.setContentType(serverResponse.contentType.toString)
        response.setCode(serverResponse.code)
        serverResponse.headers.foreach { case (k,v) => response.setValue(k,v) }

        response.getPrintStream.write(serverResponse.body.getBytes(Charset.forName("UTF-8")))
    }

    private def testPath(request: Request, route: ServerRoute): Boolean = {
        val routePath = route.path
        if (routePath.startsWith("*") || routePath.endsWith("*") ) request.getPath.getPath contains routePath.replaceAll("\\*", "")
        else request.getPath.getPath == route.path
    }

    private def testParams(request: Request, params: Map[String,String]): Boolean =
        params.forall { case (key,value) => request.getParameter(key) != null && request.getParameter(key) == value }
}