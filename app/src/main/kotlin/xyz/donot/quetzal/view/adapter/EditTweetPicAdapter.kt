package xyz.donot.quetzal.view.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import xyz.donot.quetzal.R
import xyz.donot.quetzal.databinding.ItemBitmapBinding


class EditTweetPicAdapter(context: Context, list: MutableList<Uri>)
:BasicRecyclerAdapter<xyz.donot.quetzal.view.adapter.EditTweetPicAdapter.ViewHolder, Uri>(context,list) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(mInflater.inflate(R.layout.item_bitmap, viewGroup, false))
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        if (list.size > i ) {
            val  item= list[i]
            viewHolder. binding.apply {
              textView.text="${i+1}/${list.size}"
              Picasso.with(context).load(item).into(imageView)
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