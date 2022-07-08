package ru.youTube.database.user

import ru.youTube.common.Result
import ru.youTube.database.user.dto.AuthorizationUserDTO
import ru.youTube.database.user.dto.RegistrationUserDTO
import ru.youTube.database.user.dto.UserDTO

interface UserDAO {

    fun authorization(user: AuthorizationUserDTO):Result<Int?>

    fun registration(user: RegistrationUserDTO):Int

    fun getUserInfo(id:Int) : UserDTO?
}