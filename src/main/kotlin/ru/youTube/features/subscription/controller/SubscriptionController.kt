package ru.youTube.features.subscription.controller

import ru.youTube.database.subscription.dto.CreateSubscriptionDTO
import ru.youTube.database.subscription.enums.SubscriptionSortingType
import ru.youTube.database.subscription.model.SubscriptionModel
import ru.youTube.database.subscription.model.SubscriptionUser

interface SubscriptionController {

    suspend fun getSubscriptionsByIdUser(
        idUser:Int,
        sortingType: SubscriptionSortingType? = null,
        search:String? = null,
        pageNumber:Int = 1,
        pageSize:Int = 20
    ):List<SubscriptionModel>

    suspend fun getUserSubscriptionChannel(idChannel:Int):List<SubscriptionUser>

    suspend fun getCheckSubscriptionChannel(idChannel:Int, idUser:Int):Boolean

    suspend fun getSubscriptionsChannelTotal(idChannel: Int):Int

    suspend fun getUserSubscriptionsChannelTotal(idUser: Int):Int

    suspend fun addSubscription(create: CreateSubscriptionDTO, userId:Int)

    suspend fun deleteSubscription(idSubscription: Int, idUser: Int)

    suspend fun deleteSubscriptionByIdChannel(idChannel: Int, idUser: Int)
}