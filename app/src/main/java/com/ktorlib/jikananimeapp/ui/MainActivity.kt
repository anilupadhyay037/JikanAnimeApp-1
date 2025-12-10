package com.ktorlib.jikananimeapp.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.ktorlib.jikananimeapp.R
import com.ktorlib.jikananimeapp.databinding.ActivityMainBinding
import com.ktorlib.jikananimeapp.ui.list.AnimeListFragment
import com.ktorlib.jikananimeapp.util.Constants.SYNC_INTERVAL_HOURS
import com.ktorlib.jikananimeapp.util.Constants.WORK_NAME_SYNC
import com.ktorlib.jikananimeapp.worker.AnimeSyncWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest =
            PeriodicWorkRequestBuilder<AnimeSyncWorker>(
                SYNC_INTERVAL_HOURS, TimeUnit.HOURS // configurable
            )
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            WORK_NAME_SYNC,
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, AnimeListFragment())
            .commit()

    }
}