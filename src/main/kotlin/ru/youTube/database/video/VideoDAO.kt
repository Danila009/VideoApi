package ru.youTube.database.video

import ru.youTube.database.video.dto.CreateVideoDTO
import ru.youTube.database.video.enums.VideoSortingType
import ru.youTube.database.video.model.VideoModel

interface VideoDAO {

    fun getVideos(
        search:String?,
        idGenre:Int?,
        pageNumber:Int,
        pageSize:Int,
        sortingType:VideoSortingType?
    ):List<VideoModel>

    fun getVideoById(id:Int):VideoModel

    fun deleteById(id: Int)

    fun insert(video: CreateVideoDTO)
}