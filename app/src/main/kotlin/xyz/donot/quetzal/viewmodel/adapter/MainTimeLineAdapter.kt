package xyz.donot.quetzal.viewmodel.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import xyz.donot.quetzal.view.fragment.HomeTimeLine
import xyz.donot.quetzal.view.fragment.MentionTimeLine


class MainTimeLineAdapter(val fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(i: Int): Fragment {
        when (i) {
            0 -> {
                return HomeTimeLine()
            }
            1 -> {
                return MentionTimeLine()
            }
            else -> {
                throw IllegalAccessError()
            }
        }

    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> {
                return "Home"
            }
            1 -> {
                return "Mention"
            }
            else -> {
                throw IllegalAccessError()
            }
        }
    }
}