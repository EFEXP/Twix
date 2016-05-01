package xyz.donot.quetzal.view.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import xyz.donot.quetzal.R
import xyz.donot.quetzal.databinding.ItemBitmapBinding


class EditTweetPicAdapter(context: Context, list: MutableList<Bitmap>)
:BasicRecyclerAdapter<xyz.donot.quetzal.view.adapter.EditTweetPicAdapter.ViewHolder, Bitmap>(context,list) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(mInflater.inflate(R.layout.item_bitmap, viewGroup, false))
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        if (list.size > i ) {
            val  item= list[i]
            viewHolder. binding.apply {
              imageView.setImageBitmap(item)
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