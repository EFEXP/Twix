package xyz.donot.twix.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import com.klinker.android.link_builder.Link
import org.greenrobot.eventbus.EventBus
import xyz.donot.twix.R
import xyz.donot.twix.event.OnCustomtabEvent
import xyz.donot.twix.event.OnHashtagEvent
import xyz.donot.twix.view.activity.UserActivity


fun Context.getTimeLineLayoutId():Int
{
   return if(getSharedPreferences(getString(R.string.app_name),Context.MODE_PRIVATE).getBoolean("use_card_view",false))
   {
     R.layout.item_tweet
   }
  else{
   //  R.layout.item_tweet
  R.layout.item_tweet_card
   }
}

fun Context.getPath(uri : Uri):String
{
  contentResolver.takePersistableUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION)
  val id = DocumentsContract.getDocumentId(uri)
  val selection = "_id=?"
  val selectionArgs = arrayOf(id.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])
  contentResolver.query(
    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
    arrayOf(MediaStore.MediaColumns.DATA),
    selection, selectionArgs, null).use {
    it.moveToFirst()
    val path=  it.getString(0)
    return path
}

}

fun Context.getLinkList() :MutableList<Link>{

return  arrayOf(
  Link(Regex.MENTION_PATTERN)
    .setUnderlined(false)
    .setTextColor(ContextCompat.getColor(this,R.color.colorAccent))
    .setBold(true)
    .setOnClickListener{
      logd("MENTION_PATTERN","MENTION_PATTERN")
      startActivity(Intent(this,UserActivity::class.java).putExtra("user_name",it.replace("@","")))
    }
  ,
  Link(Regex.WEB_URL_PATTERN)
    .setUnderlined(false)
    .setTextColor(ContextCompat.getColor(this,R.color.colorAccent))
    .setBold(true)
    .setOnClickListener{
      EventBus.getDefault().post(OnCustomtabEvent(it))
    }
  ,
  Link(Regex.HASHTAG_PATTERN)
    .setTextColor(ContextCompat.getColor(this,R.color.colorAccent))
    .setBold(true)
    .setOnClickListener{
      logd("HASHTAG_PATTERN","HASHTAG_PATTERN")
      EventBus.getDefault().post(OnHashtagEvent(it))
    }
).toMutableList()
}
