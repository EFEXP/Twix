package xyz.donot.twix.util

import android.content.Context
import android.support.v4.content.ContextCompat
import com.klinker.android.link_builder.Link
import org.greenrobot.eventbus.EventBus
import xyz.donot.twix.R
import xyz.donot.twix.event.OnCustomtabEvent
import xyz.donot.twix.event.OnHashtagEvent


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

fun Context.getDetailHeaderLayoutId():Int
{
  return R.layout.item_detail_tweet
}
fun Context.getLinkList() :MutableList<Link>{

return  arrayOf(

  Link(Regex.MENTION_PATTERN)
    .setUnderlined(false)
    .setTextColor(ContextCompat.getColor(this,R.color.colorAccent))
    .setBold(true)
    .setOnClickListener{
      logd("MENTION_PATTERN","MENTION_PATTERN")
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
