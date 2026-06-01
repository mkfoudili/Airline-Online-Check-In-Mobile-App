package com.example.data.mapper

import com.example.data.local.entity.BoardingPassEntity
import com.example.data.remote.dto.BoardingPassDto
import org.junit.Assert.*
import org.junit.Test

class BoardingPassMapperTest {

    private fun sampleDto() = BoardingPassDto(
        passId           = "pass-001",
        passengerId      = "pax-001",
        uid              = "uid-abc",
        flightId         = "fl-001",
        flightNumber     = "SK101",
        origin           = "ALG",
        originCity       = "Alger",
        destination      = "CDG",
        destinationCity  = "Paris",
        passengerName    = "Malik Benali",
        seatNumber       = "12A",
        gate             = "G5",
        boardingTime     = "2025-06-01T08:00:00Z",
        terminal         = "T2",
        departureTime    = "2025-06-01T10:00:00Z",
        arrivalTime      = "2025-06-01T14:00:00Z",
        bookingReference = "PNR123",
        qrCode           = "QR_DATA",
        issuedAt         = "2025-05-30T12:00:00Z"
    )

    private fun sampleEntity() = BoardingPassEntity(
        passId             = "pass-001",
        passengerId        = "pax-001",
        uid                = "uid-abc",
        flightId           = "fl-001",
        flightNumber       = "SK101",
        origin             = "ALG",
        originCity         = "Alger",
        destination        = "CDG",
        destinationCity    = "Paris",
        passengerName      = "Malik Benali",
        seatNumber         = "12A",
        gate               = "G5",
        boardingTime       = "2025-06-01T08:00:00Z",
        departureTime      = 1_748_772_000_000L,
        arrivalTime        = 1_748_786_400_000L,
        bookingReference   = "PNR123",
        terminal           = "T2",
        qrCodeData         = "QR_DATA",
        issuedAt           = 1_748_606_400_000L,
        lastSyncedAt       = 0L,
        isSyncedWithServer = true
    )

    // DTO → Domain

    @Test
    fun `dto toDomain maps all fields correctly`() {
        val domain = sampleDto().toDomain(contextUid = "uid-abc")

        assertEquals("pass-001",   domain.passId)
        assertEquals("pax-001",    domain.passengerId)
        assertEquals("uid-abc",    domain.uid)
        assertEquals("fl-001",     domain.flightId)
        assertEquals("SK101",      domain.flightNumber)
        assertEquals("ALG",        domain.origin)
        assertEquals("Alger",      domain.originCity)
        assertEquals("CDG",        domain.destination)
        assertEquals("Paris",      domain.destinationCity)
        assertEquals("Malik Benali", domain.passengerName)
        assertEquals("12A",        domain.seatNumber)
        assertEquals("G5",         domain.gate)
        assertEquals("T2",         domain.terminal)
        assertEquals("PNR123",     domain.bookingReference)
        assertEquals("QR_DATA",    domain.qrCodeData)
        assertTrue(domain.isSyncedWithServer)
        assertTrue(domain.lastSyncedAt > 0L)
    }

    @Test
    fun `dto toDomain parses departureTime from ISO8601`() {
        val domain = sampleDto().toDomain()
        assertTrue("departureTime doit être > 0", domain.departureTime!! > 0L)
    }

    @Test
    fun `dto toDomain parses arrivalTime from ISO8601`() {
        val domain = sampleDto().toDomain()
        assertTrue("arrivalTime doit être > 0", domain.arrivalTime!! > 0L)
    }

    @Test
    fun `dto toDomain uses contextUid when dto uid is null`() {
        val dto = sampleDto().copy(uid = null)
        val domain = dto.toDomain(contextUid = "fallback-uid")
        assertEquals("fallback-uid", domain.uid)
    }

    @Test
    fun `dto toDomain uses contextUid when dto uid is blank`() {
        val dto = sampleDto().copy(uid = "")
        val domain = dto.toDomain(contextUid = "fallback-uid")
        assertEquals("fallback-uid", domain.uid)
    }

    @Test
    fun `dto toDomain prefers dto uid over contextUid when present`() {
        val domain = sampleDto().toDomain(contextUid = "other-uid")
        assertEquals("uid-abc", domain.uid)
    }

    @Test
    fun `dto toDomain handles invalid date gracefully`() {
        val dto = sampleDto().copy(departureTime = "not-a-date")
        val domain = dto.toDomain()
        assertEquals(0L, domain.departureTime)
    }

    // Entity → Domain

    @Test
    fun `entity toDomain maps all fields correctly`() {
        val entity = sampleEntity()
        val domain = entity.toDomain()

        assertEquals(entity.passId,             domain.passId)
        assertEquals(entity.passengerId,        domain.passengerId)
        assertEquals(entity.uid,                domain.uid)
        assertEquals(entity.flightId,           domain.flightId)
        assertEquals(entity.flightNumber,       domain.flightNumber)
        assertEquals(entity.origin,             domain.origin)
        assertEquals(entity.originCity,         domain.originCity)
        assertEquals(entity.destination,        domain.destination)
        assertEquals(entity.destinationCity,    domain.destinationCity)
        assertEquals(entity.passengerName,      domain.passengerName)
        assertEquals(entity.seatNumber,         domain.seatNumber)
        assertEquals(entity.gate,               domain.gate)
        assertEquals(entity.boardingTime,       domain.boardingTime)
        assertEquals(entity.departureTime,      domain.departureTime)
        assertEquals(entity.arrivalTime,        domain.arrivalTime)
        assertEquals(entity.bookingReference,   domain.bookingReference)
        assertEquals(entity.terminal,           domain.terminal)
        assertEquals(entity.qrCodeData,         domain.qrCodeData)
        assertEquals(entity.issuedAt,           domain.issuedAt)
        assertEquals(entity.lastSyncedAt,       domain.lastSyncedAt)
        assertEquals(entity.isSyncedWithServer, domain.isSyncedWithServer)
    }

    // Domain → Entity

    @Test
    fun `domain toEntity maps all fields correctly`() {
        val domain = sampleEntity().toDomain()
        val entity = domain.toEntity()

        assertEquals(domain.passId,             entity.passId)
        assertEquals(domain.passengerId,        entity.passengerId)
        assertEquals(domain.uid,                entity.uid)
        assertEquals(domain.flightId,           entity.flightId)
        assertEquals(domain.flightNumber,       entity.flightNumber)
        assertEquals(domain.origin,             entity.origin)
        assertEquals(domain.originCity,         entity.originCity)
        assertEquals(domain.destination,        entity.destination)
        assertEquals(domain.destinationCity,    entity.destinationCity)
        assertEquals(domain.passengerName,      entity.passengerName)
        assertEquals(domain.seatNumber,         entity.seatNumber)
        assertEquals(domain.gate,               entity.gate)
        assertEquals(domain.boardingTime,       entity.boardingTime)
        assertEquals(domain.departureTime,      entity.departureTime)
        assertEquals(domain.arrivalTime,        entity.arrivalTime)
        assertEquals(domain.bookingReference,   entity.bookingReference)
        assertEquals(domain.terminal,           entity.terminal)
        assertEquals(domain.qrCodeData,         entity.qrCodeData)
        assertEquals(domain.issuedAt,           entity.issuedAt)
        assertEquals(domain.lastSyncedAt,       entity.lastSyncedAt)
        assertEquals(domain.isSyncedWithServer, entity.isSyncedWithServer)
    }

    @Test
    fun `entity toDomain then toEntity is lossless round-trip`() {
        val original = sampleEntity()
        val roundTripped = original.toDomain().toEntity()
        assertEquals(original, roundTripped)
    }

    // Champs nullables

    @Test
    fun `dto toDomain handles null optional fields`() {
        val dto = sampleDto().copy(
            uid      = null,
            terminal = null
        )
        val domain = dto.toDomain()
        assertEquals("", domain.uid)
        assertNull(domain.terminal)
    }

    @Test
    fun `entity toDomain preserves null nullable fields`() {
        val entity = sampleEntity().copy(
            seatNumber   = null,
            gate         = null,
            boardingTime = null,
            terminal     = null,
            qrCodeData   = null
        )
        val domain = entity.toDomain()
        assertNull(domain.seatNumber)
        assertNull(domain.gate)
        assertNull(domain.boardingTime)
        assertNull(domain.terminal)
        assertNull(domain.qrCodeData)
    }
}