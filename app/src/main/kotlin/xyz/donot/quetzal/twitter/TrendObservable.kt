package xyz.donot.quetzal.twitter

import android.content.Context
import rx.Observable
import twitter4j.Trend
import twitter4j.Twitter
import xyz.donot.quetzal.util.safeTry

class TwitterTrendObservable(val context: Context,val twitter: Twitter){
  fun  getTrend():Observable<Array<Trend>>{
    return safeTry(context){twitter.getPlaceTrends(23424856).trends}
  }
}
