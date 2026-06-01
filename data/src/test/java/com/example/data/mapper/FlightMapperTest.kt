package com.example.data.mapper

import com.example.data.local.entity.FlightEntity
import com.example.data.remote.dto.FlightDto
import org.junit.Assert.*
import org.junit.Test
import java.util.Date

class FlightMapperTest {

    // Fixtures

    private val departureMs = 1_748_772_000_000L
    private val arrivalMs   = 1_748_786_400_000L

    private fun sampleDto() = FlightDto(
        flightId         = "fl-001",
        flightNumber     = "SK101",
        origin           = "ALG",
        originCity       = "Alger",
        destination      = "CDG",
        destinationCity  = "Paris",
        departureTime    = Date(departureMs),
        arrivalTime      = Date(arrivalMs),
        aircraftType     = "A320",
        status           = "Scheduled",
        boardingTime     = "09:30",
        checkInOpensTime = "07:00",
        gate             = "G5",
        terminal         = "T2"
    )

    private fun sampleEntity() = FlightEntity(
        flightId         = "fl-001",
        flightNumber     = "SK101",
        origin           = "ALG",
        originCity       = "Alger",
        destination      = "CDG",
        destinationCity  = "Paris",
        departureTime    = departureMs,
        arrivalTime      = arrivalMs,
        aircraftType     = "A320",
        status           = "Scheduled",
        boardingTime     = "09:30",
        checkInOpensTime = "07:00",
        gate             = "G5",
        terminal         = "T2",
        lastSyncedAt     = 0L
    )

    // DTO → Domain

    @Test
    fun `dto toDomain maps all fields correctly`() {
        val domain = sampleDto().toDomain()

        assertEquals("fl-001",    domain.flightId)
        assertEquals("SK101",     domain.flightNumber)
        assertEquals("ALG",       domain.origin)
        assertEquals("Alger",     domain.originCity)
        assertEquals("CDG",       domain.destination)
        assertEquals("Paris",     domain.destinationCity)
        assertEquals(departureMs, domain.departureTime)
        assertEquals(arrivalMs,   domain.arrivalTime)
        assertEquals("A320",      domain.aircraftType)
        assertEquals("Scheduled", domain.status)
        assertEquals("09:30",     domain.boardingTime)
        assertEquals("07:00",     domain.checkInOpensTime)
        assertEquals("G5",        domain.gate)
        assertEquals("T2",        domain.terminal)
    }

    @Test
    fun `dto toDomain uses 0 when departureTime is null`() {
        val domain = sampleDto().copy(departureTime = null).toDomain()
        assertEquals(0L, domain.departureTime)
    }

    @Test
    fun `dto toDomain uses 0 when arrivalTime is null`() {
        val domain = sampleDto().copy(arrivalTime = null).toDomain()
        assertEquals(0L, domain.arrivalTime)
    }

    @Test
    fun `dto toDomain uses empty string for null optional fields`() {
        val domain = sampleDto().copy(
            boardingTime     = null,
            checkInOpensTime = null,
            gate             = null,
            terminal         = null
        ).toDomain()

        assertEquals("", domain.boardingTime)
        assertEquals("", domain.checkInOpensTime)
        assertEquals("", domain.gate)
        assertEquals("", domain.terminal)
    }

    // Entity → Domain

    @Test
    fun `entity toDomain maps all fields correctly`() {
        val domain = sampleEntity().toDomain()

        assertEquals("fl-001",    domain.flightId)
        assertEquals("SK101",     domain.flightNumber)
        assertEquals("ALG",       domain.origin)
        assertEquals("Alger",     domain.originCity)
        assertEquals("CDG",       domain.destination)
        assertEquals("Paris",     domain.destinationCity)
        assertEquals(departureMs, domain.departureTime)
        assertEquals(arrivalMs,   domain.arrivalTime)
        assertEquals("A320",      domain.aircraftType)
        assertEquals("Scheduled", domain.status)
        assertEquals("09:30",     domain.boardingTime)
        assertEquals("07:00",     domain.checkInOpensTime)
        assertEquals("G5",        domain.gate)
        assertEquals("T2",        domain.terminal)
    }

    @Test
    fun `entity toDomain uses empty string for null optional fields`() {
        val domain = sampleEntity().copy(
            boardingTime     = null,
            checkInOpensTime = null,
            gate             = null,
            terminal         = null
        ).toDomain()

        assertEquals("", domain.boardingTime)
        assertEquals("", domain.checkInOpensTime)
        assertEquals("", domain.gate)
        assertEquals("", domain.terminal)
    }

    // Domain → Entity

    @Test
    fun `domain toEntity maps all fields correctly`() {
        val domain = sampleEntity().toDomain()
        val entity = domain.toEntity()

        assertEquals(domain.flightId,        entity.flightId)
        assertEquals(domain.flightNumber,    entity.flightNumber)
        assertEquals(domain.origin,          entity.origin)
        assertEquals(domain.originCity,      entity.originCity)
        assertEquals(domain.destination,     entity.destination)
        assertEquals(domain.destinationCity, entity.destinationCity)
        assertEquals(domain.departureTime,   entity.departureTime)
        assertEquals(domain.arrivalTime,     entity.arrivalTime)
        assertEquals(domain.aircraftType,    entity.aircraftType)
        assertEquals(domain.status,          entity.status)
        assertEquals("09:30",                entity.boardingTime)
        assertEquals("07:00",                entity.checkInOpensTime)
        assertEquals("G5",                   entity.gate)
        assertEquals("T2",                   entity.terminal)
    }

    @Test
    fun `domain toEntity stores null for blank optional string fields`() {
        val domain = sampleEntity().toDomain().copy(
            boardingTime     = "",
            checkInOpensTime = "",
            gate             = "",
            terminal         = ""
        )
        val entity = domain.toEntity()

        assertNull(entity.boardingTime)
        assertNull(entity.checkInOpensTime)
        assertNull(entity.gate)
        assertNull(entity.terminal)
    }

    // Round-trip

    @Test
    fun `entity toDomain then toEntity is lossless round-trip`() {
        val original    = sampleEntity()
        val roundTripped = original.toDomain().toEntity()

        // lastSyncedAt est régénéré dans toEntity(), on ignore ce champ
        assertEquals(original.copy(lastSyncedAt = roundTripped.lastSyncedAt), roundTripped)
    }
}