package com.nxt.boundservice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.nxt.boundservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var musicBoundService: MusicBoundService
    private var isServiceConnected: Boolean = false


    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val myBinder = service as MusicBoundService.MyBinder
            musicBoundService = myBinder.getMusicBoundService()
            musicBoundService.startMusic()
            isServiceConnected = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isServiceConnected = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSend.setOnClickListener {
            clickStartService()
        }

        binding.btnUn.setOnClickListener {
            clickStopService()
        }

    }
    private fun clickStartService() {
        //intent nới muốn starservice
        val intent = Intent(this, MusicBoundService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun clickStopService() {
        if (isServiceConnected){
            unbindService(serviceConnection)
            isServiceConnected = false
        }
    }


}