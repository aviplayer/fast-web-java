package com.fasthttp.db

import com.fasthttp.dto.UserDto
import io.vertx.pgclient.PgConnectOptions
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.SqlClient
import reactor.core.publisher.Mono
import java.lang.RuntimeException


class DataBase {
    private var client: SqlClient
    init {
        val connectOptions = PgConnectOptions().setPort(5432)
            .setHost("db")
            .setDatabase("postgres")
            .setUser("postgres")
            .setPassword("postgres");
        val poolOptions = PoolOptions().setMaxSize(5)
        client = PgPool.client(connectOptions, poolOptions);
    }

    fun createUser(usr: UserDto): Mono<Int>{
        println(String.format(INSERT_USER, usr.name, usr.email  , usr.age))
        val  f = client
                .query(String.format(INSERT_USER, usr.name, usr.email  , usr.age))
            .execute()
        return Mono.fromCompletionStage(f.toCompletionStage())
            .map { r ->
                if(r.size() <= 0) {
                   throw RuntimeException("User hasn't created")
                }
                r.iterator().next().get(Integer::class.java, "id").toInt()
             }
    }

    fun getUsert(id: Int): Mono<UserDto> {
        println(String.format(GET_USER, id))
        val  f = client
            .query(String.format(GET_USER, id))
            .execute()
        return Mono.fromCompletionStage(f.toCompletionStage())
            .map { r ->
                if(r.size() <= 0) {
                    throw RuntimeException("User hasn't created")
                }
                val row = r.iterator().next()
                val id = row.get(Integer::class.java, "id").toInt()
                val name = row.get(String::class.java, "name")
                val email = row.get(String::class.java, "email")
                val age = row.get(Integer::class.java, "age").toInt()
                UserDto(id, name, email, age)
            }
    }

    fun deleteUser(usr: UserDto): Mono<UserDto> {
        println(String.format(DELETE_USER, usr.id))
        val  f = client
            .query(String.format(DELETE_USER, usr.id))
            .execute()
        return Mono.fromCompletionStage(f.toCompletionStage())
            .zipWith(Mono.just(usr))
            .map {tuple -> tuple.t2 }
    }

}