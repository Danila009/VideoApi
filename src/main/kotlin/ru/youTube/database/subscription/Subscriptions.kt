package ru.youTube.database.subscription

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import ru.youTube.database.channel.Channels
import ru.youTube.database.channel.model.Channel
import ru.youTube.database.subscription.dto.CreateSubscriptionDTO
import ru.youTube.database.subscription.model.SubscriptionModel
import ru.youTube.database.subscription.model.SubscriptionUser
import ru.youTube.database.subscription.model.mapToModel
import ru.youTube.database.user.Users
import ru.youTube.database.user.model.User

class Subscription(id: EntityID<Int>) : IntEntity(id){
    companion object : IntEntityClass<Subscription>(Subscriptions)

    var channel by Channel referencedOn Subscriptions.channel
    var user by User referencedOn Subscriptions.user
}

object Subscriptions:IntIdTable("channel_subscriptions"), SubscriptionDAO {

    val channel = reference("channel", Channels)
    val user = reference("user",Users)

    override fun getSubscriptionsByIdUser(idUser: Int): List<SubscriptionModel> {
        return selectAll()
            .filter {
               val subscription = Subscription[it[id]].mapToModel()
               subscription.user.id == idUser
            }
            .map { Subscription[it[id]].mapToModel() }
    }

    override fun getUserSubscriptionChannel(idChannel: Int): List<SubscriptionUser> {
        return selectAll()
            .filter {
                val subscription = Subscription[it[id]].mapToModel()
                subscription.channel.id == idChannel
            }.map {
                val subscription = Subscription[it[id]].mapToModel()
                SubscriptionUser(
                    id = subscription.user.id,
                    username = subscription.user.username,
                    login = subscription.user.login,
                    photo = subscription.user.photo,
                )
            }
    }

    override fun getCheckSubscriptionChannel(idChannel: Int, idUser:Int): Boolean {

        val subscriptions = selectAll().filter {
            val subscription = Subscription[it[id]].mapToModel()
            subscription.user.id == idUser && subscription.channel.id == idChannel
        }

        return subscriptions.isNotEmpty()
    }

    override fun getSubscriptionsChannelTotal(idChannel: Int): Int {
        val subscriptions = selectAll().filter {
            val subscription = Subscription[it[id]].mapToModel()
            subscription.channel.id == idChannel
        }
        return subscriptions.size
    }

    override fun getUserSubscriptionsChannelTotal(idUser: Int): Int {
        val subscriptions = selectAll().filter {
            val subscription = Subscription[it[id]].mapToModel()
            subscription.user.id == idUser
        }
        return subscriptions.size
    }

    override fun addSubscription(create: CreateSubscriptionDTO, userId: Int) {
        val subscriptionByUserId = selectAll()
            .filter {
                val subscription = Subscription[it[id]].mapToModel()
                subscription.user.id == userId
            }
            .map { Subscription[it[id]].mapToModel() }

        if (subscriptionByUserId.isNotEmpty())
            return

        Subscription.new {
            channel = Channel[create.channelId]
            user = User[userId]
        }
    }

    override fun deleteSubscription(idSubscription: Int) {
        deleteWhere { id.eq(idSubscription) }
    }
}