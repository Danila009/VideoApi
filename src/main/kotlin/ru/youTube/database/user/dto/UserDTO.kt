package ru.youTube.database.user.dto

import org.jetbrains.exposed.sql.ResultRow
import ru.youTube.database.user.Users

data class UserDTO(
    val username:String,
    val login:String
)

fun ResultRow.mapToUserDTO() : UserDTO{
    return UserDTO(
        username = this[Users.username],
        login = this[Users.login]
    )
}