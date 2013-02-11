package fr.simply.fixture

import fr.simply.{StubServer, ServerRoute}

trait StubServerFixture {
    def withStubServerFixture(port: Int, routes: ServerRoute*)(fixture: StubServer => Any) {
        val server: StubServer = new StubServer(port, routes:_*)
        try {
            server.start
            fixture(server)
        } finally server.stop
    }
}