package ru.youTube.database.video

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.youTube.database.video.dto.CreateVideoDTO
import ru.youTube.database.video.model.Video
import ru.youTube.database.video.model.mapToVideo

object Videos : Table("video"), VideoDAO {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 128)
    val previewsUrl = varchar("previews_url",528)
    val videoUrl = varchar("video_url", 528)

    override val primaryKey: PrimaryKey = PrimaryKey(id)

    override fun getVideos():List<Video> {
        return selectAll().mapNotNull { it.mapToVideo() }
    }

    override fun getVideoById(id:Int): Video? {
        return select { Videos.id eq id }
            .singleOrNull()?.mapToVideo()
    }

    override fun deleteById(id: Int) {
        deleteWhere{ Videos.id.eq(id) }
    }

    override fun insert(video: CreateVideoDTO) {
        Videos.insert {
            it[title] = video.title
            it[previewsUrl] = video.previewsUrl
            it[videoUrl] = video.videoUrl
        }
    }
}