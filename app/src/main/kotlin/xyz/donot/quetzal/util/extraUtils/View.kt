package xyz.donot.quetzal.util.extrautils

import android.view.View

public fun View.show(): Unit {
  visibility = View.VISIBLE
}
public fun View.hide(): Unit {
  visibility = View.GONE
}
public fun View.toggleVisibility(): Unit = if (visibility == View.VISIBLE) hide() else show()


