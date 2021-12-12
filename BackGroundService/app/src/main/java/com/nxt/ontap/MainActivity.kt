package com.nxt.ontap

import android.app.Service
import android.content.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.nxt.ontap.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object{
        const val MY_ACTION = "hello"
        const val MY_TEXT = "hello"
    }

    private lateinit var binding: ActivityMainBinding

    private var mBroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1?.action == MY_ACTION){
                val name = p1.getStringExtra(MY_TEXT).toString()
                binding.tv.text = name
            }
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

    private fun clickStartService(){
        val intent = Intent(this,MyBackGroundService::class.java)
        startService(intent)
    }

    private fun clickStopService(){
        val intent = Intent(this,MyBackGroundService::class.java)
        stopService(intent)
    }

    override fun onStart() {
        super.onStart()
        val intentFilter  = IntentFilter(MY_ACTION)
        registerReceiver(mBroadcastReceiver,intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mBroadcastReceiver)
    }
}