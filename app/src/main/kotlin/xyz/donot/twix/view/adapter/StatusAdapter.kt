package xyz.donot.twix.view.adapter


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import twitter4j.Status
import xyz.donot.twix.R
import xyz.donot.twix.util.getRelativeTime
import xyz.donot.twix.view.listener.OnRecyclerListener
import java.util.*

class StatusAdapter(private val mContext: Context, private val mData: LinkedList<Status>?, private val mListener: OnRecyclerListener?) : RecyclerView.Adapter<xyz.donot.twix.view.adapter.StatusAdapter.ViewHolder>() {


    private val mInflater: LayoutInflater

    init {
        mInflater = LayoutInflater.from(mContext)
    }

  override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        // 表示するレイアウトを設定
        return ViewHolder(mInflater.inflate(R.layout.item_tweet, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        // データ表示
        if (mData != null && mData.size > i ) {
         val item= if (mData[i].isRetweet){
           viewHolder.retweetText.text="${mData[i].user.name}がリツイート"
             mData[i].retweetedStatus
           }else{mData[i]}
          viewHolder.apply {
           userName.text = item.user.name
           screenName.text = item.user.screenName
           dateText.text = getRelativeTime(item.createdAt)
           tweetText.text = item.text
           countText.text= "RT:${item.retweetCount} いいね:${item.favoriteCount}"
           Glide.with(mContext).load(item.user.originalProfileImageURLHttps).into(icon)
          }
        }
      viewHolder.itemView.setOnClickListener {mListener?.onRecyclerClicked(it, i) }
    }

    override fun getItemCount(): Int {
        if (mData != null) {
            return mData.size
        } else {
            return 0
        }
    }

    // ViewHolder
  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
      val retweetText:TextView
      val userName: TextView
      val screenName:TextView
      val tweetText:TextView
      val  dateText:TextView
      val countText:TextView
      val icon:ImageView

        init {
          retweetText=itemView.findViewById(R.id.textView_isRT)as TextView
          userName = itemView.findViewById(R.id.user_name_text) as TextView
          screenName = itemView.findViewById(R.id.screen_name) as TextView
          tweetText = itemView.findViewById(R.id.tweet_text) as TextView
          countText = itemView.findViewById(R.id.count) as TextView
         dateText = itemView.findViewById(R.id.textView_date) as TextView
          icon =itemView.findViewById(R.id.icon) as ImageView
        }

    }
}
