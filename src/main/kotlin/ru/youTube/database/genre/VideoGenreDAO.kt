package ru.youTube.database.genre

import ru.youTube.database.genre.model.GenreModel

interface VideoGenreDAO {

    suspend fun getGenre():List<GenreModel>

    suspend fun getGenreById(id:Int):GenreModel
}