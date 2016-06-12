package xyz.donot.quetzal.view.fragment


import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.davemorrissey.labs.subscaleview.ImageSource
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_picture.*
import xyz.donot.quetzal.R
import xyz.donot.quetzal.util.extrautils.toast
import xyz.donot.quetzal.util.getPictureStorePath
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class PictureFragment : Fragment() {
 private  val REQUEST_WRITE:Int=1
  private   val stringURL by lazy {  arguments.getString("url") }
  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    Picasso.with(activity).load(stringURL).into(object : com.squareup.picasso.Target {
      override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
      }

      override fun onBitmapFailed(errorDrawable: Drawable?) {
      }

      override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
        photo_view_image.setImage(ImageSource.bitmap(bitmap))
      }
    })
    photo_view_image.maxScale = 100F
  }
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val v = inflater.inflate(R.layout.fragment_picture, container, false)
    return v
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
          activity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
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
