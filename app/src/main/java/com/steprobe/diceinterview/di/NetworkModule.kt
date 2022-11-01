package com.steprobe.diceinterview.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideMusicBrainzApi(): MusicBrainzApi {

        val okHttp = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .addInterceptor(
                Interceptor { chain ->
                    val requestBuilder = chain.request().newBuilder()
                    requestBuilder.header("Accept", "application/json")
                    requestBuilder.header("User-Agent", "DiceInterviewApp")

                    chain.proceed(requestBuilder.build())
                }
            )

        val retrofit = Retrofit.Builder()
            .baseUrl("https://musicbrainz.org/ws/2/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp.build())
            .build()

        return retrofit.create(MusicBrainzApi::class.java)
    }

    @Singleton
    @Provides
    fun provideCoverArtApi(): CoverArtApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://coverartarchive.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(CoverArtApi::class.java)
    }
}
