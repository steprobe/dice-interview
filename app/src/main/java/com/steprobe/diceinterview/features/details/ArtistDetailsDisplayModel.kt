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

    return ArtistDetailsDisplayModel(
        name = name,
        placeOfOrigin = area?.name,
        disbanded = lifeSpan?.ended ?: false,
        begin = safeParseDateToYear(lifeSpan?.begin),
        end = safeParseDateToYear(lifeSpan?.end)
    )
}

private fun ArtistDetailsReleaseGroupDTO.toDisplayModel(): AlbumDisplayModel {
    return AlbumDisplayModel(
        id = id,
        title = title,
        type = primaryType,
        date = safeParseDateToYear(firstReleaseDate),
        tags = secondaryTypes ?: emptyList(),
        image = null
    )
}

fun safeParseDateToYear(date: String?): String {
    date ?: return ""
    return try {
        DateTime.parse(date).year().asText
    } catch (ex: IllegalArgumentException) {
        return ""
    }
}
