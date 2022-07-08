package ru.youTube.database.video

import ru.youTube.database.video.dto.CreateVideoDTO
import ru.youTube.database.video.model.Video

interface VideoDAO {

    fun getVideos():List<Video>

    fun getVideoById(id:Int):Video?

    fun deleteById(id: Int)

    fun insert(video: CreateVideoDTO)
}