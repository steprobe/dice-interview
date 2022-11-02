package com.steprobe.diceinterview.features.details

import com.steprobe.diceinterview.network.ArtistDetailsAreaDTO
import com.steprobe.diceinterview.network.ArtistDetailsDTO
import com.steprobe.diceinterview.network.ArtistDetailsLifeSpanDTO
import org.junit.Assert.assertEquals
import org.junit.Test

class ArtistSearchDetailsMapperTest {

    @Test
    fun `Details should be able to handle empty dates`() {
        val underTest = getTestDetails("", "")

        assertEquals("", underTest.toDetailsDisplayModel().begin)
        assertEquals("", underTest.toDetailsDisplayModel().end)
    }

    @Test
    fun `Details should be able to handle null dates`() {
        val underTest = getTestDetails(null, null)

        assertEquals("", underTest.toDetailsDisplayModel().begin)
        assertEquals("", underTest.toDetailsDisplayModel().end)
    }

    @Test
    fun `Details should be able to handle badly formatted dates`() {
        val underTest = getTestDetails("not a date", "not a date")

        assertEquals("", underTest.toDetailsDisplayModel().begin)
        assertEquals("", underTest.toDetailsDisplayModel().end)
    }

    @Test
    fun `Details should be able to parse good dates`() {
        val underTest = getTestDetails("1972-8-1", "1972-9-21")

        assertEquals("1972", underTest.toDetailsDisplayModel().begin)
        assertEquals("1972", underTest.toDetailsDisplayModel().end)
    }

    private fun getTestDetails(beginDate: String?, endDate: String?): ArtistDetailsDTO {
        return ArtistDetailsDTO(
            id = "id",
            name = "artist",
            area = ArtistDetailsAreaDTO(name = "London", id = "area"),
            lifeSpan = ArtistDetailsLifeSpanDTO(begin = beginDate, end = endDate, ended = false),
            releaseGroups = emptyList()
        )
    }
}
