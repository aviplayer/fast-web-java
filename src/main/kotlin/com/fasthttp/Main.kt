package com.fasthttp
import com.fasthttp.handler.Handler
import io.vertx.pgclient.impl.codec.DataTypeCodec
import reactor.netty.http.server.HttpServer
import reactor.netty.http.server.HttpServerRoutes
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun main() {
    val port = 8089
    val server = HttpServer.create()
        .route { routes: HttpServerRoutes ->
            routes.post(
                "/api"
            ) { request, response -> Handler.handle(request, response) }
        }
        .host("0.0.0.0")
        .port(port)
        .bindNow()

    println("Starting on port $port ...")

    server.onDispose()
        .block()
}