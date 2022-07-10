package ru.youTube.database.videoComment

import ru.youTube.database.videoComment.dto.CreateVideoCommentDTO
import ru.youTube.database.videoComment.model.VideoCommentModel

interface VideoCommentsDAO {

    fun getComments() : List<VideoCommentModel>

    fun getCommentById(id:Int):VideoCommentModel

    fun insertComment(comment: CreateVideoCommentDTO, idUser:Int)

    fun getCommentsByVideoId(idVideo:Int):List<VideoCommentModel>
}