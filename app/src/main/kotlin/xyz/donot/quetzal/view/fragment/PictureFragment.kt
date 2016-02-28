package xyz.donot.quetzal.view.fragment


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import uk.co.senab.photoview.PhotoViewAttacher
import xyz.donot.quetzal.R
import xyz.donot.quetzal.event.OnSaveIt
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class PictureFragment : Fragment() {
  val stringURL by lazy {  arguments.getString("url") }
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    EventBus.getDefault().register(this)
  }

  override fun onDestroy() {
    super.onDestroy()
    EventBus.getDefault().unregister(this)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val v = inflater.inflate(R.layout.fragment_picture, container, false)
    val img = v.findViewById(R.id.photo_view_image)as ImageView
    Picasso.with(activity).load( stringURL).into(img)
    PhotoViewAttacher(img).update()
    return v
  }
  @Subscribe
  fun SavePics(onsave: OnSaveIt){
    Picasso.with(activity).load( stringURL).into(object :com.squareup.picasso.Target{
      override fun onBitmapFailed(p0: Drawable?) {

      }

      override fun onBitmapLoaded(p0: Bitmap, p1: Picasso.LoadedFrom?) {
      val file  = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        try{
          val name= Date().time
          val attachName:File= File("$file/","$name.jpg")
          FileOutputStream(attachName).use {
            p0.compress(Bitmap.CompressFormat.JPEG,100,it)
            it.flush()
            Toast.makeText(context,"保存しました",Toast.LENGTH_LONG).show()
          }
        }
        catch(ex: IOException){
          ex.printStackTrace()
        }
      }



      override fun onPrepareLoad(p0: Drawable?) {

      }

    })




}}
