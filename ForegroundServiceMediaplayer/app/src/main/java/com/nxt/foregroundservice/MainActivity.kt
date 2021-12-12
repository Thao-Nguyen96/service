package com.nxt.foregroundservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nxt.foregroundservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
        val song = Song("Hello", "Thanh huyen", R.drawable.gau8, R.raw.matrinho)

        val intent = Intent(this, MyService::class.java)
        val bundle = Bundle()
        bundle.putSerializable("object", song)
        intent.putExtras(bundle)

        startService(intent)
    }
}