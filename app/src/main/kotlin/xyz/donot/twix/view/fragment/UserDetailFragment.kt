package xyz.donot.twix.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user_detail.*
import xyz.donot.twix.R
import xyz.donot.twix.twitter.TwitterObservable
import xyz.donot.twix.util.getTwitterInstance


class UserDetailFragment(val userId:Long) : Fragment()
{val twitter by lazy { activity.getTwitterInstance() }
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val v = inflater.inflate(R.layout.fragment_user_detail, container, false)
    TwitterObservable(twitter).showUser(userId)
    .subscribe {
      Picasso.with(activity).load(it.originalProfileImageURLHttps).into(icon_user)
      user_name.text=it.name
      screen_name.text=it.screenName
      description.text=it.description.replace("\n","")
    }



    return v
  }
}
