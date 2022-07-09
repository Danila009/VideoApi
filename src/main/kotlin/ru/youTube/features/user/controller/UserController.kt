package ru.youTube.features.user.controller

import ru.youTube.database.user.dto.AuthorizationUserDTO
import ru.youTube.database.user.dto.RegistrationUserDTO
import ru.youTube.features.user.model.AuthorizationResult
import ru.youTube.features.user.model.RegistrationResult
import ru.youTube.features.user.model.UserInfoResult

interface UserController {

    suspend fun registration(user: RegistrationUserDTO): RegistrationResult

    suspend fun authorization(user: AuthorizationUserDTO):AuthorizationResult

    suspend fun getUserInfo(id:Int) : UserInfoResult

    suspend fun getUserPhoto(id: Int)
}