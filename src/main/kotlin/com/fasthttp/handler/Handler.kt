package com.fasthttp.handler

import com.dslplatform.json.DslJson
import com.dslplatform.json.runtime.Settings
import com.fasthttp.dto.UserDto
//import com.fasthttp.dto._UserDto_DslJsonConverter
import reactor.netty.NettyOutbound
import reactor.netty.http.server.HttpServerRequest
import reactor.netty.http.server.HttpServerResponse
import java.io.ByteArrayOutputStream

val objectMapper = DslJson(Settings.withRuntime<Any>().includeServiceLoader())


class Handler {

//    init {
//        objectMapper.registerReader(UserDto::class.java, _UserDto_DslJsonConverter.ObjectFormatConverter(objectMapper))
//        objectMapper.registerWriter(UserDto::class.java, _UserDto_DslJsonConverter.ObjectFormatConverter(objectMapper))
//    }

    companion object {
        fun handle(request: HttpServerRequest, response: HttpServerResponse): NettyOutbound {
            val lazy = request.receive()
                .aggregate()
                .asByteArray()
                .map { bytes -> objectMapper.deserialize(UserDto::class.java, bytes.inputStream()) }
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
