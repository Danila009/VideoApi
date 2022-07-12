package ru.youTube.database.subscription

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.selectAll
import ru.youTube.database.channel.Channel
import ru.youTube.database.channel.Channels
import ru.youTube.database.subscription.dto.CreateSubscriptionDTO
import ru.youTube.database.subscription.enums.SubscriptionSortingType
import ru.youTube.database.subscription.model.SubscriptionModel
import ru.youTube.database.subscription.model.SubscriptionUser
import ru.youTube.database.subscription.model.mapToModel
import ru.youTube.database.user.User
import ru.youTube.database.user.Users
import ru.youTube.database.user.model.mapToUser

class Subscription(id: EntityID<Int>) : IntEntity(id){
    companion object : IntEntityClass<Subscription>(Subscriptions)

    var date by Subscriptions.date
    var channel by Channel referencedOn Subscriptions.channel
    var user by User referencedOn Subscriptions.user
}

object Subscriptions:IntIdTable("channel_subscriptions"), SubscriptionDAO {

    val date = datetime("date_publication").defaultExpression(CurrentDateTime())
    val channel = reference("channel", Channels)
    val user = reference("user",Users)

    override fun getSubscriptionsByIdUser(
        idUser:Int,
        sortingType: SubscriptionSortingType?,
        search:String?,
        pageNumber:Int,
        pageSize:Int
    ): List<SubscriptionModel> {

        val subscription = selectAll()

        when(sortingType){
            SubscriptionSortingType.DATE_DESC -> subscription.orderBy(
                date,SortOrder.DESC
            )
            SubscriptionSortingType.DATE_ASC -> subscription.orderBy(
                date,SortOrder.ASC
            )
            null -> Unit
        }

        return subscription
            .limit(pageSize)
            .drop((pageNumber - 1) * pageSize)
            .filter {
                val subscriptionFilterModel = Subscription[it[id]].mapToModel()
                var filter = subscriptionFilterModel.user.id == idUser

                if (!filter)
                    return@filter false

                search?.let {
                    filter = subscriptionFilterModel.channel.title.contains(search)
                }

                filter
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
                subscription.channel.id == create.channelId
                        && subscription.user.id == userId
            }
            .map { Subscription[it[id]].mapToModel() }

        if (subscriptionByUserId.isNotEmpty())
            return

        Subscription.new {
            channel = Channel[create.channelId]
            user = User[userId]
        }
    }

    override fun deleteSubscriptionById(idSubscription: Int, idUser: Int) {
        val subscription = Subscription[idSubscription]

        if (subscription.user.id.value != idUser)
            return

        deleteWhere { id.eq(idSubscription) }
    }

    override fun deleteSubscriptionByIdChannel(idChannel: Int, idUser: Int) {
        val user = User[idUser].mapToUser()

        if (!user.channel.any{ it.id == idChannel })
            return

        deleteWhere{ channel.eq(idChannel) }
    }
}