package ru.youTube.database.user

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import ru.youTube.common.Result
import ru.youTube.database.user.dto.AuthorizationUserDTO
import ru.youTube.database.user.dto.RegistrationUserDTO
import ru.youTube.database.user.dto.UserDTO
import ru.youTube.database.user.dto.mapToUserDTO
import ru.youTube.database.user.model.mapToUser

object Users : Table("user"), UserDAO {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 128)
    val login = varchar("login", 128)
    val password = varchar("password", 128)

    override val primaryKey: PrimaryKey = PrimaryKey(id)

    override fun authorization(user: AuthorizationUserDTO):Result<Int?> {
        return try {
            val userResult =
                select { login eq user.login }.singleOrNull()?.mapToUser()
                    ?: return Result.Error("user == null & login")

            if (userResult.password != user.password)
                return Result.Error("Неверный пароль")

            Result.Success(data = userResult.id)
        }catch (e:Exception){
            Result.Error(message = e.message.toString())
        }
    }

    override fun registration(user: RegistrationUserDTO): Int {
        return insert {
            it[username] = user.username
            it[login] = user.login
            it[password] = user.password
        }[id]
    }

    override fun getUserInfo(id: Int): UserDTO? {
        return select( this.id eq id ).singleOrNull()?.mapToUserDTO()
    }
}