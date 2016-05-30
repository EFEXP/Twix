package xyz.donot.quetzal.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import com.klinker.android.link_builder.Link
import twitter4j.TwitterException
import xyz.donot.quetzal.R
import xyz.donot.quetzal.util.extrautils.d
import xyz.donot.quetzal.view.activity.SearchActivity
import xyz.donot.quetzal.view.activity.UserActivity



fun Context.twitterEx(exception: TwitterException)
{
  fun toast(message: String) {
    Handler(Looper.getMainLooper()).post { toast( message)  }

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
        d("MENTION_PATTERN", it)
        startActivity(Intent(this, UserActivity::class.java).putExtra("user_name", it.replace("@","")))
      }
    ,
    Link(Regex.VALID_URL)
      .setUnderlined(false)
      .setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
      .setBold(true)
      .setOnClickListener {
        if(this is Activity){
          onCustomTabEvent(it)
        }
      }
    ,
    Link(Regex.HASHTAG_PATTERN)
      .setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
      .setBold(true)
      .setOnClickListener {
        d("HASHTAG_PATTERN",it)
        onHashTagTouched(it)
      }
  ).toMutableList()
}
fun  Context.onHashTagTouched(tag:String)
{
  startActivity(Intent(this, SearchActivity::class.java).putExtra("query_txt",tag))
}



fun Activity.onCustomTabEvent(string:String){
  CustomTabsIntent.Builder()
          .setShowTitle(true)
          .addDefaultShareMenuItem()
          .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
          .setStartAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
          .setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right).build()
          .launchUrl(this, Uri.parse(string))
}
