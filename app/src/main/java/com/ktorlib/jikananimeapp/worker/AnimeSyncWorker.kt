package com.ktorlib.jikananimeapp.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ktorlib.jikananimeapp.data.local.AnimeDatabase
import com.ktorlib.jikananimeapp.data.remote.RetrofitClient
import com.ktorlib.jikananimeapp.data.repository.AnimeRepository

class AnimeSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val dao = AnimeDatabase
                .getInstance(applicationContext)
                .animeDao()

            val repo = AnimeRepository(
                RetrofitClient.api,
                dao
            )

            repo.refresh()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}