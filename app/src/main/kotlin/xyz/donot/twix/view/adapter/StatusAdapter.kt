package xyz.donot.twix.view.adapter

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import com.klinker.android.link_builder.LinkBuilder
import com.klinker.android.link_builder.LinkConsumableTextView
import com.squareup.picasso.Picasso
import com.twitter.sdk.android.tweetui.ToggleImageButton
import org.greenrobot.eventbus.EventBus
import twitter4j.Status
import twitter4j.Twitter
import xyz.donot.twix.R
import xyz.donot.twix.event.OnCardViewTouchEvent
import xyz.donot.twix.event.TwitterSubscriber
import xyz.donot.twix.twitter.TwitterUpdateObservable
import xyz.donot.twix.util.MediaUtil
import xyz.donot.twix.util.getLinkList
import xyz.donot.twix.util.getRelativeTime
import xyz.donot.twix.util.getTwitterInstance
import xyz.donot.twix.view.activity.PictureActivity
import xyz.donot.twix.view.activity.TweetEditActivity
import xyz.donot.twix.view.activity.UserActivity
import xyz.donot.twix.view.activity.VideoActivity
import java.util.*

class StatusAdapter(private val mContext: Context, private val statusList: LinkedList<Status>) : RecyclerView.Adapter<xyz.donot.twix.view.adapter.StatusAdapter.ViewHolder>() {
  private val mInflater: LayoutInflater by lazy { LayoutInflater.from(mContext) }
  private val twitter: Twitter by  lazy { mContext.getTwitterInstance() }
  override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
    // 表示するレイアウトを設定
    return ViewHolder(mInflater.inflate(R.layout.item_tweet_card, viewGroup, false))
  }
  override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
    if (statusList.size > i ) {
      val item= if (statusList[i].isRetweet){
        viewHolder.retweetText.visibility=View.VISIBLE
        viewHolder.retweetText.text="${statusList[i].user.name}がリツイート"
        statusList[i].retweetedStatus
      }else{
        viewHolder.retweetText.visibility=View.GONE
        statusList[i]
      }
      //画像関連
      if(item.extendedMediaEntities.size>0){
        val list =item.extendedMediaEntities.map { it.mediaURLHttps }
        val gridAdapter=TweetPictureGridAdapter(mContext,0)
        item.extendedMediaEntities.iterator().forEach {
          gridAdapter.add(it.mediaURLHttps)
        }
        viewHolder.mediaContainerGrid.apply {
          adapter=gridAdapter
          onItemClickListener= AdapterView.OnItemClickListener { parent, view, position, id ->
            val videourl:String? =MediaUtil().getVideoURL(item.mediaEntities,item.extendedMediaEntities)
            if (videourl!=null) {
              context.startActivity(Intent(context, VideoActivity::class.java).putExtra("video_url", videourl))
            } else
            { context.startActivity(Intent(context, PictureActivity::class.java).putStringArrayListExtra("picture_urls", list as ArrayList<String>)) }
          }
          visibility = View.VISIBLE
        }
      }
      else{
        viewHolder.mediaContainerGrid.visibility = View.GONE
      }
      //ビューホルダー
      viewHolder.apply {
        if(item.isFavorited){ like.setImageResource(R.drawable.ic_favorite_pressed)}
        else{ like.setImageResource(R.drawable.ic_favorite_grey)}
        if(statusList[i].isRetweeted){retweet.setImageResource(R.drawable.ic_redo_pressed)}
        else{ retweet.setImageResource(R.drawable.ic_redo_grey)}
        userName.text = item.user.name
        screenName.text = "@${item.user.screenName}"
        dateText.text = getRelativeTime(item.createdAt)
        countText.text= "RT:${item.retweetCount} いいね:${item.favoriteCount}"
        Picasso.with(mContext).load(item.user.originalProfileImageURLHttps).into(icon)
        cardView.setOnClickListener({
          EventBus.getDefault().post(OnCardViewTouchEvent(item))
        //mContext.startActivity(Intent(mContext, TweetDetailActivity::class.java).putExtra("status_id",item.id))
        })
        icon.setOnClickListener{mContext.startActivity(Intent(mContext, UserActivity::class.java).putExtra("user_id",item.user.id))}
        status_text.text=item.text
        LinkBuilder.on(status_text).addLinks(mContext.getLinkList()).build()
        reply.setOnClickListener{
          mContext.startActivity(Intent(mContext, TweetEditActivity::class.java).putExtra("status_id",item.id).putExtra("user_screen_name",item.user.screenName))
        }
        retweet.setOnClickListener{
          if(!item.isRetweeted){
            TwitterUpdateObservable(twitter).createRetweetAsync(item.id).subscribe(
              object : TwitterSubscriber() {
                override fun onStatus(status: Status) {
                  super.onStatus(status)
                  reload(status)
                }
              }) }
        }
        like.setOnClickListener{
         if(!item.isFavorited){
          TwitterUpdateObservable(twitter).createLikeAsync(item.id).subscribe(
           object : TwitterSubscriber() {
             override fun onStatus(status: Status) {
               super.onStatus(status)
               reload(status)
             }
           }) }
          else{
           TwitterUpdateObservable(twitter).deleteLikeAsync(item.id).subscribe(
             object : TwitterSubscriber() {
               override fun onStatus(status: Status) {
                 super.onStatus(status)
                 reload(status)
               }
             })
         }
      }}

    }
  }

  override fun getItemCount(): Int {
    return statusList.size
  }
  fun add(status :Status)
  {
    Handler(Looper.getMainLooper()).post {  statusList.add(status)
    this.notifyItemInserted(statusList.size)}
  }
  fun reload(status :Status)
  {
    Handler(Looper.getMainLooper()).post {
      statusList.
        filter{ it.id==status.id }
        .mapNotNull {  statusList.indexOf(it) }
     .forEach {
        statusList[it] = status
        this.notifyItemChanged(it)
      }
     }
  }
  fun insert(status: Status)
  {
    Handler(Looper.getMainLooper()).post { statusList.addFirst(status)
    this.notifyItemInserted(0)}
  }

  fun clear(){
    Handler(Looper.getMainLooper()).post {  statusList.clear()
    this.notifyDataSetChanged()}
  }
  fun remove(status: Status){
    Handler(Looper.getMainLooper()).post {   statusList.remove(status)
    this.notifyDataSetChanged()}
  }


  inner class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
    val  like : ToggleImageButton
    val  retweet : ToggleImageButton
    val retweetText: TextView
    val userName: TextView
    val screenName: TextView
    val dateText: TextView
    val countText: TextView
    val icon: ImageView
    val status_text: LinkConsumableTextView
    val mediaContainerGrid: GridView
    val cardView:CardView
    val reply :  AppCompatImageButton
    init {
      retweet=itemView.findViewById(R.id.retweet)as ToggleImageButton
      like=itemView.findViewById(R.id.like)as ToggleImageButton
      cardView=itemView.findViewById(R.id.cardView)as CardView
      status_text=itemView.findViewById(R.id.tweet_text)as LinkConsumableTextView
      mediaContainerGrid=itemView.findViewById(R.id.media_container_grid)as GridView
      retweetText=itemView.findViewById(R.id.textView_isRT)as TextView
      userName = itemView.findViewById(R.id.user_name_text) as TextView
      screenName = itemView.findViewById(R.id.screen_name) as TextView
      countText = itemView.findViewById(R.id.count) as TextView
      dateText = itemView.findViewById(R.id.textView_date) as TextView
      icon =itemView.findViewById(R.id.icon) as ImageView
      reply=itemView.findViewById(R.id.reply) as AppCompatImageButton
    }
  }

}
