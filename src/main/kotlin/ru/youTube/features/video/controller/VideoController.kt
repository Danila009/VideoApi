package ru.youTube.features.video.controller

import io.ktor.http.*
import ru.youTube.database.video.dto.CreateVideoDTO
import ru.youTube.database.video.model.Video

interface VideoController {

    suspend fun getVideos():List<Video>

    suspend fun getVideoById(id:Int):Video?

    suspend fun deleteById(id: Int): HttpStatusCode

    suspend fun insert(video: CreateVideoDTO): HttpStatusCode
}