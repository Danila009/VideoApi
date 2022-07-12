package ru.youTube.database.user

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.select
import ru.youTube.common.Result
import ru.youTube.common.Result.*
import ru.youTube.database.channel.Channel
import ru.youTube.database.channel.Channels
import ru.youTube.database.user.dto.AuthorizationUserDTO
import ru.youTube.database.user.dto.RegistrationUserDTO
import ru.youTube.database.user.model.UserModel
import ru.youTube.database.user.model.mapToUser

class User(id: EntityID<Int>): IntEntity(id){
    companion object : IntEntityClass<User>(Users)

    var username by Users.username
    var dateRegistration by Users.dateRegistration
    var photo by Users.photo
    var login by Users.login
    var password by Users.password
    val channel by Channel referrersOn Channels.user
}

object Users : IntIdTable("user"), UserDAO {

    val username = varchar("username", 128)
    val photo = varchar("photo", 1080).nullable().default(null)
    val login = varchar("login", 128)
    val password = varchar("password", 128)
    val dateRegistration = datetime("date_publication").defaultExpression(CurrentDateTime())

    override fun authorization(userDTO: AuthorizationUserDTO):Result<Int?> {
        return try {
            val userResult =
                select { login eq userDTO.login }.singleOrNull()
                    ?: return Error("user == null & login")

            val userId = userResult[Users.id]
            val user = User[userId]

            if (user.password != userDTO.password)
                return Error("Неверный пароль")

            Success(data = user.id.value)
        }catch (e:Exception){
            Error(message = e.message.toString())
        }
    }

    override fun registration(user: RegistrationUserDTO): Result<Int> {
        return try {

            val userResult =
                select { login eq user.login }.singleOrNull()

            if (userResult != null)
                return Error("Login занят")

            val userId = insert {
                it[username] = user.username
                it[photo] = user.photo
                it[login] = user.login
                it[password] = user.password
            }[id].value

            Success(userId)
        }catch (e:Exception){
            Error(e.message.toString())
        }
    }

    override fun getUserInfo(id: Int): UserModel {
        return User[id].mapToUser()
    }

    override fun updateUserPhoto(id: Int, photoUrl:String) {
        User[id].photo = photoUrl
    }
}