package com.nxt.ontap

import android.app.Service
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MyBackGroundService : Service() {

    companion object{
        const val MY_ACTION = "hello"
        const val MY_TEXT = "hello"
    }

    private lateinit var mediaPlayer: MediaPlayer

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("main", "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        doBackGroundApp()

        return START_NOT_STICKY
    }

    private fun doBackGroundApp(){
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

    override fun onDestroy() {
        super.onDestroy()
    }
}