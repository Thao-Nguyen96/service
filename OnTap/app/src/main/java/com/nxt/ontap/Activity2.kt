package com.nxt.ontap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.nxt.ontap.databinding.Activity3Binding

class Activity2 : AppCompatActivity() {

    private lateinit var binding: Activity3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name1")
        Log.d("Mainactivity", name!!)

        binding.btn2.setOnClickListener {
            val intent = Intent()
            intent.putExtra("name2", "xuan thao")
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}