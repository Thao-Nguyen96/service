package com.nxt.foregroundservice

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.nxt.foregroundservice.MyApplication.Companion.CHANNEL_ID

class MyService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object{
        const val MY_ACTION = "hello"
        const val MY_TEXT = "hello"
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("service", "onCreate")
    }

    //gửi 1 string từ activity qua service
    //START_NOT_STICKY: KO YÊU CẦU CHẠY LẠI
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val str: String = intent!!.getStringExtra("key_data")!!
        doHome()
        sendNotification(str)


        return START_NOT_STICKY
    }

    private fun doHome(){
        Thread(object : Runnable {
            override fun run() {
                for (i in 1..10) {
                    Thread.sleep(1000)
                    Log.e("main", "RUN $i")
                    // Thread.sleep(5000)
                }

                Log.d("main", "finish")
                val intent = Intent(MY_ACTION)
                intent.putExtra(MY_TEXT,"hOAN THANH")
                sendBroadcast(intent)
            }
        }).start()
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(str: String) {

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Notification channel 1")
            .setContentText(str)
                //màn hình hiện ra khi click vào notification
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_baseline_apple_24)
            .setAutoCancel(true)
            .build()

        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("service", "onDestroy")
    }
}