package xyz.donot.quetzal.view.adapter

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import twitter4j.Status
import xyz.donot.quetzal.R
import xyz.donot.quetzal.databinding.ItemBitmapBinding
import xyz.donot.quetzal.util.MediaUtil
import xyz.donot.quetzal.view.activity.PictureActivity
import xyz.donot.quetzal.view.activity.VideoActivity
import java.util.*


class TweetCardPicAdapter(context: Context,val mediaList: ArrayList<String>,val status: Status)
:BasicRecyclerAdapter<xyz.donot.quetzal.view.adapter.TweetCardPicAdapter.ViewHolder,String>(context,mediaList) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(mInflater.inflate(R.layout.item_bitmap, viewGroup, false))
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        if (list.size > i ) {
            val item = list[i]
            viewHolder.binding.apply {
                textView.text="${i+1}/${list.size}"
                Picasso.with(context).load(item).into(imageView)
                imageView.setOnClickListener {
                    val videourl: String? = MediaUtil().getVideoURL(status.mediaEntities,status.extendedMediaEntities)
                    if (videourl != null) {
                        context.startActivity(Intent(context, VideoActivity::class.java).putExtra("video_url", videourl))
                    } else {
                        context.startActivity(Intent(context, PictureActivity::class.java).putStringArrayListExtra("picture_urls", mediaList))
                    }
                }
            }

        }
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemBitmapBinding
        init {
            binding= DataBindingUtil.bind(itemView)
        }
    }
}