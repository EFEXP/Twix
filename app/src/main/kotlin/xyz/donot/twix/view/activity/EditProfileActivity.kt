package xyz.donot.twix.view.activity

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_edit_profile.*
import twitter4j.User
import xyz.donot.twix.R
import xyz.donot.twix.event.TwitterUserSubscriber
import xyz.donot.twix.twitter.TwitterObservable
import xyz.donot.twix.twitter.TwitterUpdateObservable
import xyz.donot.twix.util.getMyId
import xyz.donot.twix.util.getTwitterInstance

class EditProfileActivity : AppCompatActivity() {

val twitter by lazy { getTwitterInstance()}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
      toolbar.setNavigationOnClickListener { finish() }
        TwitterObservable( twitter).showUser(getMyId()).subscribe(object:TwitterUserSubscriber(){
          override fun onNext(user: User) {
            Picasso.with(this@EditProfileActivity).load(user.profileBannerIPadURL).into(profile_banner)
            Picasso.with(this@EditProfileActivity).load(user.originalProfileImageURLHttps).into(icon)
            web.text.insert(0,user.urlEntity.expandedURL)
            user_name.text.insert(0,user.name)
            geo.text.insert(0,user.location)
            description.text.insert(0,user.description)
          }
        })
        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
          TwitterUpdateObservable(twitter).updateProfileAsync(name = user_name.text.toString(),location = geo.text.toString(),description = description.text.toString(),url = web.text.toString())
          .subscribe (object:TwitterUserSubscriber(){
            override fun onCompleted() {
              super.onCompleted()
              Toast.makeText(this@EditProfileActivity,"更新しました",Toast.LENGTH_LONG).show()
            }
          })
          finish()

        }
    }

}
