package xyz.donot.quetzal.util

import android.app.Activity
import android.content.Context
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast

fun Activity.showSnackbar(view:View,message:Int){
  Snackbar.make(view,this.getString(message),Snackbar.LENGTH_LONG).show()
}

fun Context.toast(messageResId: Int) {
  Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
}



