package xyz.donot.quetzal.util

import android.content.Context
import android.widget.Toast
import rx.Observable
import rx.lang.kotlin.observable
import twitter4j.TwitterException


fun <T> safeTry(context: Context,body: () -> T):Observable<T> {
  return observable<T> { subscriber ->
    try {
      val toNext=body()
      subscriber.onNext(toNext)
      subscriber.onCompleted()
    } catch(ex: TwitterException) {
      loge(ex.cause.toString(),ex.stackTrace.toString())
      subscriber.onError(ex)
      fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
      }
      when (ex.errorCode) {
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
      }
    } catch(ex: Exception) {
      loge(ex.cause.toString(),ex.stackTrace.toString())
      subscriber.onError(ex)
    }
  }  .basicNetworkTask()
  }

