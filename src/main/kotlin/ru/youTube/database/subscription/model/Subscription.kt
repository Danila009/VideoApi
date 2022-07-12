package ru.youTube.database.subscription.model

import kotlinx.datetime.LocalDateTime
import ru.youTube.database.subscription.Subscription

fun Subscription.mapToModel():SubscriptionModel{
    return SubscriptionModel(
        id = this.id.value,
        date = this.date,
        user = SubscriptionUser(
            id = this.user.id.value,
            username = this.user.username,
            login = this.user.login,
            photo = this.user.photo
        ),
        channel = SubscriptionChannel(
            id = this.channel.id.value,
            title = this.channel.title,
            icon = this.channel.icon
        )
    )
}

@kotlinx.serialization.Serializable
data class SubscriptionModel(
    val id:Int,
    val date: LocalDateTime,
    val user:SubscriptionUser,
    val channel:SubscriptionChannel
)

@kotlinx.serialization.Serializable
data class SubscriptionUser(
    val id:Int,
    val username:String,
    val login:String,
    val photo:String?
)

@kotlinx.serialization.Serializable
data class SubscriptionChannel(
    val id:Int,
    val title:String,
    val icon:String
)