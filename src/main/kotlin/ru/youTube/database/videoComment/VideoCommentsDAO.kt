package ru.youTube.database.videoComment

import ru.youTube.database.videoComment.dto.CreateVideoCommentDTO
import ru.youTube.database.videoComment.enums.VideoCommentSorting
import ru.youTube.database.videoComment.model.VideoCommentModel

interface VideoCommentsDAO {

    fun getComments(
        search:String?,
        pageNumber:Int,
        pageSize:Int,
        sortingType: VideoCommentSorting?
    ) : List<VideoCommentModel>

    fun getCommentById(id:Int):VideoCommentModel

    fun insertComment(comment: CreateVideoCommentDTO, idUser:Int)

    fun getCommentsByVideoId(
        idVideo:Int,
        search: String?,
        pageNumber:Int,
        pageSize:Int,
        sortingType: VideoCommentSorting?
    ):List<VideoCommentModel>
}