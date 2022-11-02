package com.steprobe.diceinterview.features.details

import com.steprobe.diceinterview.network.ArtistDetailsDTO
import com.steprobe.diceinterview.network.ArtistDetailsReleaseGroupDTO
import org.joda.time.DateTime

data class AlbumDisplayModel(
    val id: String?,
    val title: String?,
    val type: String?,
    val tags: List<String>,
    val date: String?,
    var image: String?
)

data class ArtistDetailsDisplayModel(
    val name: String?,
    val placeOfOrigin: String?,
    val disbanded: Boolean,
    val begin: String?,
    val end: String?,
)

fun ArtistDetailsDTO.toAlbumsDisplayModel(): List<AlbumDisplayModel> {
    return releaseGroups.map { it.toDisplayModel() }
}

fun ArtistDetailsDTO.toDetailsDisplayModel(): ArtistDetailsDisplayModel {

    val begin = if (lifeSpan?.begin != null) DateTime.parse(lifeSpan.begin).year().asText else ""
    val end = if (lifeSpan?.end != null) DateTime.parse(lifeSpan.end).year().asText else ""

    return ArtistDetailsDisplayModel(
        name = name,
        placeOfOrigin = area?.name,
        disbanded = lifeSpan?.ended ?: false,
        begin = begin,
        end = end
    )
}

private fun ArtistDetailsReleaseGroupDTO.toDisplayModel(): AlbumDisplayModel {
    val date = if (firstReleaseDate != null) DateTime.parse(firstReleaseDate).year().asText else ""
    return AlbumDisplayModel(
        id = id,
        title = title,
        type = primaryType,
        date = date,
        tags = secondaryTypes ?: emptyList(),
        image = null
    )
}
