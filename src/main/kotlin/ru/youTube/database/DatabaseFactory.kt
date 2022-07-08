package ru.youTube.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import ru.youTube.database.user.Users
import ru.youTube.database.video.Videos

object DatabaseFactory {

    fun init(){
        val hostname = "cfif31.ru"
        val databaseName = "ISPr24-39_BeluakovDS_YouTube"
        val username = "ISPr24-39_BeluakovDS"
        val password = "ISPr24-39_BeluakovDS"
        val database = Database.connect(
            "jdbc:mysql://$hostname:3306/$databaseName?serverTimezone=UTC&useSSL=false",
            password = password,
            user = username
        )
        transaction(database) {
            SchemaUtils.create(Videos)
            SchemaUtils.create(Users)
        }
    }
}