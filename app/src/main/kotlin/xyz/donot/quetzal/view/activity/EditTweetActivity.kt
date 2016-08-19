package xyz.donot.quetzal.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_tweet_edit.*
import kotlinx.android.synthetic.main.content_tweet_edit.*
import rx.lang.kotlin.BehaviorSubject
import twitter4j.StatusUpdate
import xyz.donot.quetzal.R
import xyz.donot.quetzal.model.realm.DBDraft
import xyz.donot.quetzal.service.TweetPostService
import xyz.donot.quetzal.util.*
import xyz.donot.quetzal.util.extrautils.hide
import xyz.donot.quetzal.util.extrautils.newIntent
import xyz.donot.quetzal.util.extrautils.onClick
import xyz.donot.quetzal.util.extrautils.show
import xyz.donot.quetzal.util.rximage.RxImagePicker
import xyz.donot.quetzal.util.rximage.Sources
import xyz.donot.quetzal.view.fragment.DraftFragment
import xyz.donot.quetzal.view.fragment.TrendFragment
import xyz.donot.quetzal.viewmodel.adapter.EditTweetPicAdapter
import java.io.File
import java.util.*

class EditTweetActivity : RxAppCompatActivity() {
    var croppingUri:Uri?= null
  val  twitter  by lazy {  getTwitterInstance() }
  val  statusId by lazy {  intent.getLongExtra("status_id",0) }
  var screenName :String=""
  val statusTxt: String by lazy { intent.getStringExtra("status_txt") }
    val mAdapter= EditTweetPicAdapter(this@EditTweetActivity)
    var dialog:DialogFragment?=null
    val hasPictures by lazy { BehaviorSubject(false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet_edit)
        if(intent.getStringExtra("user_screen_name")!=null) {
            screenName ="@${intent.getStringExtra("user_screen_name")}"
        }
            toolbar.setNavigationOnClickListener { onBackPressed() }
        val manager = LinearLayoutManager(this@EditTweetActivity).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        pic_recycler_view.layoutManager = manager
        pic_recycler_view.adapter=mAdapter
        mAdapter.setOnItemClickListener {position->
            val item=mAdapter.getItem(position)
            val color=ContextCompat.getColor(this@EditTweetActivity,R.color.colorPrimary)
                AlertDialog.Builder(this@EditTweetActivity)
                        .setTitle("写真")
                        .setMessage("何をしますか？")
                        .setPositiveButton("編集", { dialogInterface, i ->
                            croppingUri=item
                            UCrop.of(item,Uri.fromFile(File(getPictureStorePath(),"${Date().time}.jpg")))
                                    .withOptions( UCrop.Options().apply {
                                        setFreeStyleCropEnabled(true)
                                        setToolbarColor(color)
                                        setActiveWidgetColor(color)
                                        setStatusBarColor(color)
                                        setAllowedGestures(UCropActivity.SCALE, UCropActivity.SCALE,UCropActivity.SCALE);
                                    })
                                    .start(this@EditTweetActivity)
                        })
                        .setNegativeButton("削除", { dialogInterface, i ->
                            mAdapter.remove(item)
                           hasPictures.onNext(false)
                        })
                        .show(); }

        pic_recycler_view.hasFixedSize()
      //listener
        trend_hashtag.onClick {
            dialog=TrendFragment()
            dialog?.show(supportFragmentManager,"")
        }
        show_drafts.onClick {
            dialog=DraftFragment()
            dialog?.show(supportFragmentManager,"")
        }
        text_tools.onClick{
            val item=R.array.text_tools
            AlertDialog.Builder(this@EditTweetActivity)
                    .setItems(item, { dialogInterface, int ->
                        val selectedItem=resources.getStringArray(item)[int]
                        when (selectedItem) {
                         "突然の死"->{
                             val i=editText_status.text.count()-4
                             var a=""
                             var b=""
                        for(v in 0..i){
                            a +="人"
                            b += "^Y"
                        }
                             val text="＿人人人人人人$a＿\n＞ ${editText_status.text} ＜\n￣Y^Y^Y^Y^Y$b￣"
                             editText_status.text.clear()
                             editText_status.setText(text)                         }
                        }
                    })
                    .show()
        }
      use_camera.setOnClickListener {
          if(pic_recycler_view.layoutManager.itemCount<4
                  && ContextCompat.checkSelfPermission(this@EditTweetActivity,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                  ==PackageManager.PERMISSION_GRANTED) {
            RxImagePicker.with(applicationContext).requestImage(Sources.CAMERA)
              .subscribe { it.let {addPhotos(it)}
              }
          }
      }
      add_picture.setOnClickListener {
          if(pic_recycler_view.layoutManager.itemCount<4)
          {
              RxImagePicker.with(applicationContext).requestImage(Sources.GALLERY)
              .subscribe {
                  addPhotos(it)
              }
      }}
//Set
      editText_status.setText("$screenName")
      reply_for_status.text=statusTxt
        editText_status.setSelection(editText_status.editableText.count())
      send_status.setOnClickListener{
          if(editText_status.text.count()<=140){
        val updateStatus= StatusUpdate(editText_status.text.toString())
          updateStatus.inReplyToStatusId=statusId
          val filePathList =ArrayList<String>()
          mAdapter.allData.forEach { filePathList.add(getPath(this@EditTweetActivity,it)!!) }

          startService(newIntent<TweetPostService>()
                  .putExtra("StatusUpdate",updateStatus.getSerialized())
                  .putStringArrayListExtra("FilePath",filePathList))
          finish()
      }}

    hasPictures.subscribe {
        if(it){
            pic_quality_seekBar.show()
        }
        else{
            pic_quality_seekBar.hide()
        }
    }
    }
  override fun onActivityResult(requestCode:Int , resultCode: Int, data: Intent?){
    if (resultCode == AppCompatActivity.RESULT_OK &&data!=null) {
      when(requestCode)
      {
          UCrop.REQUEST_CROP->{
              val resultUri = UCrop.getOutput(data)
              mAdapter.insertWithPosition(croppingUri!!,resultUri!!)
          }
    }
  }}

    fun changeToDraft(draft: DBDraft){
        editText_status.editableText.clear()
        editText_status.append(draft.text)
        dialog?.dismiss()
        dialog=null
    }
    fun addTrendHashtag(string: String){
        editText_status.append(" $string")
        dialog?.dismiss()
        dialog=null
    }
  fun addPhotos(uri: Uri){
     mAdapter.add(uri)
     hasPictures.onNext(true)
    }

    override fun onBackPressed() {
        if(!editText_status.editableText.isBlank()&&!editText_status.editableText.isEmpty()) {
            AlertDialog.Builder(this@EditTweetActivity)
                    .setTitle("戻る")
                    .setMessage("下書きに保存しますか？")
                    .setPositiveButton("はい", { dialogInterface, i ->
                        Realm.getDefaultInstance().executeTransaction {
                         it.createObject(DBDraft::class.java).apply {
                             text=editText_status.text.toString()
                             replyToScreenName=screenName
                             replyToStatusId=statusId
                             accountId= getMyId()
                         }
                        }
                        super.onBackPressed() })
                    .setNegativeButton("いいえ", { dialogInterface, i -> super.onBackPressed() })
                    .show()
        }
        else{
            super.onBackPressed()
        }
    }

}
