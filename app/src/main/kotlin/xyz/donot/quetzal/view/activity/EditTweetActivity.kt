package xyz.donot.quetzal.view.activity

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import twitter4j.StatusUpdate
import xyz.donot.quetzal.R
import xyz.donot.quetzal.model.DBDraft
import xyz.donot.quetzal.service.TweetPostService
import xyz.donot.quetzal.util.*
import xyz.donot.quetzal.view.adapter.BasicRecyclerAdapter
import xyz.donot.quetzal.view.adapter.EditTweetPicAdapter
import xyz.donot.quetzal.view.fragment.DraftFragment
import xyz.donot.quetzal.view.fragment.TrendFragment
import java.io.File
import java.util.*

class EditTweetActivity : RxAppCompatActivity() {
    val START_CAMERA :Int=0
  val START_GALLERY :Int=1
    val list=LinkedList<Uri>()
  val intentGallery= Intent().setAction(Intent.ACTION_PICK).setType("image/*")
    var m_uri:Uri?= null
    var croppingUri:Uri?= null
  val  twitter  by lazy {  getTwitterInstance() }
  val  statusId by lazy {  intent.getLongExtra("status_id",0) }
  var screenName :String=""
  val statusTxt by lazy { intent.getStringExtra("status_txt") }
    val mAdapter= EditTweetPicAdapter(this@EditTweetActivity, list)
    var dialog:DialogFragment?=null


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
        mAdapter.setOnItemClickListener(object: BasicRecyclerAdapter.OnItemClickListener<EditTweetPicAdapter.ViewHolder, Uri>{
            val color=ContextCompat.getColor(this@EditTweetActivity,R.color.colorPrimary)
            override fun onItemClick(adapter: BasicRecyclerAdapter<EditTweetPicAdapter.ViewHolder, Uri>, position: Int, item: Uri) {
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
                        })
                        .show(); }
        })
        pic_recycler_view.hasFixedSize()
      //listener
       trend_hashtag.setOnClickListener {
           dialog=TrendFragment()
           dialog?.show(supportFragmentManager,"")
       }
        show_drafts.setOnClickListener {
            dialog=DraftFragment()
            dialog?.show(supportFragmentManager,"")
        }
        text_tools.setOnClickListener {
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
                             val text="＿人人人人人人$a＿\n＞　${editText_status.text}　＜\n￣Y^Y^Y^Y^Y$b￣"
                             editText_status.text.clear()
                             editText_status.setText(text)                         }

                            "e=mc^2"-> {
                                editText_status.setText("\\[e = mc^2\\]")
                            }
                            "極限"-> {
                                editText_status.setText(" \\[ \\lim_{h \\to 0} \\frac{\\sqrt{a+h}-\\sqrt{a}}{h} \\]")
                            }
                            "ベクトル"-> {
                                editText_status.setText("\\(\\vec a \\cdot \\vec b \\)")
                            }
                            "積分"->{
                                editText_status.setText("\\(  \\int_{0}^{R} mg dx =\\lim_{n \\to \\infty} \\sum_{i=1}^n mg \\Delta x_{i}  \\)")
                            }
                            "微分"->{
                                editText_status.setText("\\[f'(x),f''(x),f^{(3)}(x)\\]")
                            }

                        }
                    })
                    .show()
        }
      use_camera.setOnClickListener {
          if(pic_recycler_view.layoutManager.itemCount<4
                  && ContextCompat.checkSelfPermission(this@EditTweetActivity,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                  ==PackageManager.PERMISSION_GRANTED) {
              val photoName = "${System.currentTimeMillis()}.jpg"
              val contentValues = ContentValues().apply {
                  put(MediaStore.Images.Media.TITLE, photoName)
                  put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
              }
              m_uri = contentResolver
                      .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
              val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply { putExtra(MediaStore.EXTRA_OUTPUT, m_uri) }
              startActivityForResult(intentCamera, START_CAMERA)
          }
      }
      add_picture.setOnClickListener {
          if(pic_recycler_view.layoutManager.itemCount<4) {
              startActivityForResult(intentGallery,START_GALLERY)} }

//Set
      editText_status.setText("$screenName")
      reply_for_status.text=statusTxt
        editText_status.setSelection(editText_status.editableText.count())
      send_status.setOnClickListener{
          if(editText_status.text.count()<=140){
        val updateStatus= StatusUpdate(editText_status.text.toString())
          updateStatus.inReplyToStatusId=statusId
          val filePathList =ArrayList<String>()
          list.forEach { filePathList.add(getPath(this@EditTweetActivity,it)!!) }
          startService(Intent(this@EditTweetActivity, TweetPostService::class.java)
                  .putExtra("StatusUpdate",updateStatus.getSerialized())
                  .putStringArrayListExtra("FilePath",filePathList))

          finish()
      }}


    }
  override fun onActivityResult(requestCode:Int , resultCode: Int, data: Intent?){
    if (resultCode == AppCompatActivity.RESULT_OK &&data!=null) {
      when(requestCode)
      {
          UCrop.REQUEST_CROP->{
             val resultUri = UCrop.getOutput(data)
             mAdapter.insertWithPosition(croppingUri!!,resultUri!!)
          }
        START_CAMERA ->{
           val resultUri = data.data ?:m_uri
            resultUri.let {   addPhotos(it!!) }
        }
       START_GALLERY->{
           addPhotos(data.data)
        }

      }
    }
  }

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
      list.add(uri)
      mAdapter.notifyItemInserted(list.size)
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
