package xyz.donot.twix.view.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import twitter4j.Status
import xyz.donot.twix.R
import xyz.donot.twix.view.adapter.StatusAdapter
import xyz.donot.twix.view.dialog.TweetTapDialog
import xyz.donot.twix.view.listener.LastScrollListener
import xyz.donot.twix.view.listener.OnRecyclerListener
import java.util.*

abstract class BaseFragment : Fragment() {
    open var page : Int = 0
    open val data by lazy {  ArrayList<Status>() }
    open val  mAdapter by lazy { StatusAdapter(activity, data,OnRecyclerListener { view, i ->
      TweetTapDialog().show(activity.supportFragmentManager,"TweetTapDialog")
    }) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }


   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_timeline_base, container, false)
    val recycler_view=v.findViewById(R.id.recycler_view)as RecyclerView
     recycler_view.layoutManager = LinearLayoutManager(this@BaseFragment.context)
     recycler_view.adapter=mAdapter
     recycler_view.addOnScrollListener(object :LastScrollListener( LinearLayoutManager(this@BaseFragment.context)){
       override fun onLoadMore(current_page: Int) {
         TimelineLoader()
       }
     })


        return v
    }
abstract fun TimelineLoader()

}
