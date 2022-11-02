package com.steprobe.diceinterview.di

import com.steprobe.diceinterview.network.ArtistDetailsDTO
import com.steprobe.diceinterview.network.ArtistSearchResultDTO
import com.steprobe.diceinterview.network.CoverArtResultDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MusicBrainzApi {

    @GET("artist")
    suspend fun searchArtists(
        @Query("query") search: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): ArtistSearchResultDTO

    @GET("artist/{artistId}/?inc=release-groups")
    suspend fun getArtistDetails(
        @Path(value = "artistId", encoded = true) artistId: String
    ): ArtistDetailsDTO
}

interface CoverArtApi {

    @GET("release-group/{releaseGroupId}")
    suspend fun getReleaseGroupImages(
        @Path(value = "releaseGroupId", encoded = true) releaseGroupId: String
    ): CoverArtResultDTO
}
