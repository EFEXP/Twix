package xyz.donot.quetzal.view.fragment


import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle.components.support.RxFragment
import uk.co.senab.photoview.PhotoViewAttacher
import xyz.donot.quetzal.R
import xyz.donot.quetzal.util.extrautils.toast
import xyz.donot.quetzal.util.getPictureStorePath
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class PictureFragment : RxFragment() {
 private  val REQUEST_WRITE:Int=1
  private   val stringURL by lazy {  arguments.getString("url") }
  private var photoAttacher:PhotoViewAttacher?=  null
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val v = inflater.inflate(R.layout.fragment_picture, container, false)
    val img = v.findViewById(R.id.photo_view_image)as ImageView
    Picasso.with(activity).load(stringURL).into(img)
    img.setOnClickListener{

    }
   photoAttacher=  PhotoViewAttacher(img)
    photoAttacher?.update()
    return v
  }

  override fun onStop() {
    super.onStop()
    photoAttacher?.cleanup()
  }

  fun SavePics(){
    if(ContextCompat.checkSelfPermission(this@PictureFragment.activity,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
      Save()
    }
    else{
      requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_WRITE)
    }
}
  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if(requestCode==REQUEST_WRITE){
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        Save()
      }
      if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
       toast("権限がないため保存できませんでした")
      }
    }
  }
  //実際のセーブ処理
  fun Save(){
    Picasso.with(activity).load( stringURL).into(object :com.squareup.picasso.Target{
      override fun onBitmapFailed(p0: Drawable?) {
      }

      override fun onBitmapLoaded(p0: Bitmap, p1: Picasso.LoadedFrom?) {
        val file  =getPictureStorePath()
        try{
          val name= Date().time
          val attachName: File = File("$file/", "$name.jpg")
          FileOutputStream(attachName).use {
            p0.compress(Bitmap.CompressFormat.JPEG,100,it)
            it.flush()
            Toast.makeText(context,"保存しました", Toast.LENGTH_LONG).show()
          }

          val values=  ContentValues().apply {
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.TITLE,"$file/$name.jpg")
            put("_data",attachName.absolutePath )

          }
          activity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        }
        catch(ex: IOException){
          ex.printStackTrace()
        }
      }
      override fun onPrepareLoad(p0: Drawable?) {

      }

    })

  }


}
