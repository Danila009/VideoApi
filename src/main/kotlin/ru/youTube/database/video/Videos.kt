package ru.youTube.database.video

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import ru.youTube.database.channel.Channels
import ru.youTube.database.channel.model.Channel
import ru.youTube.database.video.dto.CreateVideoDTO
import ru.youTube.database.video.model.Video
import ru.youTube.database.video.model.VideoModel
import ru.youTube.database.video.model.mapToVideo

object Videos : IntIdTable("video"), VideoDAO {

    val title = varchar("title", 128)
    val description = varchar("description", 128)
    val previewsUrl = varchar("previews_url",528)
    val videoUrl = varchar("video_url", 528)
    val channel = reference("chanel", Channels)

    override fun getVideos():List<VideoModel> {
        return selectAll().mapNotNull { Video[it[id]].mapToVideo() }
    }

    override fun getVideoById(id:Int): VideoModel {
        return Video[id].mapToVideo()
    }

    override fun deleteById(id: Int) { deleteWhere{ Videos.id.eq(id) } }

    override fun insert(video: CreateVideoDTO) {
        Video.new {
            title = video.title
            description = video.description
            previewsUrl = video.previewsUrl
            videoUrl = video.videoUrl
            channel = Channel[video.idChannel]
        }
    }
}