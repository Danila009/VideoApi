package ru.youTube.database.user.model

import org.jetbrains.exposed.sql.ResultRow
import ru.youTube.database.user.Users

data class User(
    val id:Int,
    val username:String,
    val login:String,
    val password:String
)

fun ResultRow.mapToUser():User{
    return User(
        id = this[Users.id],
        username = this[Users.username],
        login = this[Users.login],
        password = this[Users.password]
    )
}