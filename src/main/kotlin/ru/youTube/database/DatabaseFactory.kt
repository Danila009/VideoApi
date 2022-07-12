package ru.youTube.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import ru.youTube.database.channel.Channels
import ru.youTube.database.genre.VideGenres
import ru.youTube.database.subscription.Subscriptions
import ru.youTube.database.user.Users
import ru.youTube.database.video.Videos
import ru.youTube.database.videoComment.VideoComments

object DatabaseFactory {

    fun init(){
        val hostname = ""
        val databaseName = ""
        val username = ""
        val password = ""
        val database = Database.connect(
            "jdbc:mysql://$hostname:3306/$databaseName?serverTimezone=UTC&useSSL=false",
            password = password,
            user = username
        )

        transaction(database) {
            SchemaUtils.createDatabase()
            SchemaUtils.create(Channels)
            SchemaUtils.create(Videos)
            SchemaUtils.create(VideoComments)
            SchemaUtils.create(VideGenres)
            SchemaUtils.create(Users)
            SchemaUtils.create(Subscriptions)
        }
    }
}