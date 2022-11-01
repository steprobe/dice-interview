package com.steprobe.diceinterview.features.artistsearch

import com.steprobe.diceinterview.network.ArtistSearchDTO
import com.steprobe.diceinterview.network.ArtistTagDTO
import org.junit.Assert.assertEquals
import org.junit.Test

class ArtistSearchDisplayMapperTest {

    @Test
    fun `Mapping from DTO to display model should be correct`() {

        val tags = listOf(ArtistTagDTO("rock"), ArtistTagDTO("roll"))
        val dto = ArtistSearchDTO("id", "name", tags)

        val display = dto.toDisplayModel()

        assertEquals(dto.id, display.id)
        assertEquals(dto.name, display.name)
        assertEquals(tags.map { it.name }, display.tags)
    }
}
