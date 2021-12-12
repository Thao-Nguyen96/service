package com.nxt.boundservicewithmessage

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import com.nxt.boundservicewithmessage.MusicService.Companion.MSG_PLAY_MUSIC
import com.nxt.boundservicewithmessage.databinding.ActivityMainBinding

//https://developer.android.com/guide/components/bound-services


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var messenger: Messenger
    private var isServiceConnected = false

    private var serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            messenger = Messenger(service)
            isServiceConnected = true
            //sendmessga play music
            sendMessagePlayMusic()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
           messenger = null!!
            isServiceConnected = false
        }
    }

    private fun sendMessagePlayMusic() {
        val message = Message.obtain(null, MSG_PLAY_MUSIC, 0, 0)
        messenger.send(message)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.start.setOnClickListener {
            clickStartMusic()
        }

        binding.stop.setOnClickListener {
            clickStopMusic()
        }
    }

    private fun clickStartMusic() {
        val intent = Intent(this, MusicService::class.java)
        bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun clickStopMusic() {
        if (isServiceConnected){
            unbindService(serviceConnection)
            isServiceConnected = false
        }
    }
}