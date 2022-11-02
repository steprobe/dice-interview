package com.steprobe.diceinterview.network

data class ArtistTagDTO(val name: String)
data class ArtistAreaDTO(val name: String)

data class ArtistSearchDTO(
    val id: String?,
    val name: String?,
    val area: ArtistAreaDTO?,
    val tags: List<ArtistTagDTO>?
)

data class ArtistSearchResultDTO(val artists: List<ArtistSearchDTO>)
