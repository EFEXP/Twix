package xyz.donot.quetzal.view.adapter

import android.app.AlertDialog
import android.content.*
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.text.Html
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

class StatusAdapter(context: Context,  list: MutableList<Status>) : BasicRecyclerAdapter<xyz.donot.quetzal.view.adapter.StatusAdapter.ViewHolder,Status>(context,list) {
  private val twitter: Twitter by  lazy { context.getTwitterInstance() }
  override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
    return ViewHolder(mInflater.inflate(R.layout.item_tweet_card, viewGroup, false))
  }
  enum class media{
    EX_MEDIA,
    MEDIA,
    NONE
  }
  override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
    if (list.size > i ) {
      val item= if (list[i].isRetweet){
        viewHolder.binding.textViewIsRT.visibility=View.VISIBLE
        viewHolder.binding.textViewIsRT.text="${list[i].user.name}がリツイート"
        list[i].retweetedStatus
      }else{
        viewHolder.binding.textViewIsRT.visibility=View.GONE
        list[i]
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
        val gridAdapter=TweetPictureGridAdapter(context,0)
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
        if(list[i].isRetweeted){retweet.setImageResource(R.drawable.ic_redo_pressed)}
        else{ retweet.setImageResource(R.drawable.ic_redo_grey)}
        via.text=Html.fromHtml(item.source)
          userNameText.text = item.user.name
       screenName.text = "@${item.user.screenName}"
        textViewDate.text = getRelativeTime(item.createdAt)
        count.text= "RT:${item.retweetCount} いいね:${item.favoriteCount}"
        Picasso.with(context).load(item.user.originalProfileImageURLHttps).transform(RoundCorner()).into(icon)
        //cardview
        cardView.setOnClickListener({
          val tweetItem=if(getMyId() ==list[i].user.id){R.array.tweet_my_menu}else{R.array.tweet_menu}
          AlertDialog.Builder(context)
            .setItems(tweetItem, { dialogInterface, int ->
              val selectedItem=context.resources.getStringArray(tweetItem)[int]
              when (selectedItem) {
                "削除" -> {
                  TwitterUpdateObservable(twitter).deleteStatusAsync(list[i].id).subscribe (object : TwitterSubscriber(context) {
                    override fun onStatus(status: Status) {
                      super.onStatus(status)
                      Toast.makeText(context, "削除しました",  Toast.LENGTH_LONG).show()
                    }
                  })
                }
                "会話" -> {
                  context.startActivity(Intent(context, TweetDetailActivity::class.java).putExtra("status_id", list[i].id))
                }
                "コピー" -> {
                  val t= context.getSystemService(Context.CLIPBOARD_SERVICE)as ClipboardManager
                  t.primaryClip = ClipData.newPlainText(ClipDescription.MIMETYPE_TEXT_URILIST,item.text)
                }

                "RTした人"-> {
                  RetweeterDialog(item.id).show((context as AppCompatActivity).supportFragmentManager ,"")

                }
                "いいねした人"-> {EventBus.getDefault().post(OnCustomtabEvent("https://twitter.com/${item.user.screenName}/status/${item.id}"))}
              }


            })
            .show()
          EventBus.getDefault().post(OnCardViewTouchEvent(item))
        })
        icon.setOnClickListener{context.startActivity(Intent(context, UserActivity::class.java).putExtra("user_id",item.user.id))}
        tweetText.text=item.text
        LinkBuilder.on(tweetText).addLinks(context.getLinkList()).build()
        reply.setOnClickListener{
          context.startActivity(Intent(context, TweetEditActivity::class.java).putExtra("status_id",item.id).putExtra("user_screen_name",item.user.screenName))
        }
        retweet.setOnClickListener{
          if(!item.isRetweeted){
            TwitterUpdateObservable(twitter).createRetweetAsync(item.id).subscribe(object : TwitterSubscriber(context) {
              override fun onStatus(status: Status) {
                super.onStatus(status)
                reload(status)
                Toast.makeText(context, "RTしました",  Toast.LENGTH_LONG).show()
              }
            })
          }
        }
        like.setOnClickListener{
         if(!item.isFavorited){
          TwitterUpdateObservable(twitter).createLikeAsync(item.id).subscribe(
           object : TwitterSubscriber(context) {
             override fun onStatus(status: Status) {
               super.onStatus(status)
               reload(status)
             }
           }) }
          else{
           TwitterUpdateObservable(twitter).deleteLikeAsync(item.id).subscribe(
             object : TwitterSubscriber(context) {
               override fun onStatus(status: Status) {
                 super.onStatus(status)
                 reload(status)
               }
             })
         }
      }}

    }
  }
  inner class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
   val binding :ItemTweetCardBinding
    init {
      binding = DataBindingUtil.bind(itemView)
    }
  }

}
