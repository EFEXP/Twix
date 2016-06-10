package xyz.donot.quetzal.viewmodel.activity

import android.content.Context
import xyz.donot.quetzal.util.getTwitterInstance


abstract class ActivityViewModel(val context: Context) {
    abstract fun clean()
    val twitter by lazy { getTwitterInstance() }
}