package xyz.donot.quetzal.util.extrautils

import android.content.Context
import android.support.v4.app.Fragment
import android.widget.Toast

fun Context.toast(messageResId: Int) {
  mainThread { Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show() }
}

public fun Context.longToast(messageResId: Int) {
  Toast.makeText(this, messageResId, Toast.LENGTH_LONG).show()
}

public fun Context.toast(message: String) {
  mainThread { Toast.makeText(this, message, Toast.LENGTH_SHORT).show()}
}

public fun Context.longToast(message: String) {
  mainThread { Toast.makeText(this, message, Toast.LENGTH_LONG).show()}
}

public fun Fragment.toast(messageResId: Int) {
  Toast.makeText(activity, messageResId, Toast.LENGTH_SHORT).show()
}

public fun Fragment.longToast(messageResId: Int) {
  Toast.makeText(activity, messageResId, Toast.LENGTH_LONG).show()
}

public fun Fragment.toast(message: String) {
  mainThread {  Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()}
}

public fun Fragment.longToast(message: String) {
  Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
}

