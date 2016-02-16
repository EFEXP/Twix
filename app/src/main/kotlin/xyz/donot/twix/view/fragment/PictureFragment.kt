package xyz.donot.twix.view.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.goka.flickableview.FlickableImageView
import org.greenrobot.eventbus.EventBus

import xyz.donot.twix.R
import xyz.donot.twix.event.OnFlickedEvent


class PictureFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val eventbus=   EventBus.getDefault()
     val url= arguments.getString("url")
         val image=   view.findViewById(R.id.flickable_image)as FlickableImageView
      Glide.with(this).load(url).asBitmap().into(image)
      image.setOnFlickListener(object:FlickableImageView.OnFlickableImageViewFlickListener{
        override fun onStartFlick() {

        }

        override fun onFinishFlick() {
          eventbus.post(OnFlickedEvent())
        }
      })

    }
}
