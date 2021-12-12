package com.nxt.boundservice

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MusicBoundService : Service() {

    private val mBinder = MyBinder()
    private lateinit var mediaPlayer: MediaPlayer

    inner class MyBinder : Binder() {
        fun getMusicBoundService(): MusicBoundService {
            return this@MusicBoundService
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("service", "OnCreate")
    }

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
        Log.e("service", "onBind")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
        Log.e("service", "unBind")
    }

    override fun onDestroy() {
        super.onDestroy()

        mediaPlayer.release()
        Log.e("service", "Destroy")
    }
    

     fun startMusic() {
        mediaPlayer = MediaPlayer.create(this, R.raw.matrinho)
        mediaPlayer.start()
    }
}