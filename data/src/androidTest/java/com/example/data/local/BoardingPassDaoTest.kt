package com.example.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.local.dao.BoardingPassDao
import com.example.data.local.entity.BoardingPassEntity
import com.example.data.local.room.AppDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BoardingPassDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: BoardingPassDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.boardingPassDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    private fun sampleEntity(
        passId      : String = "pass-001",
        passengerId : String = "pax-001",
        uid         : String = "uid-abc",
        flightId    : String = "fl-001"
    ) = BoardingPassEntity(
        passId             = passId,
        passengerId        = passengerId,
        uid                = uid,
        flightId           = flightId,
        flightNumber       = "SK101",
        origin             = "ALG",
        originCity         = "Alger",
        destination        = "CDG",
        destinationCity    = "Paris",
        passengerName      = "Malik Benali",
        seatNumber         = "12A",
        gate               = "G5",
        boardingTime       = "09:30",
        departureTime      = 1_748_772_000_000L,
        arrivalTime        = 1_748_786_400_000L,
        bookingReference   = "PNR123",
        terminal           = "T2",
        qrCodeData         = "QR_DATA",
        issuedAt           = 1_748_606_400_000L,
        lastSyncedAt       = 0L,
        isSyncedWithServer = false
    )

    // Insert and Read

    @Test
    fun insertBoardingPass_thenGetById_returnsEntity() = runTest {
        val entity = sampleEntity()
        dao.insertBoardingPass(entity)

        val result = dao.getBoardingPassById("pass-001")
        assertNotNull(result)
        assertEquals("pass-001", result!!.passId)
        assertEquals("pax-001",  result.passengerId)
        assertEquals("SK101",    result.flightNumber)
    }

    @Test
    fun getBoardingPassById_whenNotFound_returnsNull() = runTest {
        val result = dao.getBoardingPassById("non-existent")
        assertNull(result)
    }

    @Test
    fun insertBoardingPass_thenGetByPassenger_emitsEntity() = runTest {
        dao.insertBoardingPass(sampleEntity())

        val result = dao.getBoardingPassByPassenger("pax-001").first()
        assertNotNull(result)
        assertEquals("pax-001", result!!.passengerId)
    }

    @Test
    fun getBoardingPassByPassenger_whenNotFound_emitsNull() = runTest {
        val result = dao.getBoardingPassByPassenger("unknown-pax").first()
        assertNull(result)
    }

    @Test
    fun insertBoardingPass_thenGetByFlight_emitsEntity() = runTest {
        dao.insertBoardingPass(sampleEntity())

        val result = dao.getBoardingPassByFlight("fl-001").first()
        assertNotNull(result)
        assertEquals("fl-001", result!!.flightId)
    }

    @Test
    fun insertMultiple_thenGetAll_returnsAll() = runTest {
        dao.insertBoardingPass(sampleEntity(passId = "pass-001", passengerId = "pax-001"))
        dao.insertBoardingPass(sampleEntity(passId = "pass-002", passengerId = "pax-002"))
        dao.insertBoardingPass(sampleEntity(passId = "pass-003", passengerId = "pax-003"))

        val all = dao.getAllBoardingPasses().first()
        assertEquals(3, all.size)
    }

    @Test
    fun insertMultiple_thenGetByUid_returnsOnlyMatchingUid() = runTest {
        dao.insertBoardingPass(sampleEntity(passId = "pass-001", uid = "uid-abc"))
        dao.insertBoardingPass(sampleEntity(passId = "pass-002", uid = "uid-abc"))
        dao.insertBoardingPass(sampleEntity(passId = "pass-003", uid = "uid-xyz"))

        val results = dao.getBoardingPassesByUid("uid-abc")
        assertEquals(2, results.size)
        assertTrue(results.all { it.uid == "uid-abc" })
    }

    @Test
    fun getBoardingPassesByUid_whenNoMatch_returnsEmptyList() = runTest {
        dao.insertBoardingPass(sampleEntity(uid = "uid-abc"))

        val results = dao.getBoardingPassesByUid("uid-unknown")
        assertTrue(results.isEmpty())
    }

    // Upsert (conflit)

    @Test
    fun insertBoardingPass_upsertUpdatesExistingRecord() = runTest {
        dao.insertBoardingPass(sampleEntity(passId = "pass-001"))

        val updated = sampleEntity(passId = "pass-001").copy(seatNumber = "22B")
        dao.insertBoardingPass(updated)

        val result = dao.getBoardingPassById("pass-001")
        assertEquals("22B", result!!.seatNumber)
    }

    // markAsSynced

    @Test
    fun markAsSynced_setsIsSyncedWithServer() = runTest {
        dao.insertBoardingPass(sampleEntity(passId = "pass-001"))

        val syncTime = System.currentTimeMillis()
        dao.markAsSynced(passId = "pass-001", syncedAt = syncTime)

        val result = dao.getBoardingPassById("pass-001")
        assertNotNull(result)
        assertTrue(result!!.isSyncedWithServer)
        assertEquals(syncTime, result.lastSyncedAt)
    }

    @Test
    fun markAsSynced_doesNotAffectOtherRecords() = runTest {
        dao.insertBoardingPass(sampleEntity(passId = "pass-001"))
        dao.insertBoardingPass(sampleEntity(passId = "pass-002"))

        dao.markAsSynced(passId = "pass-001", syncedAt = System.currentTimeMillis())

        val pass2 = dao.getBoardingPassById("pass-002")
        assertFalse(pass2!!.isSyncedWithServer)
    }

    // deleteAll

    @Test
    fun deleteAll_removesAllRecords() = runTest {
        dao.insertBoardingPass(sampleEntity(passId = "pass-001"))
        dao.insertBoardingPass(sampleEntity(passId = "pass-002"))

        dao.deleteAll()

        val all = dao.getAllBoardingPasses().first()
        assertTrue(all.isEmpty())
    }

    @Test
    fun deleteAll_onEmptyTable_doesNotThrow() = runTest {
        dao.deleteAll()
        val all = dao.getAllBoardingPasses().first()
        assertTrue(all.isEmpty())
    }

    // Champs nullables persistés

    @Test
    fun insertBoardingPass_withNullOptionalFields_persistsCorrectly() = runTest {
        val entity = sampleEntity().copy(
            seatNumber   = null,
            gate         = null,
            boardingTime = null,
            terminal     = null,
            qrCodeData   = null
        )
        dao.insertBoardingPass(entity)

        val result = dao.getBoardingPassById(entity.passId)
        assertNotNull(result)
        assertNull(result!!.seatNumber)
        assertNull(result.gate)
        assertNull(result.boardingTime)
        assertNull(result.terminal)
        assertNull(result.qrCodeData)
    }

    // Flow réactivité

    @Test
    fun getAllBoardingPasses_emitsUpdatedListAfterInsert() = runTest {
        val initial = dao.getAllBoardingPasses().first()
        assertTrue(initial.isEmpty())

        dao.insertBoardingPass(sampleEntity(passId = "pass-001"))

        val afterInsert = dao.getAllBoardingPasses().first()
        assertEquals(1, afterInsert.size)
    }
}