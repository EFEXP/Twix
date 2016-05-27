package xyz.donot.quetzal.util.extrautils

import android.view.View

fun View.show(): Unit {
  visibility = View.VISIBLE
}
fun View.hide(): Unit {
  visibility = View.GONE
}
fun View.toggleVisibility(): Unit = if (visibility == View.VISIBLE) hide() else show()


