package xyz.donot.quetzal.view.listener

import android.support.v7.widget.RecyclerView

abstract class OnLoadMoreListener : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        if (!recyclerView!!.canScrollVertically(-1)) {
            onScrolledToTop()
        } else if (!recyclerView.canScrollVertically(1)) {
            onScrolledToBottom()
        } else if (dy < 0) {
            onScrolledUp()
        } else if (dy > 0) {
            onScrolledDown()
        }
    }

    fun onScrolledUp() {
    }

    fun onScrolledDown() {
    }

    fun onScrolledToTop() {
    }

   abstract  fun onScrolledToBottom()
}
