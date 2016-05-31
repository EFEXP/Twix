package xyz.donot.quetzal.util

import android.app.Activity
import android.databinding.BindingAdapter
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.ImageView

fun Activity.showSnackbar(view:View,message:Int){
  Snackbar.make(view,this.getString(message),Snackbar.LENGTH_LONG).show()
}




@BindingAdapter("srcCompat")
fun srcCompat(view : ImageView, resourceId:Int) {
  view.setImageResource(resourceId);
}
