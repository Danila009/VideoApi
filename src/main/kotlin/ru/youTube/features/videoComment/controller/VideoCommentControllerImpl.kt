package ru.youTube.features.videoComment.controller

import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.youTube.database.videoComment.VideoCommentsDAO
import ru.youTube.database.videoComment.dto.CreateVideoCommentDTO
import ru.youTube.database.videoComment.model.VideoCommentModel
import ru.youTube.features.videoComment.dto.InsertVideoCommentDto

class VideoCommentControllerImpl(
    private val dao:VideoCommentsDAO
):VideoCommentController {

    override suspend fun getComments(): List<VideoCommentModel> = newSuspendedTransaction {
        return@newSuspendedTransaction dao.getComments()
    }

    override suspend fun getCommentById(id: Int): VideoCommentModel = newSuspendedTransaction {
        return@newSuspendedTransaction dao.getCommentById(id)
    }

    override suspend fun insertComment(comment: CreateVideoCommentDTO, idUser: Int):
            InsertVideoCommentDto = newSuspendedTransaction {
                return@newSuspendedTransaction try {
                    dao.insertComment(comment, idUser)
                    InsertVideoCommentDto()
                } catch (e:Exception){
                    InsertVideoCommentDto(e.message.toString())
                }
            }

}