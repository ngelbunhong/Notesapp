package com.example.notesapp.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class NoteReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        NotificationHelper.showNotification(
            applicationContext,
            "Check Your Notes",
            "Don't forget your story today!"
        )
        return Result.success()
    }
}
