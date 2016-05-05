package xyz.donot.quetzal.view.activity

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.MediaController
import kotlinx.android.synthetic.main.activity_video.*
import xyz.donot.quetzal.R

class VideoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
      val url = intent.getStringExtra("video_url")
      val mp =MediaController(this@VideoActivity)
      video_view.apply {
        setVideoURI(Uri.parse(url))
        setMediaController(mp)
       setOnCompletionListener {video_view.seekTo(0)
         start()
       }
       start()

      }
    }
}
