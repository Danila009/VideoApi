package ru.youTube.features.user.controller

import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.youTube.auth.TokenProvider
import ru.youTube.common.Result
import ru.youTube.database.user.UserDAO
import ru.youTube.database.user.dto.AuthorizationUserDTO
import ru.youTube.features.user.model.AuthorizationResult
import ru.youTube.features.user.model.UserInfoResult

class UserControllerImpl(
    private val dao:UserDAO,
    private val tokenProvider: TokenProvider
) : UserController {

    override suspend fun getUserInfo(id: Int): UserInfoResult= newSuspendedTransaction {
        return@newSuspendedTransaction try {
            val response = dao.getUserInfo(id)
            UserInfoResult(
                login = response?.login,
                username = response?.username
            )
        }catch (e:Exception){
            UserInfoResult(error = e.message.toString())
        }
    }

    override suspend fun authorization(user: AuthorizationUserDTO): AuthorizationResult =
            newSuspendedTransaction {
        return@newSuspendedTransaction when(val useridResult = dao.authorization(user)){
            is Result.Error -> AuthorizationResult(
                error = useridResult.message
            )
            is Result.Success -> {
                if(useridResult.data == null){
                    AuthorizationResult(
                        error = "Id user === null"
                    )
                }else{
                    val token = tokenProvider.createToken(useridResult.data)
                    AuthorizationResult(
                        token = token
                    )
                }
            }
        }
    }
}