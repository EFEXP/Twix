package xyz.donot.quetzal.util

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso
import xyz.donot.quetzal.R

object CustomizedSetter{
    @JvmStatic
    @BindingAdapter("avatarImageUrl")
    fun avatarImageUrl(view:ImageView,url:String) {
        Picasso.with(view.context).load(url).transform(RoundCorner()).placeholder(R.drawable.ic_contacts_grey_400_36dp).into(view)
    }
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun imageUrl(view:ImageView,url:String) {
        Picasso.with(view.context).load(url).placeholder(R.drawable.pugnotification_ic_placeholder).into(view)
    }

    @JvmStatic
    @BindingAdapter("srcCompat")
    fun srcCompat(view: ImageView, resourceId: Int) {
        view.setImageResource(resourceId)
    }
}
