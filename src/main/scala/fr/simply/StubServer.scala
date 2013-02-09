package fr.simply

import org.simpleframework.transport.connect.SocketConnection
import java.net.{BindException, InetSocketAddress, SocketAddress}
import util.{Text_Plain, ContentType}

case class ServerRoute(restVerb: RestVerb, path: String, response: ServerResponse, params: Map[String, String] = Map())
case class ServerResponse(contentType: ContentType, body: String, code: Int)

object GET {
    def apply(path: String, params: Map[String, String] = Map(), response: ServerResponse): ServerRoute =
        ServerRoute(Get, path, response, params)
}

object POST {
    def apply(path: String, params: Map[String, String] = Map(), response: ServerResponse): ServerRoute =
        ServerRoute(Post, path, response, params)
}

class StubServer(port: Int, routes: ServerRoute*) {
    private var defaultResponse = ServerResponse(Text_Plain, "error", 404)
    private var simplyServer: SocketConnection = _
    private var portUsed = port

    def portInUse: Int = portUsed

    def start: StubServer = {
        this.simplyServer = startServer
        this
    }

    def stop = if (simplyServer != null) simplyServer.close()

    def defaultResponse(contentType: ContentType, body: String, responseCode: Int): StubServer = {
        this.defaultResponse = ServerResponse(contentType, body, responseCode)
        this
    }

    private def startServer: SocketConnection = {
        val container = new SimplyScala(defaultResponse, routes.toList)
        val connection = new SocketConnection(container)

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