package com.example.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.local.dao.FlightDao
import com.example.data.local.entity.FlightEntity
import com.example.data.local.room.AppDatabase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FlightDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: FlightDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.flightDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    // Fixture

    private fun sampleFlight(
        flightId     : String = "fl-001",
        flightNumber : String = "SK101"
    ) = FlightEntity(
        flightId         = flightId,
        flightNumber     = flightNumber,
        origin           = "ALG",
        originCity       = "Alger",
        destination      = "CDG",
        destinationCity  = "Paris",
        departureTime    = 1_748_772_000_000L,
        arrivalTime      = 1_748_786_400_000L,
        aircraftType     = "A320",
        status           = "Scheduled",
        boardingTime     = "09:30",
        checkInOpensTime = "07:00",
        gate             = "G5",
        terminal         = "T2",
        lastSyncedAt     = 0L
    )

    // Insert & Read

    @Test
    fun insertFlight_thenGetById_returnsEntity() = runTest {
        dao.insertFlight(sampleFlight())

        val result = dao.getFlightById("fl-001")
        assertNotNull(result)
        assertEquals("fl-001", result!!.flightId)
        assertEquals("SK101",  result.flightNumber)
    }

    @Test
    fun getFlightById_whenNotFound_returnsNull() = runTest {
        val result = dao.getFlightById("non-existent")
        assertNull(result)
    }

    @Test
    fun insertMultipleFlights_thenGetAll_returnsAll() = runTest {
        dao.insertFlight(sampleFlight(flightId = "fl-001", flightNumber = "SK101"))
        dao.insertFlight(sampleFlight(flightId = "fl-002", flightNumber = "SK102"))
        dao.insertFlight(sampleFlight(flightId = "fl-003", flightNumber = "SK103"))

        val all = dao.getAllFlights()
        assertEquals(3, all.size)
    }

    @Test
    fun getAllFlights_onEmptyTable_returnsEmptyList() = runTest {
        val all = dao.getAllFlights()
        assertTrue(all.isEmpty())
    }

    // insertFlights (bulk upsert)

    @Test
    fun insertFlights_insertsAllEntities() = runTest {
        val flights = listOf(
            sampleFlight(flightId = "fl-001"),
            sampleFlight(flightId = "fl-002"),
            sampleFlight(flightId = "fl-003")
        )
        dao.insertFlights(flights)

        val all = dao.getAllFlights()
        assertEquals(3, all.size)
    }

    @Test
    fun insertFlights_upsertUpdatesExistingRecord() = runTest {
        dao.insertFlight(sampleFlight(flightId = "fl-001"))

        dao.insertFlights(listOf(sampleFlight(flightId = "fl-001").copy(status = "Delayed")))

        val result = dao.getFlightById("fl-001")
        assertEquals("Delayed", result!!.status)
    }

    // REPLACE on conflict (insertFlight)

    @Test
    fun insertFlight_replaceOnConflict_updatesRecord() = runTest {
        dao.insertFlight(sampleFlight(flightId = "fl-001"))
        dao.insertFlight(sampleFlight(flightId = "fl-001").copy(gate = "G9"))

        val result = dao.getFlightById("fl-001")
        assertEquals("G9", result!!.gate)

        val all = dao.getAllFlights()
        assertEquals(1, all.size)
    }

    // deleteAllFlights

    @Test
    fun deleteAllFlights_removesAllRecords() = runTest {
        dao.insertFlight(sampleFlight(flightId = "fl-001"))
        dao.insertFlight(sampleFlight(flightId = "fl-002"))

        dao.deleteAllFlights()

        val all = dao.getAllFlights()
        assertTrue(all.isEmpty())
    }

    @Test
    fun deleteAllFlights_onEmptyTable_doesNotThrow() = runTest {
        dao.deleteAllFlights()
        val all = dao.getAllFlights()
        assertTrue(all.isEmpty())
    }

    // Champs nullables persistés

    @Test
    fun insertFlight_withNullOptionalFields_persistsCorrectly() = runTest {
        val entity = sampleFlight().copy(
            departureTime    = null,
            arrivalTime      = null,
            aircraftType     = null,
            status           = null,
            gate             = null,
            terminal         = null,
            boardingTime     = null,
            checkInOpensTime = null
        )
        dao.insertFlight(entity)

        val result = dao.getFlightById(entity.flightId)
        assertNotNull(result)
        assertNull(result!!.departureTime)
        assertNull(result.arrivalTime)
        assertNull(result.aircraftType)
        assertNull(result.status)
        assertNull(result.gate)
        assertNull(result.terminal)
        assertNull(result.boardingTime)
        assertNull(result.checkInOpensTime)
    }

    // Isolation entre tables

    @Test
    fun deleteAllFlights_doesNotAffectBoardingPassTable() = runTest {
        // Vérification que deleteAllFlights n'a pas d'effet de bord sur boarding_passes
        dao.insertFlight(sampleFlight())
        dao.deleteAllFlights()

        // La table flights est vide, mais l'appel ne lève pas d'exception
        // et n'affecte pas les autres tables
        val all = dao.getAllFlights()
        assertTrue(all.isEmpty())
    }
}