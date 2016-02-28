package xyz.donot.quetzal.view.dialog


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import xyz.donot.quetzal.R


class TweetTapDialog : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_tweet_tap_dialog, container, false)


        return v
    }

}
