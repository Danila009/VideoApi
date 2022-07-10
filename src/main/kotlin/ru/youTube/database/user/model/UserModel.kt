package ru.youTube.database.user.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.youTube.database.channel.Channels
import ru.youTube.database.channel.model.Channel
import ru.youTube.database.user.Users
import ru.youTube.database.user.dto.UserChannelDTO

class User(id: EntityID<Int>):IntEntity(id){
    companion object : IntEntityClass<User>(Users)

    var username by Users.username
    var photo by Users.photo
    var login by Users.login
    var password by Users.password
    val channel by Channel referrersOn Channels.user
}

data class UserModel(
    val id:Int,
    val username:String,
    val photo:String?,
    val login:String,
    val password:String,
    val channel: List<UserChannelDTO>
)

fun User.mapToUser():UserModel{
    return UserModel(
        id = this.id.value,
        username = this.username,
        photo = this.photo,
        login = this.login,
        password = this.password,
        channel = this.channel.map {
            UserChannelDTO(
                id = it.id.value,
                title = it.title,
                description = it.description
            )
        }
    )
}
