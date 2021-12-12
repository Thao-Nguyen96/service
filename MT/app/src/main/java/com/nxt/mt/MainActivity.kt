package com.nxt.mt

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.nxt.mt.MyService.Companion.MY_TEXT
import com.nxt.mt.MyService.Companion.My_ACTION
import com.nxt.mt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var myService: MyService
    private var isServiceConnected = false

    private val serviceConnection: ServiceConnection = object : ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val myBinder = p1 as MyService.MyBinder
            myService = myBinder.getMyService()
            myService.doBackGround()
            isServiceConnected = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isServiceConnected = false
        }

    }

    private var mBroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1?.action == My_ACTION){
                val name = p1.getStringExtra(MY_TEXT).toString()
                binding.tv.text = name
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn.setOnClickListener {
            clickStartService()
        }
        binding.btnCancel.setOnClickListener {
            clickStopService()
        }
    }

    private fun clickStartService(){
        val intent = Intent(this,MyService::class.java)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }
    private fun clickStopService(){
        if (isServiceConnected) {
            unbindService(serviceConnection)
            isServiceConnected = false
        }
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(My_ACTION)
        registerReceiver(mBroadcastReceiver,intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mBroadcastReceiver)
    }
}