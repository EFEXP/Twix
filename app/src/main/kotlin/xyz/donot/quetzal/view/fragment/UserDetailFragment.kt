package xyz.donot.quetzal.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.klinker.android.link_builder.LinkBuilder
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle.components.support.RxFragment
import kotlinx.android.synthetic.main.fragment_user_detail.*
import twitter4j.User
import xyz.donot.quetzal.R
import xyz.donot.quetzal.event.TwitterUserSubscriber
import xyz.donot.quetzal.twitter.TwitterObservable
import xyz.donot.quetzal.twitter.TwitterUpdateObservable
import xyz.donot.quetzal.util.bindToLifecycle
import xyz.donot.quetzal.util.getLinkList
import xyz.donot.quetzal.util.getMyId
import xyz.donot.quetzal.util.getTwitterInstance
import xyz.donot.quetzal.view.activity.PictureActivity
import xyz.donot.quetzal.view.activity.UsersActivity


class UserDetailFragment(val userId:Long) : RxFragment()
{val twitter by lazy { activity.getTwitterInstance() }
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val v = inflater.inflate(R.layout.fragment_user_detail, container, false)

    TwitterObservable(twitter).showUser(userId)
     .bindToLifecycle(this@UserDetailFragment)
    .subscribe (object : TwitterUserSubscriber() {
      override fun onNext(user: User) {
        super.onNext(user)
        if(user.id!=getMyId()) {
          TwitterObservable(twitter).showFriendShip(userId).subscribe {
            follow_button.apply {
              if (it.isSourceFollowingTarget) {
                text = "フォロー中"
                setOnClickListener {
                  TwitterUpdateObservable(twitter).destroyFriendShipAsync(userId).subscribe {
                    text = "フォロー"
                    setBackgroundColor(R.color.cardview_light_background)
                  }
                }
              } else {
                setOnClickListener {
                  TwitterUpdateObservable(twitter).createFriendShipAsync(userId)
                    .subscribe {
                      text = "フォロー中"
                      setBackgroundColor(R.color.colorPrimary)
                    }
                }
              }
            }
          }
        }
        else{
          follow_button.visibility=View.GONE
        }
        val iconIntent= Intent(activity, PictureActivity::class.java).putStringArrayListExtra("picture_urls",arrayListOf(user.originalProfileImageURLHttps))
        Picasso.with(activity).load(user.originalProfileImageURLHttps).into(icon_user)
        icon_user.setOnClickListener{startActivity(iconIntent)}
        user_name.text=user.name
        screen_name.text="@${user.screenName}"
        description.text=user.description.replace("\n","")
        web_txt.text=user.urlEntity.expandedURL
        geo_txt.text=user.location
        followed_text.text="Followers:${user.followersCount}"
        following_text.text="Friends:${user.friendsCount}"
        LinkBuilder.on(description).addLinks(activity.getLinkList()).build()
        LinkBuilder.on(web_txt).addLinks(activity.getLinkList()).build()
        cardView.setOnClickListener{activity.startActivity(Intent(activity, UsersActivity::class.java).putExtra("user_id",user.id))}
      }
    })
    return v
  }
}
