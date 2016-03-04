package xyz.donot.quetzal.view.adapter

import android.app.AlertDialog
import android.content.*
import android.databinding.DataBindingUtil
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import com.klinker.android.link_builder.LinkBuilder
import com.squareup.picasso.Picasso
import org.greenrobot.eventbus.EventBus
import twitter4j.Status
import twitter4j.Twitter
import xyz.donot.quetzal.R
import xyz.donot.quetzal.databinding.ItemTweetCardBinding
import xyz.donot.quetzal.event.OnCardViewTouchEvent
import xyz.donot.quetzal.event.OnCustomtabEvent
import xyz.donot.quetzal.event.TwitterSubscriber
import xyz.donot.quetzal.twitter.TwitterUpdateObservable
import xyz.donot.quetzal.util.*
import xyz.donot.quetzal.view.activity.*
import xyz.donot.quetzal.view.dialog.RetweeterDialog
import java.util.*

class StatusAdapter(private val mContext: Context, private val statusList: MutableList<Status>) : RecyclerView.Adapter<xyz.donot.quetzal.view.adapter.StatusAdapter.ViewHolder>() {
  private val mInflater: LayoutInflater by lazy { LayoutInflater.from(mContext) }
  private val twitter: Twitter by  lazy { mContext.getTwitterInstance() }
  override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
    // 表示するレイアウトを設定
    return ViewHolder(mInflater.inflate(R.layout.item_tweet_card, viewGroup, false))
  }
  enum class media{
    EX_MEDIA,
    MEDIA,
    NONE
  }
  override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
    if (statusList.size > i ) {
      val item= if (statusList[i].isRetweet){
        viewHolder.binding.textViewIsRT.visibility=View.VISIBLE
        viewHolder.binding.textViewIsRT.text="${statusList[i].user.name}がリツイート"
        statusList[i].retweetedStatus
      }else{
        viewHolder.binding.textViewIsRT.visibility=View.GONE
        statusList[i]
      }
      val type=if(item.extendedMediaEntities.isNotEmpty()){ media.EX_MEDIA }
      else if(item.mediaEntities.isNotEmpty()){ media.MEDIA }
      else{ media.NONE }

      val mediaDisplayIds=ArrayList<String>()
      val statusMediaIds=ArrayList<String>()
      when(type){
        media.NONE->{viewHolder.binding.mediaContainerGrid.visibility = View.GONE}
        media.EX_MEDIA->{statusMediaIds.addAll(item.extendedMediaEntities.map { it.mediaURLHttps })
          mediaDisplayIds.addAll(item.extendedMediaEntities.map { it.displayURL })
        }
        media.MEDIA-> {statusMediaIds.addAll(item.mediaEntities.map { it.mediaURLHttps })
          mediaDisplayIds.addAll(item.mediaEntities.map { it.displayURL })
        }
      }
      if(type!= media.NONE){
        val gridAdapter=TweetPictureGridAdapter(mContext,0)
        gridAdapter.addAll(statusMediaIds)
        viewHolder.binding.mediaContainerGrid.apply {
          adapter=gridAdapter
          onItemClickListener= AdapterView.OnItemClickListener { parent, view, position, id ->
            val videourl:String? =MediaUtil().getVideoURL(item.mediaEntities,item.extendedMediaEntities)
            if (videourl!=null) {
              context.startActivity(Intent(context, VideoActivity::class.java).putExtra("video_url", videourl))
            } else
            { context.startActivity(Intent(context, PictureActivity::class.java).putStringArrayListExtra("picture_urls", statusMediaIds)) }
          }
          visibility = View.VISIBLE
        }
      }
      //ビューホルダー
      viewHolder. binding.apply {
        if(item.isFavorited){ like.setImageResource(R.drawable.ic_favorite_pressed)}
        else{like.setImageResource(R.drawable.ic_favorite_grey)}
        if(statusList[i].isRetweeted){retweet.setImageResource(R.drawable.ic_redo_pressed)}
        else{ retweet.setImageResource(R.drawable.ic_redo_grey)}
        via.text=Html.fromHtml(item.source)
          userNameText.text = item.user.name
       screenName.text = "@${item.user.screenName}"
        textViewDate.text = getRelativeTime(item.createdAt)
        count.text= "RT:${item.retweetCount} いいね:${item.favoriteCount}"
        Picasso.with(mContext).load(item.user.originalProfileImageURLHttps).transform(RoundCorner()).into(icon)

        //cardview
        cardView.setOnClickListener({
          val tweetItem=if(getMyId() ==statusList[i].user.id){R.array.tweet_my_menu}else{R.array.tweet_menu}
          AlertDialog.Builder(mContext)
            .setItems(tweetItem, { dialogInterface, int ->
              val selectedItem=mContext.resources.getStringArray(tweetItem)[int]
              when (selectedItem) {
                "削除" -> {
                  TwitterUpdateObservable(twitter).deleteStatusAsync(statusList[i].id).subscribe (object : TwitterSubscriber(mContext) {
                    override fun onStatus(status: Status) {
                      super.onStatus(status)
                      Toast.makeText(mContext, "削除しました",  Toast.LENGTH_LONG).show()
                    }
                  })
                }
                "会話" -> {
                  mContext.startActivity(Intent(mContext, TweetDetailActivity::class.java).putExtra("status_id", statusList[i].id))
                }
                "コピー" -> {
                  val t= mContext.getSystemService(Context.CLIPBOARD_SERVICE)as ClipboardManager
                  t.primaryClip = ClipData.newPlainText(ClipDescription.MIMETYPE_TEXT_URILIST,item.text)
                }

                "RTした人"-> {
                  RetweeterDialog(item.id).show((mContext as AppCompatActivity).supportFragmentManager ,"")

                }
                "いいねした人"-> {EventBus.getDefault().post(OnCustomtabEvent("https://twitter.com/${item.user.screenName}/status/${item.id}"))}
              }


            })
            .show()
          EventBus.getDefault().post(OnCardViewTouchEvent(item))
        })
        icon.setOnClickListener{mContext.startActivity(Intent(mContext, UserActivity::class.java).putExtra("user_id",item.user.id))}
        tweetText.text=item.text
        LinkBuilder.on(tweetText).addLinks(mContext.getLinkList()).build()
        reply.setOnClickListener{
          mContext.startActivity(Intent(mContext, TweetEditActivity::class.java).putExtra("status_id",item.id).putExtra("user_screen_name",item.user.screenName))
        }
        retweet.setOnClickListener{
          if(!item.isRetweeted){
            TwitterUpdateObservable(twitter).createRetweetAsync(item.id).subscribe(object : TwitterSubscriber(mContext) {
              override fun onStatus(status: Status) {
                super.onStatus(status)
                reload(status)
                Toast.makeText(mContext, "RTしました",  Toast.LENGTH_LONG).show()
              }
            })
          }
        }
        like.setOnClickListener{
         if(!item.isFavorited){
          TwitterUpdateObservable(twitter).createLikeAsync(item.id).subscribe(
           object : TwitterSubscriber(mContext) {
             override fun onStatus(status: Status) {
               super.onStatus(status)
               reload(status)
             }
           }) }
          else{
           TwitterUpdateObservable(twitter).deleteLikeAsync(item.id).subscribe(
             object : TwitterSubscriber(mContext) {
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
  fun addAll(statuses :Iterable<Status>)
  {
    Handler(Looper.getMainLooper()).post()
    {
      statuses.forEach { statusList.add(it)
        this.notifyItemInserted(statusList.size)
      }
    }
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
    Handler(Looper.getMainLooper()).post { statusList.add(0,status)
    this.notifyItemInserted(0)}
  }
  fun clear(){
    Handler(Looper.getMainLooper()).post {  statusList.clear()
    this.notifyDataSetChanged()}
  }
  fun remove(status: Status){
    Handler(Looper.getMainLooper()).post {statusList.remove(status)
    this.notifyDataSetChanged()}
  }


  inner class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
   val binding :ItemTweetCardBinding
    init {
      binding = DataBindingUtil.bind(itemView)
    }
  }

}
