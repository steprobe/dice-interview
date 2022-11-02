package com.steprobe.diceinterview.di

import com.steprobe.diceinterview.features.details.ArtistDetailsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ArtistDetailsModule {

    @Singleton
    @Provides
    fun provideArtistDetailsRepository(
        musicBrainzApi: MusicBrainzApi,
        coverArtApi: CoverArtApi
    ): ArtistDetailsRepository {
        return ArtistDetailsRepository(musicBrainzApi, coverArtApi)
    }
}
