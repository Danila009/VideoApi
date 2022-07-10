package ru.youTube.database.video

import ru.youTube.database.video.dto.CreateVideoDTO
import ru.youTube.database.video.model.VideoModel

interface VideoDAO {

    fun getVideos():List<VideoModel>

    fun getVideoById(id:Int):VideoModel

    fun deleteById(id: Int)

    fun insert(video: CreateVideoDTO)
}