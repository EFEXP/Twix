package xyz.donot.quetzal.binding

import android.databinding.BindingAdapter
import android.support.design.widget.NavigationView
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso
import xyz.donot.quetzal.R
import xyz.donot.quetzal.util.RoundCorner

@BindingAdapter("srcCompat")
fun srcCompat(view: ImageView, resourceId: Int) {
    view.setImageResource(resourceId);
}

object CustomizedSetter {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun imageUri(view: ImageView, url: String) {
        Picasso.with(view.context).load(url).placeholder(R.drawable.picture_place_holder).into(view)
    }

    @JvmStatic
    @BindingAdapter("avatarImageUrl")
    fun avatarImageUrl(view: ImageView, url: String) {
        Picasso.with(view.context).load(url).placeholder(R.drawable.avatar_place_holder).transform(RoundCorner()).into(view)
    }

    @JvmStatic
    @BindingAdapter("onClickListener")
    fun setListener(view: NavigationView, listener: View.OnClickListener) {
        view.setOnClickListener(listener)
    }
}