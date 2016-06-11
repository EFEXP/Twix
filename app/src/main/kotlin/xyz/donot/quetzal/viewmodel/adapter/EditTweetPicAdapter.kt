package xyz.donot.quetzal.viewmodel.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import xyz.donot.quetzal.R
import xyz.donot.quetzal.databinding.ItemBitmapBinding


class EditTweetPicAdapter(context: Context)
: BasicRecyclerAdapter<xyz.donot.quetzal.viewmodel.adapter.EditTweetPicAdapter.ViewHolder, Uri>(context) {
    override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<*>? {
        val view:View = mInflater.inflate(R.layout.item_bitmap, parent, false)
        return  ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder<Uri>(itemView) {
        val binding: ItemBitmapBinding

        init {
            binding = DataBindingUtil.bind(itemView)
        }
        override fun setData(data: Uri) {
            super.setData(data)
            binding.url = data.toString()
            binding.textView.text = "${layoutPosition + 1}/$count"
        }
    }}