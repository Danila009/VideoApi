package ru.youTube.features.video.videoGenre.controller

import ru.youTube.database.genre.model.GenreModel

interface VideoGenreController {

    suspend fun getGenres():List<GenreModel>

    suspend fun getGenreById(id:Int):GenreModel
}