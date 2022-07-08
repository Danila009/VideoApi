package ru.youTube.features.video.controller

import io.ktor.http.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.youTube.database.video.VideoDAO
import ru.youTube.database.video.dto.CreateVideoDTO
import ru.youTube.database.video.model.Video

class VideoControllerImpl (
    private val videoDAO:VideoDAO
) : VideoController {

    override suspend fun getVideos(): List<Video> = newSuspendedTransaction {
        return@newSuspendedTransaction videoDAO.getVideos()
    }

    override suspend fun getVideoById(id:Int): Video? = newSuspendedTransaction {
        return@newSuspendedTransaction videoDAO.getVideoById(id)
    }

    override suspend fun deleteById(id: Int): HttpStatusCode = newSuspendedTransaction {
        return@newSuspendedTransaction try {
            videoDAO.deleteById(id)
            HttpStatusCode.OK
        } catch (e:Exception){
            HttpStatusCode.NotFound
        }
    }

    override suspend fun insert(video: CreateVideoDTO) = newSuspendedTransaction {
        return@newSuspendedTransaction try {
            videoDAO.insert(video)
            HttpStatusCode.OK
        }catch (e:Exception){
            HttpStatusCode.NotFound
        }
    }

}