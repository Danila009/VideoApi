package ru.youTube.features.video.videoComment.controller

import ru.youTube.database.videoComment.dto.CreateVideoCommentDTO
import ru.youTube.database.videoComment.enums.VideoCommentSorting
import ru.youTube.database.videoComment.model.VideoCommentModel
import ru.youTube.features.video.videoComment.dto.InsertVideoCommentDto

interface VideoCommentController {

    suspend fun getComments(
        search:String? = null,
        pageNumber:Int = 1,
        pageSize:Int = 20,
        sortingType: VideoCommentSorting? = null
    ) : List<VideoCommentModel>

    suspend fun getCommentById(id:Int): VideoCommentModel

    suspend fun insertComment(comment: CreateVideoCommentDTO, idUser:Int): InsertVideoCommentDto

    suspend fun getCommentsByVideoId(
        idVideo:Int,
        search: String? = null,
        pageNumber:Int = 1,
        pageSize:Int = 20,
        sortingType: VideoCommentSorting? = null
    ):List<VideoCommentModel>
}