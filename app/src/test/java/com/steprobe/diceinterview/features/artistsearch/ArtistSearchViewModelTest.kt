package com.steprobe.diceinterview.features.artistsearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.steprobe.diceinterview.DataState
import com.steprobe.diceinterview.network.ArtistAreaDTO
import com.steprobe.diceinterview.network.ArtistSearchDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.argThat
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.given
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class ArtistSearchViewModelTest {

    @get:Rule val executor = InstantTaskExecutorRule()

    @Mock private lateinit var mockRepo: ArtistSearchRepository
    @Mock private lateinit var mockObserver: Observer<DataState<List<ArtistDisplayModel>>>

    private val dispatcher = StandardTestDispatcher()

    private var mockHandle: AutoCloseable? = null

    @Before
    fun setUp() {
        mockHandle = MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mockHandle?.close()
    }

    @Test
    fun `Artist Search View Model should be able to handle error`() = runTest {
        val underTest = ArtistSearchViewModel(mockRepo, dispatcher)

        given(mockRepo.searchArtists(eq(SEARCH_TERM))).willThrow()
        underTest.artists.observeForever(mockObserver)
        underTest.searchArtists(SEARCH_TERM)

        advanceUntilIdle()

        verify(mockRepo).searchArtists(eq(SEARCH_TERM))
        verify(mockObserver).onChanged(argThat { result -> result is DataState.Error })
    }

    @Test
    fun `Artist Search View Model should be able to load artists`() = runTest {

        val underTest = ArtistSearchViewModel(mockRepo, dispatcher)

        given(mockRepo.searchArtists(eq(SEARCH_TERM))).willReturn(TEST_ARTISTS)
        underTest.artists.observeForever(mockObserver)
        underTest.searchArtists(SEARCH_TERM)

        advanceUntilIdle()

        verify(mockRepo).searchArtists(eq(SEARCH_TERM))
        argumentCaptor<DataState<List<ArtistDisplayModel>>>().apply {
            verify(mockObserver).onChanged(capture())
            assertEquals(DataState.Success(TEST_ARTISTS.map { it.toDisplayModel() }), firstValue)
        }
    }

    companion object {
        const val SEARCH_TERM = "whatever"

        val TEST_ARTISTS = listOf(
            ArtistSearchDTO("1", "Artist1", ArtistAreaDTO("Location"), listOf()),
            ArtistSearchDTO("2", "Artist2", ArtistAreaDTO("Location"), listOf()),
            ArtistSearchDTO("3", "Artist3", ArtistAreaDTO("Location"), listOf())
        )
    }
}
