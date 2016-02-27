package xyz.donot.twix.view.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle.components.support.RxFragment
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator
import twitter4j.User
import xyz.donot.twix.R
import xyz.donot.twix.util.getTwitterInstance
import xyz.donot.twix.view.adapter.UsersAdapter
import xyz.donot.twix.view.listener.OnLoadMoreListener
import java.util.*

abstract class BaseUserFragment : RxFragment() {
  open var page : Int = 0
    get() {
      field++
      return field
    }

  open val data by lazy { LinkedList<User>() }
  open val  mAdapter by lazy { UsersAdapter(activity,data) }
  open val  twitter by lazy {activity.getTwitterInstance() }


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    val v = inflater.inflate(R.layout.fragment_timeline_base, container, false)
    val recycler=v.findViewById(R.id.recycler_view)as RecyclerView
    recycler.apply{
      itemAnimator= OvershootInRightAnimator(0.1f)
      adapter = AlphaInAnimationAdapter(mAdapter)
      layoutManager = LinearLayoutManager(activity)
      addOnScrollListener(object: OnLoadMoreListener(){
        override fun onScrolledToBottom() {
          TimelineLoader()
        }

      }

      )

      TimelineLoader()
    }
    return v}


  abstract fun TimelineLoader()


}