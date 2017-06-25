package xyz.donot.quetzal.viewmodel.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.klinker.android.link_builder.LinkBuilder
import com.squareup.picasso.Picasso
import twitter4j.Status
import twitter4j.Twitter
import xyz.donot.quetzal.R
import xyz.donot.quetzal.databinding.ItemTweetCardBinding
import xyz.donot.quetzal.event.TwitterSubscriber
import xyz.donot.quetzal.twitter.TwitterUpdateObservable
import xyz.donot.quetzal.util.*
import xyz.donot.quetzal.util.extrautils.*
import xyz.donot.quetzal.view.activity.EditTweetActivity
import xyz.donot.quetzal.view.activity.TweetDetailActivity
import xyz.donot.quetzal.view.activity.UserActivity
import xyz.donot.quetzal.view.fragment.RetweeterDialog

class StatusAdapter(context: Context) : BasicRecyclerAdapter<StatusAdapter.ViewHolder,Status>(context) {
    override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<*>? {
        return ViewHolder(mInflater.inflate(R.layout.item_tweet_card, parent, false))
    }

    private val twitter: Twitter by  lazy { getTwitterInstance() }

  enum class media{
    NONE
  }

  inner class ViewHolder(itemView: View) :BaseViewHolder<Status>(itemView) {
   val binding : ItemTweetCardBinding = DataBindingUtil.bind(itemView)
      override fun setData(data: Status) {
          super.setData(data)
          val item= if (data.isRetweet){
              binding.textViewIsRT.show()
              binding.textViewIsRT.text="${data.user.name}がリツイート"
              data.retweetedStatus
          }else{
              binding.textViewIsRT.hide()
              data
          }
         binding.status=item
          //mediaType
          val statusMediaIds=getImageUrls(item)
          if(statusMediaIds.isNotEmpty()){
              val mAdapter=TweetCardPicAdapter(context,item)
              val manager = LinearLayoutManager(context).apply {
                  orientation = LinearLayoutManager.HORIZONTAL
              }
             binding.tweetCardRecycler.apply {
                  adapter=mAdapter
                  layoutManager=manager
                  visibility = View.VISIBLE
                  hasFixedSize()
              }
              mAdapter.addAll(statusMediaIds)
          }
          else{
              binding.tweetCardRecycler.visibility = View.GONE
          }
          //ビューホルダー
          binding.apply {
              //引用
              if(item.quotedStatus!=null){
                  itemQuotedTweet.visibility= View.VISIBLE
                  val q=item.quotedStatus
                  quotedCardview.setOnClickListener { (context as Activity).start<TweetDetailActivity>(Bundle { putLong("status_id", q.id) }) }
                  quotedUserName.text=q.user.name
                  quotedScreenName.text="@${q.user.screenName}"
                  quotedText.text=q.text
                  Picasso.with(context).load(q.user.profileImageURLHttps).transform(RoundCorner()).into(quotedIcon)
              }else{
                  itemQuotedTweet.visibility=View.GONE
              }
              via.text=getClientName(item.source)
              screenName.text = "@${item.user.screenName}"
              textViewDate.text = getRelativeTime(item.createdAt)
              count.text= "RT:${item.retweetCount} いいね:${item.favoriteCount}"
              //cardview
              cardView.setOnClickListener({
                  if( !(context as Activity).isFinishing){
                      val tweetItem=if(getMyId() ==data.user.id){R.array.tweet_my_menu}else{R.array.tweet_menu}
                      AlertDialog.Builder(context)
                              .setItems(tweetItem, { _, int ->
                                  val selectedItem=context.resources.getStringArray(tweetItem)[int]
                                  when (selectedItem) {
                                      "削除" -> {
                                          TwitterUpdateObservable(context,twitter).deleteStatusAsync(data.id).subscribe (object : TwitterSubscriber(context) {
                                              override fun onStatus(status: Status) {
                                                  super.onStatus(status)
                                                  context.longToast("削除しました")
                                              }
                                          })
                                      }
                                      "会話" -> {
                                          if(context is Activity) {
                                              (context as Activity).start<TweetDetailActivity>(Bundle().apply { putLong("status_id", data.id) })
                                          }
                                      }
                                      "コピー" -> {
                                          (context.getSystemService(Context.CLIPBOARD_SERVICE)as ClipboardManager)
                                                  .primaryClip = ClipData.newPlainText(ClipDescription.MIMETYPE_TEXT_URILIST,item.text)
                                      }

                                      "RTした人"-> {
                                          val fragment=RetweeterDialog()
                                          fragment.apply { Bundle().apply { putLong("statusId",item.id) } }
                                          fragment.show((context as AppCompatActivity).supportFragmentManager ,"")
                                      }
                                      "共有"-> {
                                          context. startActivity( Intent().apply {
                                              action = Intent.ACTION_SEND
                                              type = "text/plain"
                                              putExtra(Intent.EXTRA_TEXT,"@${item.user.screenName}さんのツイート https://twitter.com/${item.user.screenName}/status/${item.id}をチェック")
                                          })
                                      }
                                      "公式で見る"-> {
                                          if(context is Activity){
                                             (context as Activity).onCustomTabEvent("https://twitter.com/${item.user.screenName}/status/${item.id}")
                                          }
                                      }
                                  }


                              })
                              .show()
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
                      TwitterUpdateObservable(context,twitter).createRetweetAsync(item.id)
                              .subscribe(object : TwitterSubscriber(context) {
                          override fun onStatus(status: Status) {
                              super.onStatus(status)
                              insertWithPosition(data,status)
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
                                       insertWithPosition(data,status)
                                  }
                              }) }
                  else{
                      TwitterUpdateObservable(context,twitter).deleteLikeAsync(item.id).subscribe(
                              object : TwitterSubscriber(context) {
                                  override fun onStatus(status: Status) {
                                      super.onStatus(status)
                                      insertWithPosition(item,status)
                                  }
                              })
                  }
              }}
      }

  }

}
