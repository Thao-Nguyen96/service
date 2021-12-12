package com.nxt.foregroundservice

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.nxt.foregroundservice.MyApplication.Companion.CHANNEL_ID

class MyService : Service() {

    companion object {
        const val ACTION_PAUSE = 1
        const val ACTION_RESUME = 2
        const val ACTION_CLEAR = 3
    }

    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying = false
    private lateinit var mSong: Song

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("service", "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val bundle = intent?.extras
        val song = bundle?.get("object") as Song

        mSong = song

        startMusic(song)
        sendNotification(song)


        val action = intent.getIntExtra("action_music", 0)
        actionMusic(action)

        return START_NOT_STICKY
    }

    private fun startMusic(song: Song) {
        mediaPlayer = MediaPlayer.create(applicationContext, song.resource)
        mediaPlayer.start()
        isPlaying = true
    }

    private fun actionMusic(action: Int) {
        when (action) {
            ACTION_PAUSE -> pauseMusic()
            ACTION_RESUME -> resumeMusic()
            ACTION_CLEAR -> stopSelf()
        }
    }

    private fun pauseMusic() {
        if (isPlaying) {
            mediaPlayer.pause()
            isPlaying = false
            sendNotification(mSong)
        }
    }

    private fun resumeMusic() {
        if (!isPlaying) {
            mediaPlayer.start()
            isPlaying = true
            sendNotification(mSong)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(song: Song) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            //tự đông update
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val bitmap = (ResourcesCompat.getDrawable(this.resources,
            song.image,
            null) as BitmapDrawable).toBitmap()

        val remoteViews = RemoteViews(packageName, R.layout.custom_notification)
        remoteViews.setTextViewText(R.id.tv_title_single, song.title)
        remoteViews.setTextViewText(R.id.tv_title_single, song.single)
        remoteViews.setImageViewBitmap(R.id.img_song, bitmap)

        remoteViews.setImageViewResource(R.id.img_play_or_pause, R.drawable.ic_baseline_pause_24)

        //-bắt sự kiện click buttom trên media

        if (isPlaying) {
            remoteViews.setOnClickPendingIntent(R.id.img_play_or_pause,
                getPendingIntent(this, ACTION_PAUSE))
            remoteViews.setImageViewResource(R.id.img_play_or_pause,
                R.drawable.ic_baseline_pause_24)
        } else {
            remoteViews.setOnClickPendingIntent(R.id.img_play_or_pause, getPendingIntent(this,
                ACTION_RESUME))
            remoteViews.setImageViewResource(R.id.img_play_or_pause,
                R.drawable.ic_baseline_play_arrow_24)
        }

        remoteViews.setOnClickPendingIntent(R.id.img_clear, getPendingIntent(this, ACTION_CLEAR))

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_baseline_apple_24)
            .setCustomContentView(remoteViews)
            .build()

        startForeground(1, notification)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getPendingIntent(context: Context, action: Int): PendingIntent {
        //dung broadcasd recever
        val intent = Intent(this, MyReceiver::class.java)
        intent.putExtra("action", action)

        return PendingIntent.getBroadcast(context.applicationContext,
            action,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("service", "onDestroy")
        mediaPlayer.release()
    }
}