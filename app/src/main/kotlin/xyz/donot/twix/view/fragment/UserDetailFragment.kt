package xyz.donot.twix.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.klinker.android.link_builder.LinkBuilder

import com.squareup.picasso.Picasso
import com.trello.rxlifecycle.components.support.RxFragment
import kotlinx.android.synthetic.main.fragment_user_detail.*
import xyz.donot.twix.R
import xyz.donot.twix.twitter.TwitterObservable
import xyz.donot.twix.util.bindToLifecycle
import xyz.donot.twix.util.getLinkList
import xyz.donot.twix.util.getTwitterInstance
import xyz.donot.twix.util.logd
import xyz.donot.twix.view.activity.PictureActivity
import xyz.donot.twix.view.activity.UsersActivity


class UserDetailFragment(val userId:Long) : RxFragment()
{val twitter by lazy { activity.getTwitterInstance() }
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val v = inflater.inflate(R.layout.fragment_user_detail, container, false)

    TwitterObservable(twitter).showUser(userId)
     .bindToLifecycle(this@UserDetailFragment)
    .subscribe {
      user->
      val iconIntent=Intent(activity,PictureActivity::class.java).putStringArrayListExtra("",arrayListOf(user.originalProfileImageURLHttps))
      Picasso.with(activity).load(user.originalProfileImageURLHttps).into(icon_user)
      icon_user.setOnClickListener{startActivity(iconIntent)}

      user_name.text=user.name
      screen_name.text="@${user.screenName}"
      description.text=user.description.replace("\n","")
      followed_text.text="Followers:${user.followersCount}"
      following_text.text="Friends:${user.friendsCount}"
      LinkBuilder.on(description).addLinks(activity.getLinkList()).build()
      following_text.setOnClickListener{
        logd("fo","fooooooooooooooooooooooo")
        activity.startActivity(Intent(activity,UsersActivity::class.java).putExtra("mode","friend").putExtra("userid",user.id))
      }
      followed_text.setOnClickListener{
        activity.startActivity(Intent(activity,UsersActivity::class.java).putExtra("mode","follower").putExtra("userid",user.id))
      }

    }
    return v
  }
}
