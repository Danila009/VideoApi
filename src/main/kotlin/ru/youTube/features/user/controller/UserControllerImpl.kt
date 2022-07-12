package ru.youTube.features.user.controller

import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.youTube.auth.TokenProvider
import ru.youTube.common.Result
import ru.youTube.database.user.UserDAO
import ru.youTube.database.user.dto.AuthorizationUserDTO
import ru.youTube.database.user.dto.RegistrationUserDTO
import ru.youTube.features.user.model.AuthorizationResult
import ru.youTube.features.user.model.RegistrationResult
import ru.youTube.features.user.model.UserInfoResult

class UserControllerImpl(
    private val dao:UserDAO,
    private val tokenProvider: TokenProvider
) : UserController {

    override suspend fun getUserPhoto(id: Int) {

    }

    override suspend fun getUserInfo(id: Int): UserInfoResult= newSuspendedTransaction {
        return@newSuspendedTransaction try {
            val response = dao.getUserInfo(id)
            UserInfoResult(
                login = response.login,
                photo = response.photo,
                username = response.username,
                channel = response.channel
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

    override suspend fun registration(user: RegistrationUserDTO): RegistrationResult =
        newSuspendedTransaction {
            return@newSuspendedTransaction when(val userIdResult = dao.registration(user)){
                is Result.Error -> {
                    RegistrationResult(
                        error = userIdResult.message
                    )
                }
                is Result.Success -> {
                    val token = tokenProvider.createToken(userIdResult.data!!)
                    RegistrationResult(
                        token = token
                    )
                }
        }
    }

    override suspend fun updateUserPhoto(id: Int, photoUrl: String) = newSuspendedTransaction {
        dao.updateUserPhoto(id, photoUrl)
    }
}