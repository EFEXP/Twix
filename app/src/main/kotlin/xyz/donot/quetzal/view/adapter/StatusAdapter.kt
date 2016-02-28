package xyz.donot.quetzal.view.adapter

import android.app.AlertDialog
import android.content.*
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.klinker.android.link_builder.LinkBuilder
import com.klinker.android.link_builder.LinkConsumableTextView
import com.squareup.picasso.Picasso
import org.greenrobot.eventbus.EventBus
import twitter4j.Status
import twitter4j.Twitter
import xyz.donot.quetzal.R
import xyz.donot.quetzal.event.OnCardViewTouchEvent
import xyz.donot.quetzal.event.TwitterSubscriber
import xyz.donot.quetzal.twitter.TwitterUpdateObservable
import xyz.donot.quetzal.util.*
import xyz.donot.quetzal.view.activity.*
import java.util.*

class StatusAdapter(private val mContext: Context, private val statusList: LinkedList<Status>) : RecyclerView.Adapter<xyz.donot.quetzal.view.adapter.StatusAdapter.ViewHolder>() {
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
          val tweetItem=if(getMyId() ==item.user.id){R.array.tweet_my_menu}else{R.array.tweet_menu}
          AlertDialog.Builder(mContext)
            .setItems(tweetItem, { dialogInterface, i ->
              val selectedItem=mContext.resources.getStringArray(tweetItem)[i]
              when (selectedItem) {
                "削除" -> {
                  TwitterUpdateObservable(twitter).deleteStatusAsync(item.id).subscribe (object : TwitterSubscriber() {
                    override fun onError(e: Throwable) {
                      super.onError(e)
                      Toast.makeText(mContext, "失敗しました", Toast.LENGTH_LONG).show()
                    }
                    override fun onStatus(status: Status) {
                      super.onStatus(status)
                      Toast.makeText(mContext, "削除しました",  Toast.LENGTH_LONG).show()
                    }
                  })
                }
                "会話" -> {
                  mContext.startActivity(Intent(mContext, TweetDetailActivity::class.java).putExtra("status_id", item.id))
                }
                "コピー" -> {
                  val t= mContext.getSystemService(Context.CLIPBOARD_SERVICE)as ClipboardManager
                  t.primaryClip = ClipData.newPlainText(ClipDescription.MIMETYPE_TEXT_URILIST,item.text)
                }
              }


            })
            .show()


          EventBus.getDefault().post(OnCardViewTouchEvent(item))
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
    val  like : AppCompatImageButton
    val  retweet : AppCompatImageButton
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
      retweet=itemView.findViewById(R.id.retweet)as AppCompatImageButton
      like=itemView.findViewById(R.id.like)as AppCompatImageButton
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
