package ru.youTube.database.video

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import ru.youTube.database.channel.Channel
import ru.youTube.database.channel.Channels
import ru.youTube.database.genre.Genre
import ru.youTube.database.genre.VideGenres
import ru.youTube.database.video.dto.CreateVideoDTO
import ru.youTube.database.video.enums.VideoSortingType
import ru.youTube.database.video.model.VideoModel
import ru.youTube.database.video.model.mapToModel

class Video(id: EntityID<Int>): IntEntity(id){
    companion object : IntEntityClass<Video>(Videos)

    var title by Videos.title
    var description by Videos.description
    var previewsUrl by Videos.previewsUrl
    var videoUrl by Videos.videoUrl
    var datePublication by Videos.datePublication
    var channel by Channel referencedOn Videos.channel
    var genre by Genre referencedOn Videos.genre
}

object Videos : IntIdTable("video"), VideoDAO {

    val title = varchar("title", 128)
    val description = varchar("description", 128)
    val previewsUrl = varchar("previews_url",528)
    val videoUrl = varchar("video_url", 528)
    val channel = reference("chanel", Channels)
    val genre = reference("genre", VideGenres)
    val datePublication = datetime("date_publication").defaultExpression(CurrentDateTime())

    override fun getVideos(
        search:String?,
        idGenre:Int?,
        pageNumber:Int,
        pageSize:Int,
        sortingType: VideoSortingType?
    ):List<VideoModel> {

        val videos = selectAll()

        when(sortingType){
            VideoSortingType.TITLE_ASC -> videos.orderBy(
                title,SortOrder.ASC
            )
            VideoSortingType.TITLE_DESC -> videos.orderBy(
                title,SortOrder.DESC
            )
            VideoSortingType.DESCRIPTION_DESC -> videos.orderBy(
                description,SortOrder.DESC
            )
            VideoSortingType.DESCRIPTION_ASC -> videos.orderBy(
                description,SortOrder.ASC
            )
            VideoSortingType.DATE_PUBLICATION_ASC -> videos.orderBy(
                datePublication,SortOrder.ASC
            )
            VideoSortingType.DATE_PUBLICATION_DESC -> videos.orderBy(
                datePublication,SortOrder.DESC
            )
            else -> Unit
        }

        return videos
            .limit(pageSize)
            .drop((pageNumber - 1) * pageSize)
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