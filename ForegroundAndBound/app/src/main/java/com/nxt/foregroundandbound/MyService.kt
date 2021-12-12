package com.nxt.foregroundandbound

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class MyService : Service() {

    private var mBinder = MyBinder()
    private lateinit var mediaPlayer: MediaPlayer
     var isPlaying = false
     lateinit var mSong: Song

    inner class MyBinder: Binder(){
        fun getMyService(): MyService{
            return this@MyService
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("mainbinding", "onCreate")
    }

    override fun onBind(p0: Intent?): IBinder {
        Log.d("mainbinding", "onBind")
        return mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("mainbinding", "onStartCommand")

       val bundle = intent?.extras

        val song = bundle?.get("song") as? Song

        if (song != null){
            mSong = song
            sendNotification(song)
            startMusic(song)
        }


        return START_NOT_STICKY
    }

    private fun startMusic(song: Song){
        mediaPlayer = MediaPlayer.create(applicationContext, song.resource)
        mediaPlayer.start()
        isPlaying = true
    }

    fun pauseMusic(){
        if (isPlaying){
            mediaPlayer.pause()
            isPlaying = false
        }
    }

    fun resumeMusic(){
        if (!isPlaying){
            mediaPlayer.start()
            isPlaying = true
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("mainbinding", "onUnbind")

        return super.onUnbind(intent)

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun sendNotification(song: Song){
        val notification = NotificationCompat.Builder(this,"hello")
            .setContentTitle("notification")
            .setContentText(song.name)
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .build()

        //val manager = getSystemService(NotificationManager::class.java)as NotificationManager
        startForeground(20, notification)
    }

}