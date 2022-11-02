package com.steprobe.diceinterview.network

import com.google.gson.annotations.SerializedName

data class ArtistDetailsAreaDTO(val id: String?, val name: String?)
data class ArtistDetailsLifeSpanDTO(val ended: Boolean, val begin: String?, val end: String?)
data class ArtistDetailsReleaseGroupDTO(
    val id: String?,
    @SerializedName("first-release-date") val firstReleaseDate: String?,
    val title: String?,
    @SerializedName("primary-type") val primaryType: String?,
    @SerializedName("secondary-types") val secondaryTypes: List<String>?
)

data class ArtistDetailsDTO(
    val id: String?,
    val name: String?,
    val area: ArtistDetailsAreaDTO?,
    @SerializedName("life-span") val lifeSpan: ArtistDetailsLifeSpanDTO?,
    @SerializedName("release-groups") val releaseGroups: List<ArtistDetailsReleaseGroupDTO>
)
