package ru.youTube.database.video

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import ru.youTube.database.channel.Channels
import ru.youTube.database.channel.model.Channel
import ru.youTube.database.genre.Genre
import ru.youTube.database.genre.VideGenres
import ru.youTube.database.video.dto.CreateVideoDTO
import ru.youTube.database.video.model.Video
import ru.youTube.database.video.model.VideoModel
import ru.youTube.database.video.model.mapToModel

object Videos : IntIdTable("video"), VideoDAO {

    val title = varchar("title", 128)
    val description = varchar("description", 128)
    val previewsUrl = varchar("previews_url",528)
    val videoUrl = varchar("video_url", 528)
    val channel = reference("chanel", Channels)
    val genre = reference("genre", VideGenres)

    override fun getVideos(search:String?, idGenre:Int?):List<VideoModel> {
        return selectAll()
            .orderBy(title)
            .filter { result ->
                var filterResult:Boolean? = null
                val video = Video[result[id]].mapToModel()
                search?.let {
                    filterResult = video.title.lowercase().contains(it.lowercase())
                            || video.description.lowercase().contains(it.lowercase())
                }
                idGenre?.let {
                    filterResult = video.genre.id == it
                }
                filterResult ?: true
            }
            .map { Video[it[id]].mapToModel() }
    }

    override fun getVideoById(id:Int): VideoModel {
        return Video[id].mapToModel()
    }

    override fun deleteById(id: Int) { deleteWhere{ Videos.id.eq(id) } }

    override fun insert(video: CreateVideoDTO) {
        Video.new {
            title = video.title
            description = video.description
            previewsUrl = video.previewsUrl
            videoUrl = video.videoUrl
            channel = Channel[video.idChannel]
            genre = Genre[video.idGenre]
        }
    }
}