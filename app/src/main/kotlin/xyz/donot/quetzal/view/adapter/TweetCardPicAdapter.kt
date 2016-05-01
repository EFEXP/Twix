package xyz.donot.quetzal.view.adapter

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import xyz.donot.quetzal.R
import xyz.donot.quetzal.databinding.ItemBitmapBinding
import xyz.donot.quetzal.view.activity.PictureActivity
import java.util.*


class TweetCardPicAdapter(context: Context, list: ArrayList<String>)
:BasicRecyclerAdapter<xyz.donot.quetzal.view.adapter.TweetCardPicAdapter.ViewHolder,String>(context,list) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(mInflater.inflate(R.layout.item_bitmap, viewGroup, false))
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        if (list.size > i ) {
            val  item= list[i]
            viewHolder. binding.apply {
                Picasso.with(context).load(item).into(imageView)
                imageView.setOnClickListener { context.startActivity(Intent(context, PictureActivity::class.java)
                        .putStringArrayListExtra("picture_urls",list as ArrayList<String>)) }
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