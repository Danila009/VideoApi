package ru.youTube.database.genre.model

import ru.youTube.database.genre.Genre

@kotlinx.serialization.Serializable
data class GenreModel(
    val id:Int,
    val name:String
)

fun Genre.mapToModel():GenreModel {
    return GenreModel(
        id = this.id.value,
        name = this.name
    )
}
