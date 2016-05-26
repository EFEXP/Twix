package xyz.donot.quetzal.view.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
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
import xyz.donot.quetzal.util.extrautils.hide
import xyz.donot.quetzal.util.extrautils.longToast
import xyz.donot.quetzal.util.extrautils.show
import xyz.donot.quetzal.util.extrautils.start
import xyz.donot.quetzal.view.activity.EditTweetActivity
import xyz.donot.quetzal.view.activity.TweetDetailActivity
import xyz.donot.quetzal.view.activity.UserActivity
import xyz.donot.quetzal.view.dialog.RetweeterDialog

class StatusAdapter(val context: Context,val  list: MutableList<Status>) : BasicRecyclerAdapter<StatusAdapter.ViewHolder,Status>(context,list) {
  private val twitter: Twitter by  lazy { getTwitterInstance() }
  override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
    return ViewHolder(mInflater.inflate(R.layout.item_tweet_card, viewGroup, false))
  }
  enum class media{
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

      //mediaType
        val statusMediaIds=getImageUrls(item)
      if(statusMediaIds.isNotEmpty()){
          val manager = LinearLayoutManager(context).apply {
              orientation = LinearLayoutManager.HORIZONTAL
          }
          viewHolder.binding.tweetCardRecycler.apply {
              adapter=TweetCardPicAdapter(context,statusMediaIds,item)
              layoutManager=manager
              visibility = View.VISIBLE
              hasFixedSize()
          }
      }
        else{
          viewHolder.binding.tweetCardRecycler.visibility = View.GONE
      }
      //ビューホルダー
      viewHolder.binding.apply {
          if(item.text.contains("\\(")||item.text.contains("\\[")){
              mathView.config("""MathJax.Hub.Config({ tex2jax: {inlineMath: [ ['$','$'], ['\\(','\\)'] ], processEscapes: true},
                            TeX: {equationNumbers: {autoNumber: "AMS"}}});""");
              mathView.text=item.text
              mathView.setBackgroundColor(Color.WHITE)
              mathView.show()
          }
          else{
              mathView.hide()
          }
          //引用
          if(item.quotedStatus!=null){
             itemQuotedTweet.visibility= View.VISIBLE
              val q=item.quotedStatus
             quotedUserName.text=q.user.name
           quotedScreenName.text="@${q.user.screenName}"
             quotedText.text=q.text
              Picasso.with(context).load(q.user.profileImageURLHttps).transform(RoundCorner()).into(quotedIcon)
          }else{
           itemQuotedTweet.visibility=View.GONE
          }
        if(item.isFavorited){ like.setImageResource(R.drawable.ic_favorite_pressed_24dp)}
        else{like.setImageResource(R.drawable.ic_favorite_grey_400_24dp)}
        if(list[i].isRetweeted){retweet.setImageResource(R.drawable.ic_retweet_pressed_24dp)}
        else{ retweet.setImageResource(R.drawable.ic_retweet_grey_400_24dp)}
          if(item.user.isProtected){

              val re= ResourcesCompat.getDrawable(context.resources,R.drawable.ic_lock_grey_400_18dp,null)
              via.setCompoundDrawablesWithIntrinsicBounds(null,null,re,null)
          }
          else{
              via.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null)
          }
        via.text=getClientName(item.source)
          userNameText.text = item.user.name
        screenName.text = "@${item.user.screenName}"
        textViewDate.text = getRelativeTime(item.createdAt)
        count.text= "RT:${item.retweetCount} いいね:${item.favoriteCount}"
        Picasso.with(context).load(item.user.originalProfileImageURLHttps).transform(RoundCorner()).into(icon)
        //cardview
        cardView.setOnClickListener({
         if( !(context as Activity).isFinishing){
          val tweetItem=if(getMyId() ==list[i].user.id){R.array.tweet_my_menu}else{R.array.tweet_menu}
          AlertDialog.Builder(context)
            .setItems(tweetItem, { dialogInterface, int ->
              val selectedItem=context.resources.getStringArray(tweetItem)[int]
              when (selectedItem) {
                "削除" -> {
                  TwitterUpdateObservable(context,twitter).deleteStatusAsync(list[i].id).subscribe (object : TwitterSubscriber(context) {
                    override fun onStatus(status: Status) {
                      super.onStatus(status)
                    context.longToast("削除しました")
                    }
                  })
                }
                "会話" -> {

                  context.start<TweetDetailActivity>(Bundle().apply { putLong("status_id", list[i].id) })

                }
                "コピー" -> {
                (context.getSystemService(Context.CLIPBOARD_SERVICE)as ClipboardManager)
                  .primaryClip = ClipData.newPlainText(ClipDescription.MIMETYPE_TEXT_URILIST,item.text)
                }

                "RTした人"-> {
                  RetweeterDialog(item.id).show((context as AppCompatActivity).supportFragmentManager ,"")

                }
                "共有"-> {
                  context. startActivity( Intent().apply {
                    action = Intent.ACTION_SEND
                      type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT,"@${item.user.screenName}さんのツイート https://twitter.com/${item.user.screenName}/status/${item.id}をチェック")
                  })
                }
                  "公式で見る"-> {EventBus.getDefault().post(OnCustomtabEvent("https://twitter.com/${item.user.screenName}/status/${item.id}"))}
              }


            })
            .show()
          EventBus.getDefault().post(OnCardViewTouchEvent(item))
        }})
        icon.setOnClickListener{context.startActivity(Intent(context, UserActivity::class.java).putExtra("user_id",item.user.id))}
        tweetText.text=getExpandedText(status = item)
        LinkBuilder.on(tweetText).addLinks(context.getLinkList()).build()
        reply.setOnClickListener{
       val bundle=   Bundle().apply {
            putString("status_txt",item.text)
            putLong("status_id",item.id)
            putString("user_screen_name",item.user.screenName)
          }
          (context as Activity).start<EditTweetActivity>(bundle)
        }
        retweet.setOnClickListener{
          if(!item.isRetweeted){
            TwitterUpdateObservable(context,twitter).createRetweetAsync(item.id).subscribe(object : TwitterSubscriber(context) {
              override fun onStatus(status: Status) {
                super.onStatus(status)
                reload(status)
                context.longToast("RTしました")
              }
            })
          }
        }
          like.setOnClickListener{
              if(!item.isFavorited){
                  TwitterUpdateObservable(context,twitter).createLikeAsync(item.id).subscribe(
                          object : TwitterSubscriber(context) {
                              override fun onStatus(status: Status) {
                                  super.onStatus(status)
                                  reload(status)
                              }
                          }) }
              else{
                  TwitterUpdateObservable(context,twitter).deleteLikeAsync(item.id).subscribe(
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
