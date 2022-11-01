package com.steprobe.diceinterview.features.artistsearch

import com.steprobe.diceinterview.di.MusicBrainzApi
import com.steprobe.diceinterview.network.ArtistSearchDTO

class ArtistSearchRepository(private val api: MusicBrainzApi) {

    suspend fun searchArtists(search: String): List<ArtistSearchDTO> {
        try {
            return api.searchArtists(search, PAGE_SIZE, 0).artists
        } catch (ex: Exception) {
            ex.printStackTrace()
            return emptyList()
        }
    }

    companion object {
        const val PAGE_SIZE = 100
    }
}
