package ru.youTube.features.videoComment.controller

import ru.youTube.database.videoComment.dto.CreateVideoCommentDTO
import ru.youTube.database.videoComment.model.VideoCommentModel
import ru.youTube.features.videoComment.dto.InsertVideoCommentDto

interface VideoCommentController {

    suspend fun getComments() : List<VideoCommentModel>

    suspend fun getCommentById(id:Int): VideoCommentModel

    suspend fun insertComment(comment: CreateVideoCommentDTO, idUser:Int): InsertVideoCommentDto
}