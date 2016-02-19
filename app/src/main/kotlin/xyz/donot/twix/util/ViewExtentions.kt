package xyz.donot.twix.util

import android.app.Activity
import android.support.design.widget.Snackbar
import android.view.View

fun Activity.showSnackbar(view:View,message:String){
  Snackbar.make(view,message,Snackbar.LENGTH_LONG).show()
}

fun Activity.showSnackbar(view:View,message:Int){
  Snackbar.make(view,this.getString(message),Snackbar.LENGTH_LONG).show()
}
