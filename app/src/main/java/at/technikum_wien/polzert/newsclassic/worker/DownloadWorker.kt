package at.technikum_wien.polzert.newsclassic.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class DownloadWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    private val logTag = "DownloadWorker"

    override suspend fun doWork(): Result {
        Log.e(logTag, "doWork() called")

        return Result.success()
    }

}