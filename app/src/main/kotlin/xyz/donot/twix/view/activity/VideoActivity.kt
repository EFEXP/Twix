package xyz.donot.twix.view.activity

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.MediaController
import android.widget.VideoView
import xyz.donot.twix.R

class VideoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
      val video=findViewById(R.id.video_view)as VideoView
      val url = intent.getStringExtra("video_url")
      val mp =MediaController(this@VideoActivity)
      video.apply {
        setVideoURI(Uri.parse(url))
        setMediaController(mp)
      setOnCompletionListener {start() }
       start()
      }

     // video.setMediaController(mControlsView)
    //  mContentView.start()

    }
}
