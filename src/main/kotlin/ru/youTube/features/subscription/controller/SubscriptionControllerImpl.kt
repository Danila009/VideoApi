package ru.youTube.features.subscription.controller

import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.youTube.database.subscription.SubscriptionDAO
import ru.youTube.database.subscription.dto.CreateSubscriptionDTO
import ru.youTube.database.subscription.enums.SubscriptionSortingType
import ru.youTube.database.subscription.model.SubscriptionModel
import ru.youTube.database.subscription.model.SubscriptionUser

class SubscriptionControllerImpl(
    private val dao:SubscriptionDAO
):SubscriptionController {

    override suspend fun getSubscriptionsByIdUser(
        idUser: Int,
        sortingType: SubscriptionSortingType?,
        search:String?,
        pageNumber:Int,
        pageSize:Int
    ): List<SubscriptionModel> =
        newSuspendedTransaction {
            return@newSuspendedTransaction dao.getSubscriptionsByIdUser(
                idUser, sortingType, search, pageNumber, pageSize
            )
        }

    override suspend fun getUserSubscriptionChannel(idChannel: Int): List<SubscriptionUser> =
        newSuspendedTransaction { return@newSuspendedTransaction dao.getUserSubscriptionChannel(idChannel) }

    override suspend fun getCheckSubscriptionChannel(idChannel: Int, idUser: Int): Boolean =
        newSuspendedTransaction { return@newSuspendedTransaction dao.getCheckSubscriptionChannel(idChannel,idUser) }

    override suspend fun getSubscriptionsChannelTotal(idChannel: Int): Int =
        newSuspendedTransaction { return@newSuspendedTransaction dao.getSubscriptionsChannelTotal(idChannel) }

    override suspend fun getUserSubscriptionsChannelTotal(idUser: Int): Int =
        newSuspendedTransaction { return@newSuspendedTransaction dao.getUserSubscriptionsChannelTotal(idUser) }

    override suspend fun addSubscription(create: CreateSubscriptionDTO, userId: Int) =
        newSuspendedTransaction { dao.addSubscription(create, userId) }

    override suspend fun deleteSubscription(idSubscription: Int, idUser: Int) =
        newSuspendedTransaction { dao.deleteSubscriptionById(idSubscription, idUser) }

    override suspend fun deleteSubscriptionByIdChannel(idChannel: Int, idUser: Int) =
        newSuspendedTransaction { dao.deleteSubscriptionByIdChannel(idChannel, idUser) }
}