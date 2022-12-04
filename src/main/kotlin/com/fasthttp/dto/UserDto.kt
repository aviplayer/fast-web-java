package com.fasthttp.dto

import com.dslplatform.json.CompiledJson

@CompiledJson
data class UserDto(
    val id: Int,
    val username: String
)