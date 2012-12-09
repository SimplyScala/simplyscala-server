package fr.simply

import org.simpleframework.http.core.Container
import org.simpleframework.http.Response
import org.simpleframework.http.Request
import org.simpleframework.transport.connect.{Connection, SocketConnection}
import java.net.{SocketAddress, InetSocketAddress}

object SimplyScala extends App {
    val container = new SimplyScala()
    val connection: Connection = new SocketConnection(container)
    val address: SocketAddress = new InetSocketAddress(8080)

    connection.connect(address)
}

class SimplyScala extends Container {
    def handle(request: Request, response: Response) {
        val body = response.getPrintStream
        val time = System.currentTimeMillis

        response.set("Content-Type", "text/plain")
        response.set("Server", "SimplyScala/1.0 (Simple 4.0)")
        response.setDate("Date", time)
        response.setDate("Last-Modified", time)

        body.println("Hello World !!")
        body.close
    }
}