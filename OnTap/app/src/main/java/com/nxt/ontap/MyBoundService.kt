package com.nxt.ontap

import android.app.Service
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MyBoundService : Service() {

    private val mBinder = MyBinder()
    private lateinit var mediaPlayer: MediaPlayer

    inner class MyBinder : Binder() {
        fun getMusicBoundService(): MyBoundService {
            return this@MyBoundService
        }
    }


    override fun onCreate() {
        super.onCreate()
        Log.d("main", "onCreate")
    }

    override fun onBind(p0: Intent?): IBinder {
        return mBinder
    }

    override fun unbindService(conn: ServiceConnection) {
        super.unbindService(conn)
        Log.d("main", "unbindService")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("main", "onDestroy")
        mediaPlayer.release()
    }

     fun startMusic(){
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.matrinho)
        mediaPlayer.start()
    }
}