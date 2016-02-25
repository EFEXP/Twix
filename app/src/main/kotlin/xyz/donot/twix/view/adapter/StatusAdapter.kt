package xyz.donot.twix.view.adapter

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
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
import twitter4j.Status
import xyz.donot.twix.R
import xyz.donot.twix.util.MediaUtil
import xyz.donot.twix.util.getLinkList
import xyz.donot.twix.util.getRelativeTime
import xyz.donot.twix.view.activity.PictureActivity
import xyz.donot.twix.view.activity.TweetDetailActivity
import xyz.donot.twix.view.activity.UserActivity
import xyz.donot.twix.view.activity.VideoActivity
import java.util.*

class StatusAdapter(private val mContext: Context, private val statusList: LinkedList<Status>) : RecyclerView.Adapter<xyz.donot.twix.view.adapter.StatusAdapter.ViewHolder>() {
  private val mInflater: LayoutInflater by lazy { LayoutInflater.from(mContext) }

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
        userName.text = item.user.name
        screenName.text = item.user.screenName
        dateText.text = getRelativeTime(item.createdAt)
        countText.text= "RT:${item.retweetCount} いいね:${item.favoriteCount}"
        Picasso.with(mContext).load(item.user.originalProfileImageURLHttps).into(icon)
        cardView.setOnClickListener({ mContext.startActivity(Intent(mContext, TweetDetailActivity::class.java).putExtra("status_id",item.id)) })
        icon.setOnClickListener{mContext.startActivity(Intent(mContext, UserActivity::class.java).putExtra("user_id",item.user.id))}
        status_text.text=item.text
        LinkBuilder.on(status_text).addLinks(mContext.getLinkList()).build()

      }}
  }

  override fun getItemCount(): Int {
    return statusList.size
  }
  fun add(status :Status)
  {
    Handler(Looper.getMainLooper()).post {  statusList.add(status)
    this.notifyItemInserted(statusList.size)}
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
    val retweetText: TextView
    val userName: TextView
    val screenName: TextView
    val  dateText: TextView
    val countText: TextView
    val icon: ImageView
    val status_text: LinkConsumableTextView
    val mediaContainerGrid: GridView
    val cardView:CardView
    init {
      cardView=itemView.findViewById(R.id.cardView)as CardView
      status_text=itemView.findViewById(R.id.tweet_text)as LinkConsumableTextView
      mediaContainerGrid=itemView.findViewById(R.id.media_container_grid)as GridView
      retweetText=itemView.findViewById(R.id.textView_isRT)as TextView
      userName = itemView.findViewById(R.id.user_name_text) as TextView
      screenName = itemView.findViewById(R.id.screen_name) as TextView
      countText = itemView.findViewById(R.id.count) as TextView
      dateText = itemView.findViewById(R.id.textView_date) as TextView
      icon =itemView.findViewById(R.id.icon) as ImageView
    }

  }

}
