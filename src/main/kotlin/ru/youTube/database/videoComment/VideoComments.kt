package ru.youTube.database.videoComment

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.selectAll
import ru.youTube.database.user.User
import ru.youTube.database.user.Users
import ru.youTube.database.video.Video
import ru.youTube.database.video.Videos
import ru.youTube.database.videoComment.dto.CreateVideoCommentDTO
import ru.youTube.database.videoComment.enums.VideoCommentSorting
import ru.youTube.database.videoComment.model.VideoCommentModel
import ru.youTube.database.videoComment.model.mapToModel

class VideoComment(id: EntityID<Int>): IntEntity(id){
    companion object : IntEntityClass<VideoComment>(VideoComments)

    var description by VideoComments.description
    var datePublication by VideoComments.datePublication
    var user by User referencedOn VideoComments.user
    var video by Video referencedOn VideoComments.video
}

object VideoComments : IntIdTable("video_comments"), VideoCommentsDAO {

    val description = varchar("description", 1080)
    val datePublication = datetime("date_publication").defaultExpression(CurrentDateTime())
    val video = reference("video", Videos)
    val user = reference("user", Users)

    override fun getComments(
        search: String?,
        pageNumber: Int,
        pageSize: Int,
        sortingType: VideoCommentSorting?
    ): List<VideoCommentModel> {
        val comments = selectAll()

        when(sortingType){
            VideoCommentSorting.DESCRIPTION_DESC -> comments.orderBy(
                description,SortOrder.DESC
            )
            VideoCommentSorting.DESCRIPTION_ASC -> comments.orderBy(
                description,SortOrder.ASC
            )
            VideoCommentSorting.DATE_PUBLICATION_ASC -> comments.orderBy(
                datePublication,SortOrder.ASC
            )
            VideoCommentSorting.DATE_PUBLICATION_DESC -> comments.orderBy(
                datePublication,SortOrder.DESC
            )
            null -> Unit
        }

        return comments
            .limit(pageSize)
            .drop((pageNumber - 1) * pageSize)
            .filter {
                val comment = VideoComment[it[id]].mapToModel()
                search?.let {
                    return@filter comment.description.contains(search)
                }
                true
            }
            .map { VideoComment[it[id]].mapToModel() }
    }

    override fun getCommentById(id: Int): VideoCommentModel {
        return VideoComment[id].mapToModel()
    }

    override fun insertComment(comment: CreateVideoCommentDTO, idUser: Int) {
        VideoComment.new {
            description = comment.description
            user = User[idUser]
            video = Video[comment.videoId]
        }
    }

    override fun getCommentsByVideoId(
        idVideo:Int,
        search: String?,
        pageNumber:Int,
        pageSize:Int,
        sortingType: VideoCommentSorting?
    ): List<VideoCommentModel> {
        val comments = selectAll()

        when(sortingType){
            VideoCommentSorting.DESCRIPTION_DESC -> comments.orderBy(
                description,SortOrder.DESC
            )
            VideoCommentSorting.DESCRIPTION_ASC -> comments.orderBy(
                description,SortOrder.ASC
            )
            VideoCommentSorting.DATE_PUBLICATION_ASC -> comments.orderBy(
                datePublication,SortOrder.ASC
            )
            VideoCommentSorting.DATE_PUBLICATION_DESC -> comments.orderBy(
                datePublication,SortOrder.DESC
            )
            null -> Unit
        }

        return comments
            .limit(pageSize)
            .drop((pageNumber - 1) * pageSize)
            .filter {
                val comment = VideoComment[it[id]].mapToModel()

                var filter = comment.video.id == idVideo

                if (!filter)
                    return@filter false

                search?.let {
                    filter = comment.description.contains(search)
                }

                filter
            }.map {
                VideoComment[it[id]].mapToModel()
            }
    }
}