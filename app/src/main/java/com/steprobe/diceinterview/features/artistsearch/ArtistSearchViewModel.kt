package com.steprobe.diceinterview.features.artistsearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.steprobe.diceinterview.DataState
import com.steprobe.diceinterview.di.IODispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ArtistSearchViewModel @Inject constructor(
    private val repo: ArtistSearchRepository,
    @IODispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _artists = MutableLiveData<DataState<List<ArtistDisplayModel>>>()
    val artists: LiveData<DataState<List<ArtistDisplayModel>>> = _artists

    fun searchArtists(search: String) {

        if (search.isEmpty()) {
            _artists.value = DataState.Success(emptyList())
            return
        }

        viewModelScope.launch(defaultDispatcher) {
            val artists = try {
                repo.searchArtists(search).map { it.toDisplayModel() }
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }

            withContext(Dispatchers.Main) {
                _artists.value = if (artists != null)
                    DataState.Success(artists) else
                    DataState.Error
            }
        }
    }
}
