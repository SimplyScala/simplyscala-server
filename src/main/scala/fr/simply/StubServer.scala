package fr.simply

import org.simpleframework.transport.connect.SocketConnection
import java.net.{InetSocketAddress, SocketAddress}

case class ServerRoute(restVerb: RestVerb, path: String, response: ServerResponse, params: Map[String, String] = Map())

case class ServerResponse(contentType: String, body: String, code: Int)

object GET {
    def apply(path: String, response: ServerResponse, params: Map[String, String] = Map()): ServerRoute =
        ServerRoute(get, path, response, params)
}

class StubServer(port: Int, routes: ServerRoute*) {
    private var defaultResponse = ServerResponse("text/plain", "error", 404)

    private var simplyServer: SocketConnection = _

    def start: StubServer = {
        this.simplyServer = startServer
        this
    }

    def stop = if (simplyServer != null) simplyServer.close()

    def defaultResponse(contentType: String, body: String, responseCode: Int): StubServer = {
        this.defaultResponse = ServerResponse(contentType, body, responseCode)
        this
    }

    private def startServer: SocketConnection = {
        val container = new SimplyScala(defaultResponse, routes.toList)
        val connection = new SocketConnection(container)
        val address: SocketAddress = new InetSocketAddress(port)

        connection.connect(address)

        connection
    }
}