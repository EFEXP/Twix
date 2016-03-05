package xyz.donot.quetzal.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.klinker.android.link_builder.Link
import org.greenrobot.eventbus.EventBus
import twitter4j.TwitterException
import xyz.donot.quetzal.R
import xyz.donot.quetzal.event.OnCustomtabEvent
import xyz.donot.quetzal.event.OnHashtagEvent
import xyz.donot.quetzal.view.activity.UserActivity


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
fun Context.twitterEx(exception: TwitterException)
{
  fun toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
  }
  when(exception.errorCode)
  {
    32 -> {
      toast("ユーザーを認証できませんでした")
    }
    34 -> {
      toast("ページが見つかりませんでした")
    }
    64 -> {
      toast("あなたのアカウントは凍結されています")
    }
    88 -> {
      toast("レート制限を超えました。")
    }
    130 -> {
      toast("Twitterがダウンしています")
    }
    131 -> {
      toast("内部エラー")
    }
    135 -> {
      toast("ユーザーを認証できませんでした。")
    }
    161 -> {
      toast("今はこれ以上フォローできません。")
    }
    179 -> {
      toast("このステータスを見る権限がありません")
    }
    185 -> {
      toast("一日のTweet回数制限をオーバーしました。")
    }
    187 -> {
      toast("ステータスが重複しています。")
    }
    226 -> {
      toast("このリクエストは自動送信の疑いがあります。悪意ある行動から他のユーザを守るため、このアクションは実行されませんでした。")
    }
    261 -> {
      toast("アプリケーションに書き込み権限がありません。")
  }

}}


fun Context.getLinkList() :MutableList<Link> {

  return listOf<Link>(
    Link(Regex.MENTION_PATTERN)
      .setUnderlined(false)
      .setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
      .setBold(true)
      .setOnClickListener {
        logd("MENTION_PATTERN", "MENTION_PATTERN")
        startActivity(Intent(this, UserActivity::class.java).putExtra("user_name", it.replace("@", "")))
      }
    ,
    Link(Regex.WEB_URL_PATTERN)
      .setUnderlined(false)
      .setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
      .setBold(true)
      .setOnClickListener {
        EventBus.getDefault().post(OnCustomtabEvent(it))
      }
    ,
    Link(Regex.HASHTAG_PATTERN)
      .setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
      .setBold(true)
      .setOnClickListener {
        logd("HASHTAG_PATTERN", "HASHTAG_PATTERN")
        EventBus.getDefault().post(OnHashtagEvent(it))
      },
    Link(Regex.EMAIL_ADDRESS_PATTERN)
      .setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
      .setBold(true)
      .setOnClickListener {
        EventBus.getDefault().post(OnHashtagEvent(it))
      }
  ).toMutableList()
}
