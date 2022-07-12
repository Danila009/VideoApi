package ru.youTube.features.video.videoGenre.controller

import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.youTube.database.genre.VideoGenreDAO
import ru.youTube.database.genre.model.GenreModel

class VideoGenreControllerImpl(
    private val dao:VideoGenreDAO
):VideoGenreController {

    override suspend fun getGenres(
        search:String?
    ): List<GenreModel> = newSuspendedTransaction {
        return@newSuspendedTransaction dao.getGenre(search)
    }

    override suspend fun getGenreById(id: Int): GenreModel = newSuspendedTransaction {
        return@newSuspendedTransaction dao.getGenreById(id)
    }
}