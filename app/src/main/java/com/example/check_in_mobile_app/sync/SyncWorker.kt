package com.example.check_in_mobile_app.sync

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.domain.repository.BoardingPassRepository
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.FlightRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val flightRepository: FlightRepository,
    private val boardingPassRepository: BoardingPassRepository,
    private val authRepository: AuthRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val uid = authRepository.getCurrentUserId()
        Log.d("SyncWorker", "doWork() started — uid=$uid")

        if (uid == null) {
            Log.e("SyncWorker", "uid is null → failure")
            return Result.failure()
        }

        val flightResult = flightRepository.refreshFlightsFromRemote(uid)
        Log.d("SyncWorker", "flightResult success=${flightResult.isSuccess} error=${flightResult.exceptionOrNull()?.message}")

        if (flightResult.isFailure) {
            Log.e("SyncWorker", "Flight sync failed → retry")
            return Result.retry()
        }

        val boardingResult = boardingPassRepository.refreshBoardingPassesFromRemote()
        Log.d("SyncWorker", "boardingResult success=${boardingResult.isSuccess} error=${boardingResult.exceptionOrNull()?.message}")

        return Result.success()
    }

    companion object {
        const val WORK_NAME = "sync_flight_data"
    }
}