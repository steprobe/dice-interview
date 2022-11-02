package com.steprobe.diceinterview.features.artistsearch

import com.steprobe.diceinterview.network.ArtistSearchDTO
import java.util.*

data class ArtistDisplayModel(
    val id: String?,
    val name: String?,
    val origin: String?,
    val tags: List<String>?
)

fun ArtistSearchDTO.toDisplayModel(): ArtistDisplayModel {
    return ArtistDisplayModel(id, name, area?.name, tags?.map { capitalize(it.name) })
}

fun capitalize(str: String): String {
    return str.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }
}
