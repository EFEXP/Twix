package xyz.donot.twix.view.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import org.greenrobot.eventbus.EventBus
import uk.co.senab.photoview.PhotoView
import xyz.donot.twix.R


class PictureFragment : Fragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

  }


  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val v = inflater!!.inflate(R.layout.fragment_picture, container, false)
    val eventbus=   EventBus.getDefault()
    val stringURL= arguments.getString("url")
    val img = v.findViewById(R.id.photo_view_image)as PhotoView
    Glide.with(this).load( stringURL).asBitmap().into(img)




    return v
  }



}
