package ru.youTube.database.videoComment

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.selectAll
import ru.youTube.database.user.Users
import ru.youTube.database.user.model.User
import ru.youTube.database.video.Videos
import ru.youTube.database.video.model.Video
import ru.youTube.database.videoComment.dto.CreateVideoCommentDTO
import ru.youTube.database.videoComment.model.VideoCommentModel
import ru.youTube.database.videoComment.model.mapToModel

class VideoComment(id: EntityID<Int>): IntEntity(id){
    companion object : IntEntityClass<VideoComment>(VideoComments)

    var description by VideoComments.description
    var user by User referencedOn VideoComments.user
    var video by Video referencedOn VideoComments.video
}

object VideoComments : IntIdTable("video_comments"), VideoCommentsDAO {

    val description = varchar("description", 1080)
//    val datePublication = datetime("date_publication").defaultExpression(CurrentDateTime())
    val video = reference("video", Videos)
    val user = reference("user", Users)

    override fun getComments():List<VideoCommentModel> {
        return selectAll().mapNotNull { VideoComment[it[id]].mapToModel() }
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

    override fun getCommentsByVideoId(idVideo: Int): List<VideoCommentModel> {
        return emptyList()
    }
}