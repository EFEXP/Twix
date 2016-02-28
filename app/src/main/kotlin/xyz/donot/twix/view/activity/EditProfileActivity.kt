package xyz.donot.twix.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
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
import xyz.donot.twix.util.getPath
import xyz.donot.twix.util.getTwitterInstance
import java.io.File

class EditProfileActivity : AppCompatActivity() {

val twitter by lazy { getTwitterInstance()}
  var uri: Uri?=null
  var uri2: Uri?=null
  val intentGallery=
    if (Build.VERSION.SDK_INT < 19) {
      Intent(Intent.ACTION_GET_CONTENT)
        .setType("image/*")

    } else {
      Intent(Intent.ACTION_OPEN_DOCUMENT)
        .addCategory(Intent.CATEGORY_OPENABLE)
        .setType("image/jpeg")
    }
  override fun onActivityResult(requestCode:Int , resultCode: Int, data: Intent?){
    if (resultCode == RESULT_OK&&data!=null) {
      val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION
      when(requestCode)
      {
        //banner
      1->{
        uri2 = data.data
        contentResolver.takePersistableUriPermission(uri2, takeFlags)
        Picasso.with(this@EditProfileActivity).load(uri2).noPlaceholder().into(profile_banner)
      }
        2->{
          uri = data.data
          contentResolver.takePersistableUriPermission(uri, takeFlags)
          Picasso.with(this@EditProfileActivity).load(uri).noPlaceholder().into(icon)
        }

      }
    }
  }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
      toolbar.setNavigationOnClickListener { finish() }
        TwitterObservable( twitter).showUser(getMyId()).subscribe(object:TwitterUserSubscriber(){
          override fun onNext(user: User) {
            Picasso.with(this@EditProfileActivity).load(user.profileBannerIPadURL).into(profile_banner)
            Picasso.with(this@EditProfileActivity).load(user.originalProfileImageURLHttps).into(icon)
            profile_banner.setOnClickListener{
              startActivityForResult(intentGallery,1) }
            icon.setOnClickListener{
              startActivityForResult(intentGallery,2) }

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

            override fun onError(e: Throwable) {
              super.onError(e)
              Toast.makeText(this@EditProfileActivity,"失敗しました",Toast.LENGTH_LONG).show()
            }
          })
          if (uri2 != null) {
            TwitterUpdateObservable(twitter).profileImageUpdateAsync(File(getPath(uri2!!)))
          }
          if (uri != null) {
            TwitterUpdateObservable(twitter).profileImageUpdateAsync(File(getPath(uri!!))).subscribe (object:TwitterUserSubscriber(){
              override fun onCompleted() {
                super.onCompleted()
                Toast.makeText(this@EditProfileActivity,"画像更新しました",Toast.LENGTH_LONG).show()
              }

              override fun onError(e: Throwable) {
                super.onError(e)
                Toast.makeText(this@EditProfileActivity,"画像失敗しました",Toast.LENGTH_LONG).show()
              }
            })
          }

          finish()

        }
    }

}
