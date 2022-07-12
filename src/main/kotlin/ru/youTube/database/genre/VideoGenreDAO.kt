package ru.youTube.database.genre

import ru.youTube.database.genre.model.GenreModel

interface VideoGenreDAO {

    suspend fun getGenre(
        search:String?
    ):List<GenreModel>

    suspend fun getGenreById(id:Int):GenreModel
}