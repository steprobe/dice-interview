package com.steprobe.diceinterview.features.details

import com.steprobe.diceinterview.di.CoverArtApi
import com.steprobe.diceinterview.di.MusicBrainzApi
import com.steprobe.diceinterview.network.ArtistDetailsDTO

class ArtistDetailsRepository(
    private val musicBrainzApi: MusicBrainzApi,
    private val coverArtApi: CoverArtApi
) {
    suspend fun getArtistDetails(artistId: String): ArtistDetailsDTO {
        return musicBrainzApi.getArtistDetails(artistId)
    }

    suspend fun getReleaseUrl(releaseGroupId: String): String? {
        // API returns http instead of https. Server seems to support https though so this looks safe
        // If it turns out not to be we could update the network security config for this domain to
        // allow http
        return coverArtApi.getReleaseGroupImages(releaseGroupId).images.firstOrNull()?.image?.replace(
            "http",
            "https"
        )
    }
}
