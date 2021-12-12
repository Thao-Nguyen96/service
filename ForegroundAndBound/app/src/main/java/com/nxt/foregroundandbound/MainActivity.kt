package com.nxt.foregroundandbound

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import com.nxt.foregroundandbound.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var myService: MyService
    private var isServiceConnected = false

    private var serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val mBinder = p1 as MyService.MyBinder
            myService = mBinder.getMyService()

            handleLayoutMusic()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isServiceConnected = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnFore.setOnClickListener {
            clickStartFore()
        }

        binding.stop.setOnClickListener {
            clickStop()
        }

        binding.btnPause.setOnClickListener {
            if (myService.isPlaying){
                myService.pauseMusic()
            }else{
                myService.resumeMusic()
            }
            playOrPause()
        }
    }


    private fun handleLayoutMusic() {
        binding.layoutBottom.visibility = View.VISIBLE
        binding.name.text = myService.mSong.name

        playOrPause()
    }

   private fun playOrPause(){
       if (myService.isPlaying){
           binding.btnPause.setImageResource(R.drawable.ic_baseline_pause_24)
       }else{
           binding.btnPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
       }
    }

    private fun clickStartFore() {

        val song = Song("mat tri nho", R.raw.matrinho)
        val intent = Intent(this, MyService::class.java)

        val bundle = Bundle()
        bundle.putSerializable("song", song)
        intent.putExtras(bundle)

        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
        startService(intent)
    }

    private fun clickStop() {
        val intent = Intent(this, MyService::class.java)
        stopService(intent)
        if (isServiceConnected) {
            unbindService(serviceConnection)
            isServiceConnected = false
        }
    }
}