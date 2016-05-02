package xyz.donot.quetzal.view.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_tweet_edit.*
import kotlinx.android.synthetic.main.content_tweet_edit.*
import twitter4j.StatusUpdate
import xyz.donot.quetzal.R
import xyz.donot.quetzal.twitter.TwitterUpdateObservable
import xyz.donot.quetzal.util.getTwitterInstance
import xyz.donot.quetzal.view.adapter.EditTweetPicAdapter
import java.util.*

class TweetEditActivity : RxAppCompatActivity() {
val START_CAMERA :Int=0
  val START_GALLERY :Int=1
    val list=LinkedList<Bitmap>()
  val intentGallery=
          if (Build.VERSION.SDK_INT < 19) {
            Intent(Intent.ACTION_GET_CONTENT)
                    .setType("image/*")
          } else {

            Intent(Intent.ACTION_OPEN_DOCUMENT)
                    .addCategory(Intent.CATEGORY_OPENABLE)
                    .setType("image/jpeg")
          }
  val  twitter  by lazy {  getTwitterInstance() }
  val  statusId by lazy {  intent.getLongExtra("status_id",0) }
  val screenName by lazy { intent.getStringExtra("user_screen_name") }
  val statusTxt by lazy { intent.getStringExtra("status_txt") }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet_edit)
      toolbar.setNavigationOnClickListener { finish() }
      //listener
      use_camera.setOnClickListener { startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE),START_CAMERA) }
      add_picture.setOnClickListener { startActivityForResult(intentGallery,START_GALLERY) }


      editText_status.setText("@$screenName")
      editText_status.selectionEnd
      reply_for_status.text=statusTxt
      send_status.setOnClickListener{
        val updateStatus= StatusUpdate(editText_status.text.toString())
        updateStatus.inReplyToStatusId=statusId
        TwitterUpdateObservable(this@TweetEditActivity,twitter).updateStatusAsync(updateStatus)
        .subscribe ({ Toast.makeText(this@TweetEditActivity,"送信しました",Toast.LENGTH_LONG).show()
          finish()},
          {Toast.makeText(this@TweetEditActivity,"失敗しました",Toast.LENGTH_LONG).show()
            finish()
          })

      }


    }
  override fun onActivityResult(requestCode:Int , resultCode: Int, data: Intent?){
    if (resultCode == AppCompatActivity.RESULT_OK &&data!=null) {
      val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION
      when(requestCode)
      {
      //banner
        START_CAMERA ->{
         val picData= data.extras.get("data") as Bitmap
          addPhotos(picData)
        }
       START_GALLERY->{
         contentResolver.takePersistableUriPermission( data.data, takeFlags)
         contentResolver.openInputStream(data.data).use {
        val bmp = BitmapFactory.decodeStream(it)
         addPhotos(bmp)
         }
        }

      }
    }
  }

  fun addPhotos(bitmap: Bitmap){
      list.add(bitmap)
      val adapter=EditTweetPicAdapter(this@TweetEditActivity,list)
     val manager = LinearLayoutManager(this@TweetEditActivity)
      manager.orientation = LinearLayoutManager.HORIZONTAL
      pic_recycler_view.layoutManager = manager
      pic_recycler_view.adapter=adapter
      pic_recycler_view.hasFixedSize()
    }

}
