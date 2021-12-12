package com.nxt.boundservicewithmessage

import android.app.Application
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.widget.Toast

class MusicService : Service() {

    companion object {
        const val MSG_PLAY_MUSIC = 1
    }

    private lateinit var messenger: Messenger
    private lateinit var mediaPlayer: MediaPlayer

    inner class MyHandler(
        context: Context,
        private val application: Context = context.applicationContext,
    ): Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg.what){
                MSG_PLAY_MUSIC -> startMusic()
                else -> super.handleMessage(msg)
            }
        }

        private fun startMusic() {
            mediaPlayer = MediaPlayer.create(getApplication(), R.raw.matrinho)
            mediaPlayer.start()
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("main", "onCreate")
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.e("main", "onBind")
        messenger = Messenger(MyHandler(this))
        return messenger.binder

    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.e("main", "onUnbind")
        return super.onUnbind(intent)

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        Log.e("main", "onDestroy")
    }
}