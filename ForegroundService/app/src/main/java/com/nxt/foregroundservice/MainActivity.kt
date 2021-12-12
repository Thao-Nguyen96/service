package com.nxt.foregroundservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nxt.foregroundservice.MyService.Companion.MY_ACTION
import com.nxt.foregroundservice.MyService.Companion.MY_TEXT
import com.nxt.foregroundservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mBroadcastReceiver =object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == MY_ACTION){
                val name = intent.getStringExtra(MY_TEXT).toString()
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

        binding.btnStop.setOnClickListener {
            clickStopService()
        }

    }

    private fun clickStopService() {
        val intent = Intent(this, MyService::class.java)
        stopService(intent)
    }

    private fun clickStartService() {
        val intent = Intent(this, MyService::class.java)
        intent.putExtra("key_data", binding.edtData.text.toString().trim())
        startService(intent)
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(MY_ACTION)
        registerReceiver(mBroadcastReceiver,intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(mBroadcastReceiver)
    }
}