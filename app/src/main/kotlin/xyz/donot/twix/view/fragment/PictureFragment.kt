package xyz.donot.twix.view.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import uk.co.senab.photoview.PhotoViewAttacher
import xyz.donot.twix.R


class PictureFragment : Fragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

  }


  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val v = inflater.inflate(R.layout.fragment_picture, container, false)
    val stringURL= arguments.getString("url")
    val img = v.findViewById(R.id.photo_view_image)as ImageView
    Picasso.with(activity).load( stringURL).into(img)
    PhotoViewAttacher(img).update()
    return v
  }



}
