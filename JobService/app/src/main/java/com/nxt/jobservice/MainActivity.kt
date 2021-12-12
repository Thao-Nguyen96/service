package com.nxt.jobservice

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.nxt.jobservice.MyService.Companion.MY_ACTION
import com.nxt.jobservice.MyService.Companion.MY_TEXT
import com.nxt.jobservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        const val JOB_ID = 23
    }

    private lateinit var binding: ActivityMainBinding

    private val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (MY_ACTION == intent!!.action) {

                 val text = intent.getStringExtra(MY_TEXT)
                // binding.tv.text = text

                val builder = AlertDialog.Builder(this@MainActivity)
                    .setTitle("Thong Bao")
                    .setMessage("Cong chuoi hoan thanh\n ket qua chuoi : $text")
                    .setPositiveButton("OK") { dialog, which ->
                        Toast.makeText(this@MainActivity, "ok", Toast.LENGTH_SHORT).show()
                    }
                val dialog = builder.create()
                dialog.show()

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.start.setOnClickListener {
            clickStartJob()
        }

        binding.cancel.setOnClickListener {
            clickCancelJob()
        }
    }

    private fun clickStartJob() {

        val componentName = ComponentName(this, MyService::class.java)

        //nhưng yêu cầu để thưc hiện job
        val jobInfo = JobInfo.Builder(JOB_ID, componentName)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)//wifi
            .setPersisted(true)//hoạt động khi bị sập nguồn khởi động lại vẫn hđ
            //.setPeriodic(15 * 60 * 1000)//15p thực hiện 1 lần
            .build()

        val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(jobInfo)

    }

    private fun clickCancelJob() {

        val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.cancel(JOB_ID)

    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(MY_ACTION)
        registerReceiver(mBroadcastReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()

        unregisterReceiver(mBroadcastReceiver)
    }
}