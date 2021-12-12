package com.nxt.mt

import android.app.Service
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MyService : Service() {

    companion object{
       const val My_ACTION = "hello"
       const val MY_TEXT = "hello"
    }

    private var mBinder = MyBinder()

    inner class MyBinder : Binder() {
        fun getMyService(): MyService {
            return this@MyService
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("main", "onCreate")
    }

    override fun onBind(p0: Intent?): IBinder {
        return mBinder
    }

    fun doBackGround(){
        Thread(object : Runnable {
            override fun run() {
                for (i in 1..10) {
                    Thread.sleep(1000)
                    Log.e("main", "RUN $i")
                    // Thread.sleep(5000)
                }

                Log.d("main", "finish")
                val intent = Intent(My_ACTION)
                intent.putExtra(MY_TEXT,"hOAN THANH")
                sendBroadcast(intent)
            }
        }).start()
    }

    override fun unbindService(conn: ServiceConnection) {
        super.unbindService(conn)
        Log.d("main", "unbindService")
    }
}