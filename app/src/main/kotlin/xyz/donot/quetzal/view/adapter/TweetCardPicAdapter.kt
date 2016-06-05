package xyz.donot.quetzal.view.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.squareup.picasso.Picasso
import twitter4j.Status
import xyz.donot.quetzal.R
import xyz.donot.quetzal.databinding.ItemBitmapBinding
import xyz.donot.quetzal.util.extrautils.start
import xyz.donot.quetzal.util.getSerialized
import xyz.donot.quetzal.util.getVideoURL
import xyz.donot.quetzal.view.activity.PictureActivity
import xyz.donot.quetzal.view.activity.VideoActivity


class TweetCardPicAdapter(context: Context,val status: Status)
:BasicRecyclerAdapter<TweetCardPicAdapter.ViewHolder,String>(context) {

    override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<*>? {
        return ViewHolder(mInflater.inflate(R.layout.item_bitmap,parent, false))
    }

    inner class ViewHolder(itemView: View) :BaseViewHolder<String>(itemView) {
        val binding: ItemBitmapBinding
        init {
            binding= DataBindingUtil.bind(itemView)
        }
        override fun setData(data: String) {
            super.setData(data)

           binding.apply {
                Picasso.with(context).load(data).placeholder(R.drawable.ic_launcher).into(imageView)
                textView.text="${layoutPosition+1}/$count"
                imageView.setOnClickListener {
                    val videourl: String? = getVideoURL(status.mediaEntities, status.extendedMediaEntities)
                    if (videourl != null) {
                        context.startActivity(Intent(context, VideoActivity::class.java).putExtra("video_url", videourl))
                    } else {
                        ( context as Activity).start<PictureActivity>(Bundle().apply {
                            putInt("starts_with",layoutPosition)
                            putByteArray("status",status.getSerialized())
                        })
                    }
                }
            }
        }
    }
}