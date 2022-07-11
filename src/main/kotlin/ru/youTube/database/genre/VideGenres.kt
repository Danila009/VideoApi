package ru.youTube.database.genre

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.selectAll
import ru.youTube.database.genre.model.GenreModel
import ru.youTube.database.genre.model.mapToModel

class Genre(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Genre>(VideGenres)
    val name by VideGenres.name
}

object VideGenres : IntIdTable("genres"), VideoGenreDAO {
    val name = varchar("name", 128)

    override suspend fun getGenre(): List<GenreModel> {
        return selectAll().mapNotNull { Genre[it[id]].mapToModel() }
    }

    override suspend fun getGenreById(id:Int): GenreModel {
        return Genre[id].mapToModel()
    }
}