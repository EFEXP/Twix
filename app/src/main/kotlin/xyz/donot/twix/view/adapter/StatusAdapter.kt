package xyz.donot.twix.view.adapter


import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import org.greenrobot.eventbus.EventBus
import twitter4j.Status
import xyz.donot.twix.R
import xyz.donot.twix.event.OnCustomtabEvent
import xyz.donot.twix.util.getRelativeTime
import xyz.donot.twix.view.activity.PictureActivity
import xyz.donot.twix.view.customview.simplelinkabletext.Link
import xyz.donot.twix.view.customview.simplelinkabletext.LinkableTextView
import xyz.donot.twix.view.listener.OnRecyclerListener
import java.util.*
import java.util.regex.Pattern

 class StatusAdapter(private val mContext: Context, private val mData: LinkedList<Status>, private val mListener: OnRecyclerListener?) : RecyclerView.Adapter<xyz.donot.twix.view.adapter.StatusAdapter.ViewHolder>() {


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
        if (mData.size > i ) {
         val item= if (mData[i].isRetweet){
           viewHolder.retweetText.text="${mData[i].user.name}がリツイート"
             mData[i].retweetedStatus
           }else{
           viewHolder.retweetText.visibility=View.GONE
           mData[i]
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
                context.startActivity(Intent(context, PictureActivity::class.java).putStringArrayListExtra("picture_urls", list as ArrayList<String>)) }
              visibility = View.VISIBLE
            }

          }
          else{
            viewHolder.mediaContainerGrid.visibility = View.GONE
          }
          //ビューホルダー
          viewHolder.apply {
          val array=  arrayOf(

            Link(Pattern.compile("http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?"))
              .setUnderlined(false)
              .setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
              .setTextStyle(Link.TextStyle.BOLD)
              .setClickListener {

                EventBus.getDefault().post(OnCustomtabEvent(it))

              }
            ,

            Link(Pattern.compile("(@\\w+)"))
              .setUnderlined(false)
              .setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
              .setTextStyle(Link.TextStyle.BOLD)
              .setClickListener {



              }
            ,
              Link(Pattern.compile("(#\\w+)"))
                .setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                .setTextStyle(Link.TextStyle.BOLD)).toMutableList()

            status_text.setText(item.text).addLinks(array).build()
           userName.text = item.user.name
           screenName.text = item.user.screenName
           dateText.text = getRelativeTime(item.createdAt)
           countText.text= "RT:${item.retweetCount} いいね:${item.favoriteCount}"
           Glide.with(mContext).load(item.user.originalProfileImageURLHttps).into(icon)
          }
        }
      viewHolder.itemView.setOnClickListener {mListener?.onRecyclerClicked(it, i) }
    }

    override fun getItemCount(): Int {

            return mData.size

    }


    // ViewHolder
  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
      val retweetText:TextView
      val userName: TextView
      val screenName:TextView
      val  dateText:TextView
      val countText:TextView
      val icon:ImageView
      val status_text: LinkableTextView
      val mediaContainerGrid:GridView
        init {
          status_text=itemView.findViewById(R.id.tweet_text)as LinkableTextView
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
