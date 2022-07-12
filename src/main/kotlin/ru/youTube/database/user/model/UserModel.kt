package ru.youTube.database.user.model

import kotlinx.datetime.LocalDateTime
import ru.youTube.database.user.User
import ru.youTube.database.user.dto.UserChannelDTO

data class UserModel(
    val id:Int,
    val username:String,
    val dateRegistration: LocalDateTime,
    val photo:String?,
    val login:String,
    val password:String,
    val channel: List<UserChannelDTO>
)

fun User.mapToUser():UserModel{
    return UserModel(
        id = this.id.value,
        username = this.username,
        dateRegistration = this.dateRegistration,
        photo = this.photo,
        login = this.login,
        password = this.password,
        channel = this.channel.map {
            UserChannelDTO(
                id = it.id.value,
                title = it.title,
                icon = it.icon,
                description = it.description
            )
        }
    )
}
