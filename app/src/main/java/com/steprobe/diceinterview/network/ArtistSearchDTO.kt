package com.steprobe.diceinterview.network

data class ArtistTagDTO(val name: String)

data class ArtistSearchDTO(
    val id: String?,
    val name: String?,
    val tags: List<ArtistTagDTO>?
)

data class ArtistSearchResultDTO(val artists: List<ArtistSearchDTO>)
