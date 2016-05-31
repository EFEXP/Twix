package xyz.donot.quetzal.util

import android.content.Context
import android.net.ConnectivityManager

fun Context.haveNetworkConnection():Boolean
{
  val connMgr :ConnectivityManager? = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
 return  connMgr?.activeNetworkInfo?.isConnected?:false
}
