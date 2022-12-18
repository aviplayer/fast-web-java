package com.fasthttp.db

const val GET_USER = """SELECT * FROM users as u
WHERE u.id=%d"""

const val INSERT_USER = """INSERT INTO users (name, email, age)
values ('%s', '%s', %d) RETURNING id"""

const val DELETE_USER = """DELETE FROM users as u
WHERE u.id=%d"""