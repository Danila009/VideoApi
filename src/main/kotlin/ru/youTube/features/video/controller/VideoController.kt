package ru.youTube.features.video.controller

import io.ktor.http.*
import ru.youTube.database.video.dto.CreateVideoDTO
import ru.youTube.database.video.model.VideoModel

interface VideoController {

    suspend fun getVideos():List<VideoModel>

    suspend fun getVideoById(id:Int):VideoModel

    suspend fun deleteById(id: Int): HttpStatusCode

    suspend fun insert(video: CreateVideoDTO): HttpStatusCode
}