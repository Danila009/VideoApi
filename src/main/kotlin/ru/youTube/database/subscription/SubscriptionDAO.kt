package ru.youTube.database.subscription

import ru.youTube.database.subscription.dto.CreateSubscriptionDTO
import ru.youTube.database.subscription.model.SubscriptionModel
import ru.youTube.database.subscription.model.SubscriptionUser

interface SubscriptionDAO {

    fun getSubscriptionsByIdUser(idUser:Int):List<SubscriptionModel>

    fun getUserSubscriptionChannel(idChannel:Int):List<SubscriptionUser>

    fun getCheckSubscriptionChannel(idChannel:Int, idUser:Int):Boolean

    fun getSubscriptionsChannelTotal(idChannel: Int):Int

    fun getUserSubscriptionsChannelTotal(idUser: Int):Int

    fun addSubscription(create: CreateSubscriptionDTO, userId:Int)

    fun deleteSubscription(idSubscription: Int)
}