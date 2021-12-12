package com.nxt.ontap

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.nxt.ontap.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var myBoundService: MyBoundService
    private var isServiceConnected: Boolean = false

    //IBinder để thực hiên giao tiếp với bound service
    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val myBinder = p1 as MyBoundService.MyBinder
            myBoundService = myBinder.getMusicBoundService()
            myBoundService.startMusic()
            isServiceConnected = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isServiceConnected = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            clickStartService()
        }

        binding.btnCancel.setOnClickListener {
            clickStopService()
        }
    }

    //chuyển ý định INTENT là thằng service muốn chạy
    private fun clickStartService() {
        val intent = Intent(this, MyBoundService::class.java)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }

    private fun clickStopService() {
        if (isServiceConnected) {
            unbindService(serviceConnection)
            isServiceConnected = false
        }
    }
}