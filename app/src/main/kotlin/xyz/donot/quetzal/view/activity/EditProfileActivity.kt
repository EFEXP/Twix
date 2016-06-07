package xyz.donot.quetzal.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import br.com.goncalves.pugnotification.notification.PugNotification
import com.squareup.picasso.Picasso
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import kotlinx.android.synthetic.main.content_edit_profile.*
import twitter4j.User
import xyz.donot.quetzal.R
import xyz.donot.quetzal.event.TwitterUserSubscriber
import xyz.donot.quetzal.notification.NotificationWrapper
import xyz.donot.quetzal.twitter.TwitterObservable
import xyz.donot.quetzal.twitter.TwitterUpdateObservable
import xyz.donot.quetzal.util.extrautils.longToast
import xyz.donot.quetzal.util.getMyId
import xyz.donot.quetzal.util.getPath
import xyz.donot.quetzal.util.getSerialized
import xyz.donot.quetzal.util.getTwitterInstance
import xyz.donot.quetzal.util.rximage.RxImagePicker
import xyz.donot.quetzal.util.rximage.Sources
import java.io.File
import java.util.*

class EditProfileActivity : AppCompatActivity() {

val twitter by lazy { getTwitterInstance()}
  var iconUri: Uri?=null
  var bannerUri: Uri?=null

  override fun onActivityResult(requestCode:Int , resultCode: Int, data: Intent?){
    if (resultCode == RESULT_OK&&data!=null) {
      when(requestCode)
      {
      3->{
        iconUri =UCrop.getOutput(data)
        Picasso.with(this@EditProfileActivity).load(iconUri).placeholder(R.drawable.avater_place_holder).into(icon)
      }
        4->{
          bannerUri =UCrop.getOutput(data)
          Picasso.with(this@EditProfileActivity).load(bannerUri).placeholder(R.drawable.picture_place_holder).into(profile_banner)
        }

      }
    }
  }

  override fun onBackPressed() {
     AlertDialog.Builder(this@EditProfileActivity)
            .setTitle("戻る")
            .setMessage("編集を削除して戻りますか？")
            .setPositiveButton("はい",  { dialogInterface, i ->   super.onBackPressed() })
            .setNegativeButton("いいえ",{ dialogInterface, i -> dialogInterface.cancel()})
            .show();

  }

  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
    val color=ContextCompat.getColor(this@EditProfileActivity,R.color.colorPrimary)
      toolbar.setNavigationOnClickListener { onBackPressed() }
        TwitterObservable(applicationContext,twitter).showUser(getMyId()).subscribe(object:TwitterUserSubscriber(this@EditProfileActivity){
          override fun onNext(user: User) {
            Picasso.with(this@EditProfileActivity).load(user.profileBannerIPadRetinaURL).into(profile_banner)
            Picasso.with(this@EditProfileActivity).load(user.originalProfileImageURLHttps).into(icon)
            profile_banner.setOnClickListener{
              RxImagePicker.with(this@EditProfileActivity).requestImage(Sources.GALLERY)
              .subscribe {
                bannerUri = it
                UCrop.of(bannerUri!!,Uri.fromFile(File(cacheDir,"${Date().time}.jpg")))
                        .withOptions( UCrop.Options().apply {
                          setToolbarColor(color)
                          setActiveWidgetColor(color)
                          setStatusBarColor(color)
                          setAllowedGestures(UCropActivity.SCALE, UCropActivity.SCALE, UCropActivity.SCALE);
                        })
                        .withAspectRatio(3F,1F)
                        .start(this@EditProfileActivity,4)
              }

              }
            icon.setOnClickListener{
              RxImagePicker.with(context).requestImage(Sources.GALLERY)
              .subscribe {
                iconUri = it
                UCrop.of(iconUri!!,Uri.fromFile(File(cacheDir,"${Date().time}.jpg")))
                        .withOptions( UCrop.Options().apply {
                          setToolbarColor(color)
                          setActiveWidgetColor(color)
                          setStatusBarColor(color)
                          setAllowedGestures(UCropActivity.SCALE, UCropActivity.SCALE, UCropActivity.SCALE);
                        })
                        .withAspectRatio(1F,1F)
                        .start(this@EditProfileActivity,3)
              }

            }
            web.text.insert(0,user.urlEntity.expandedURL)
            user_name.text.insert(0,user.name)
            geo.text.insert(0,user.location)
            description.text.insert(0,user.description)
          }
        })
        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
          val id=Random().nextInt(100)+1
          NotificationWrapper(this).sendingNotification(id)
          TwitterUpdateObservable(this@EditProfileActivity,twitter)
                  .updateProfileAsync(name = user_name.text.toString(),
                          location = geo.text.toString(),
                          description = description.text.toString(),
                          url = web.text.toString())
          .subscribe (object:TwitterUserSubscriber(this@EditProfileActivity){
            override fun onNext(user: User) {
              super.onNext(user)
              val bundle =  Bundle()
              bundle.putByteArray("userObject",user.getSerialized())
              setResult(RESULT_OK,Intent().putExtras(bundle));
              finish()
            }

            override fun onCompleted() {
              super.onCompleted()
              longToast("更新しました")
              PugNotification.with(this@EditProfileActivity).cancel(id)
            }
          })
          if (bannerUri != null) {
            TwitterUpdateObservable(this@EditProfileActivity,twitter)
                    .profileImageUpdateAsync(File(getPath(this@EditProfileActivity, bannerUri!!)))
          }
          if (iconUri != null) {
            TwitterUpdateObservable(this@EditProfileActivity,twitter).profileImageUpdateAsync(File(getPath(this@EditProfileActivity, iconUri!!)))
                    .subscribe (object:TwitterUserSubscriber(this@EditProfileActivity){
              override fun onCompleted() {
                super.onCompleted()
              longToast("画像更新しました")
                PugNotification.with(this@EditProfileActivity).cancel(id)
              }
            })
          }

        }
    }

}
