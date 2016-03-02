package xyz.donot.quetzal.util

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso

object BindingUtil {
  @BindingAdapter("bind:imageUrl")
  @JvmStatic
  fun loadImage(view: ImageView, url: String) {
    Picasso.with(view.context).load(url).into(view)
  }
}
