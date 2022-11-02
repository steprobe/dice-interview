package com.steprobe.diceinterview.features.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.steprobe.diceinterview.DataState
import com.steprobe.diceinterview.di.IODispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ArtistDetailsViewModel @Inject constructor(
    private val repo: ArtistDetailsRepository,
    @IODispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _albums = MutableLiveData<List<AlbumDisplayModel>>()
    val albums: LiveData<List<AlbumDisplayModel>>
        get() = _albums

    private val _details = MutableLiveData<DataState<ArtistDetailsDisplayModel>>()
    val details: LiveData<DataState<ArtistDetailsDisplayModel>>
        get() = _details

    fun loadDetails(artistId: String) {

        viewModelScope.launch(defaultDispatcher) {
            // Two parts to the loading here - first get the details,
            // then get each releases album art, if available
            // 1. Get Details
            val detailsDTO = try {
                repo.getArtistDetails(artistId)
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }

            val details = detailsDTO?.toDetailsDisplayModel()
            val albums = detailsDTO?.toAlbumsDisplayModel()

            // Expose what we have immediately - the user doesn't have to wait for the album art
            // That's a nice to have and might not even be available
            withContext(Dispatchers.Main) {
                _details.value = if (details != null)
                    DataState.Success(details) else
                    DataState.Error

                albums?.let { _albums.value = it }
            }

            // 2. Get album art
            albums?.map {
                async { loadAndExposeAlbumArtUrl(it.id) }
            }?.awaitAll()
        }
    }

    private suspend fun loadAndExposeAlbumArtUrl(releaseId: String?) {

        releaseId ?: return

        try {
            repo.getReleaseUrl(releaseId)?.let { albumArtUrl ->
                withContext(Dispatchers.Main) {
                    val albums = _albums.value ?: return@withContext

                    // We do a deep copy here so as not to override any data we have already exposed
                    // and keep it immutable
                    val updatedAlbums = albums.map {
                        AlbumDisplayModel(
                            id = it.id,
                            title = it.title,
                            type = it.type,
                            tags = it.tags,
                            date = it.date,
                            image = if (it.id == releaseId) albumArtUrl else it.image
                        )
                    }

                    _albums.value = updatedAlbums
                }
            }
        } catch (ex: Exception) {
            // If there is no art available, there is nothing to do. Just move on.
            ex.printStackTrace()
        }
    }
}
