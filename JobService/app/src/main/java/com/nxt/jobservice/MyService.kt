package com.nxt.jobservice

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MyService : JobService() {

    companion object {
        const val TAG = "jobruntime"
        const val MY_ACTION = "main"
        const val MY_TEXT = "text"
    }

    private var jobCanceled: Boolean = false

    override fun onStartJob(jobParameters: JobParameters?): Boolean {
        Log.d(TAG, "job started")

        doBackGroundApp(jobParameters)
        return true
    }

    private fun doBackGroundApp(jobParameters: JobParameters?) {
        var result = 0
        Thread(object : Runnable {
            override fun run() {
                for (i in 1..1000000) {
                    if (jobCanceled) {
                        return
                    }
                    result += i
                    //Log.e(TAG, "RUN $result")
                    // Thread.sleep(5000)
                }
                Thread.sleep(5000)
                Log.d(TAG, "job finish")
                //co muốn lặp lại ko -> false: ko cần
                jobFinished(jobParameters, false)
                val intent = Intent(MY_ACTION)
                intent.putExtra(MY_TEXT, result.toString())
                sendBroadcast(intent)
            }
        }).start()
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        Log.d(TAG, "JOB STOPPED")
        jobCanceled = true
        return true
    }
}