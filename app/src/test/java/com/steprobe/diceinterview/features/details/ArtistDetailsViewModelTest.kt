package com.steprobe.diceinterview.features.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.steprobe.diceinterview.DataState
import com.steprobe.diceinterview.network.ArtistDetailsAreaDTO
import com.steprobe.diceinterview.network.ArtistDetailsDTO
import com.steprobe.diceinterview.network.ArtistDetailsLifeSpanDTO
import com.steprobe.diceinterview.network.ArtistDetailsReleaseGroupDTO
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
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.given
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class ArtistDetailsViewModelTest {

    @get:Rule val executor = InstantTaskExecutorRule()

    @Mock private lateinit var mockRepo: ArtistDetailsRepository
    @Mock private lateinit var mockObserver: Observer<DataState<ArtistDetailsDisplayModel>>
    @Mock private lateinit var mockAlbumObserver: Observer<List<AlbumDisplayModel>>

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
    fun `Should be able to load details and album art`() = runTest {

        val underTest = ArtistDetailsViewModel(mockRepo, dispatcher)

        given(mockRepo.getArtistDetails(eq(TEST_ARTIST_ID))).willReturn(TEST_ARTIST_DTO)
        given(mockRepo.getReleaseUrl(eq(RELEASE_1_ID))).willReturn(RELEASE_1_URL)
        given(mockRepo.getReleaseUrl(eq(RELEASE_2_ID))).willReturn(RELEASE_2_URL)

        underTest.details.observeForever(mockObserver)
        underTest.albums.observeForever(mockAlbumObserver)
        underTest.loadDetails(TEST_ARTIST_ID)

        advanceUntilIdle()

        verify(mockObserver).onChanged(
            argThat { details ->
                details is DataState.Success &&
                    details.data.disbanded == TEST_ARTIST_DTO.lifeSpan?.ended &&
                    details.data.placeOfOrigin == TEST_ARTIST_DTO.area?.name &&
                    details.data.begin == TEST_ARTIST_DTO.lifeSpan?.begin &&
                    details.data.end == TEST_ARTIST_DTO.lifeSpan?.end
            }
        )

        argumentCaptor<List<AlbumDisplayModel>>().apply {

            // First it gets called with no album art while it goes and gets it. Then once per
            // album art it needs to update
            verify(mockAlbumObserver, times(3)).onChanged(capture())

            assertEquals(2, thirdValue.size)
            assertEquals(RELEASE_1_ID, thirdValue[0].id)
            assertEquals(RELEASE_1_URL, thirdValue[0].image)

            assertEquals(RELEASE_2_ID, thirdValue[1].id)
            assertEquals(RELEASE_2_URL, thirdValue[1].image)
        }
    }

    @Test
    fun `View model should report error loading details`() = runTest {

        val underTest = ArtistDetailsViewModel(mockRepo, dispatcher)

        given(mockRepo.getArtistDetails(eq(TEST_ARTIST_ID))).willThrow(RuntimeException())

        underTest.details.observeForever(mockObserver)
        underTest.albums.observeForever(mockAlbumObserver)
        underTest.loadDetails(TEST_ARTIST_ID)

        advanceUntilIdle()

        verify(mockObserver).onChanged(argThat { details -> details is DataState.Error })
        verify(mockAlbumObserver, never()).onChanged(any())
    }

    @Test
    fun `View model should be able to handle error from album art loading`() = runTest {

        val underTest = ArtistDetailsViewModel(mockRepo, dispatcher)

        given(mockRepo.getArtistDetails(eq(TEST_ARTIST_ID))).willReturn(TEST_ARTIST_DTO)
        given(mockRepo.getReleaseUrl(any())).willThrow(RuntimeException())

        underTest.details.observeForever(mockObserver)
        underTest.albums.observeForever(mockAlbumObserver)
        underTest.loadDetails(TEST_ARTIST_ID)

        advanceUntilIdle()

        verify(mockObserver).onChanged(any())
        verify(mockAlbumObserver, times(1)).onChanged(any())
    }

    companion object {
        const val TEST_ARTIST_ID = "artist ID"

        const val RELEASE_1_ID = "release 1"
        const val RELEASE_2_ID = "release 2"
        const val RELEASE_1_URL = "https://release1"
        const val RELEASE_2_URL = "https://release2"

        val TEST_ARTIST_DTO = ArtistDetailsDTO(
            id = TEST_ARTIST_ID,
            name = "Test Artist",
            area = ArtistDetailsAreaDTO("area", "London"),
            lifeSpan = ArtistDetailsLifeSpanDTO(
                ended = true,
                begin = "1969",
                end = "1981"
            ),
            releaseGroups = listOf(
                ArtistDetailsReleaseGroupDTO(
                    RELEASE_1_ID,
                    "1969",
                    "release 1",
                    "Album",
                    listOf("Compilation")
                ),
                ArtistDetailsReleaseGroupDTO(
                    RELEASE_2_ID,
                    "1970",
                    "release 2",
                    "Album",
                    listOf("Compilation")
                )
            )
        )
    }
}
