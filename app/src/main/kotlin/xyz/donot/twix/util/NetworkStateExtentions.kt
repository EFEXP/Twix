package xyz.donot.twix.util

import android.content.Context
import android.net.ConnectivityManager

fun Context.haveNetworkConnection():Boolean
{
  val connMgr = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
 return  connMgr.activeNetworkInfo?.isConnected?:false
}
