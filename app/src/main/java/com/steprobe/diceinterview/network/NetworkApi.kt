package com.steprobe.diceinterview.di

import com.steprobe.diceinterview.network.ArtistSearchResultDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicBrainzApi {

    @GET("artist")
    suspend fun searchArtists(
        @Query("query") search: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): ArtistSearchResultDTO
}

interface CoverArtApi
