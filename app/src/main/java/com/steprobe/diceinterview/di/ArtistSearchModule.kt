package com.steprobe.diceinterview.di

import com.steprobe.diceinterview.features.artistsearch.ArtistSearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ArtistSearchModule {

    @Singleton
    @Provides
    fun provideArtistSearchRepository(api: MusicBrainzApi): ArtistSearchRepository {
        return ArtistSearchRepository(api)
    }
}
