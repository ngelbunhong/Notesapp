package com.example.notesapp

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.utils.NoteReminderWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val nasController = navHostFragment.navController

        nasController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.insertNoteFragment -> {
                    binding.mainToolbar.visibility = View.GONE
                }
                else -> binding.mainToolbar.visibility = View.VISIBLE
            }
        }

        scheduleNoteReminderIfNeeded()
    }

    fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    fun showBackButton(show: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(show)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }


    private fun scheduleNoteReminderIfNeeded() {
        val workRequest = OneTimeWorkRequestBuilder<NoteReminderWorker>()
            .setInitialDelay(6, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(this).enqueueUniqueWork(
            "note_reminder",
            ExistingWorkPolicy.KEEP,
            workRequest
        )
    }


}