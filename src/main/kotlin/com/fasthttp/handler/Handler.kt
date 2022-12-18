package com.fasthttp.handler

import com.dslplatform.json.DslJson
import com.dslplatform.json.runtime.Settings
import com.fasthttp.db.DataBase
import com.fasthttp.dto.UserDto
//import com.fasthttp.dto._UserDto_DslJsonConverter
import reactor.netty.NettyOutbound
import reactor.netty.http.server.HttpServerRequest
import reactor.netty.http.server.HttpServerResponse
import java.io.ByteArrayOutputStream

val objectMapper = DslJson(Settings.withRuntime<Any>().includeServiceLoader())
val db = DataBase()


class Handler {
    companion object {
        fun handle(request: HttpServerRequest, response: HttpServerResponse): NettyOutbound {
            val lazy = request.receive()
                .aggregate()
                .asByteArray()
                .map { bytes -> objectMapper.deserialize(UserDto::class.java, bytes.inputStream()) }
                .flatMap { u ->  db.createUser(u)}
                .flatMap { id -> db.getUsert(id) }
                .flatMap { u -> db.deleteUser(u) }
                .doOnNext { user -> println(user) }
                .map {
                    val outputStream = ByteArrayOutputStream()
                    objectMapper.serialize(it, outputStream)
                    outputStream.toByteArray()
                }
                .doOnError { it.printStackTrace() }
            return response.sendByteArray(lazy)
        }
    }
}
