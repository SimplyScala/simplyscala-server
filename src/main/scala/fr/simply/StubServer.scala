package fr.simply

import org.simpleframework.transport.connect.SocketConnection
import java.net.{BindException, InetSocketAddress, SocketAddress}
import util.{Text_Plain, ContentType}
import org.simpleframework.http.Request
import org.simpleframework.http.core.ContainerServer

case class ServerRoute(restVerb: RestVerb,
                       path: String,
                       response: ServerResponse,
                       params: Map[String, String] = Map())

sealed trait ServerResponse
case class StaticServerResponse(contentType: ContentType, body: String, code: Int) extends ServerResponse
case class DynamicServerResponse(response: Request => StaticServerResponse) extends ServerResponse

object GET {
    def apply(path: String, params: Map[String, String] = Map(), response: ServerResponse): ServerRoute =
        ServerRoute(Get, path, response, params)
}

object POST {
    def apply(path: String, params: Map[String, String] = Map(), response: ServerResponse): ServerRoute =
        ServerRoute(Post, path, response, params)
}

object PUT {
    def apply(path: String, params: Map[String, String] = Map(), response: ServerResponse): ServerRoute =
        ServerRoute(Put, path, response, params)
}

object DELETE {
    def apply(path: String, params: Map[String, String] = Map(), response: ServerResponse): ServerRoute =
        ServerRoute(Delete, path, response, params)
}

object HEAD {
    def apply(path: String, params: Map[String, String] = Map(), response: ServerResponse): ServerRoute =
        ServerRoute(Head, path, response, params)
}

object OPTIONS {
    def apply(path: String, params: Map[String, String] = Map(), response: ServerResponse): ServerRoute =
        ServerRoute(Options, path, response, params)
}

object TRACE {
    def apply(path: String, params: Map[String, String] = Map(), response: ServerResponse): ServerRoute =
        ServerRoute(Trace, path, response, params)
}

object PATCH {
    def apply(path: String, params: Map[String, String] = Map(), response: ServerResponse): ServerRoute =
        ServerRoute(Patch, path, response, params)
}

class StubServer(port: Int, routes: ServerRoute*) {
    private var defaultResponse = StaticServerResponse(Text_Plain, "error", 404)
    private var simplyServer: SocketConnection = _
    private var portUsed = port

    def portInUse: Int = portUsed

    def start: StubServer = {
        this.simplyServer = startServer
        this
    }

    def stop = if (simplyServer != null) simplyServer.close()

    def defaultResponse(contentType: ContentType, body: String, responseCode: Int): StubServer = {
        this.defaultResponse = StaticServerResponse(contentType, body, responseCode)
        this
    }

    private def startServer: SocketConnection = {
        val container = new SimplyScala(defaultResponse, routes.toList)
        val connection = new SocketConnection(new ContainerServer(container))

        startServerWithAvailablePort(connection, port)
    }

    private def startServerWithAvailablePort(connection: SocketConnection, port: Int): SocketConnection = {
        val address: SocketAddress = new InetSocketAddress(port)

        try {
            connection.connect(address)
            portUsed = port
        } catch {
            case e: BindException => startServerWithAvailablePort(connection, port + 1)
        }

        connection
    }
}