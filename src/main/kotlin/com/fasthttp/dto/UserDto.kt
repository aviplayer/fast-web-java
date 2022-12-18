package com.fasthttp.dto

import com.dslplatform.json.CompiledJson

@CompiledJson
data class UserDto(
    val id: Int,
    val name: String,
    val email: String,
    val age: Int,
)