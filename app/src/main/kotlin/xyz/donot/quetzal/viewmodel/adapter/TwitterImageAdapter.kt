package xyz.donot.quetzal.viewmodel.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.View
import android.view.ViewGroup
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import xyz.donot.quetzal.R
import xyz.donot.quetzal.databinding.ItemTwitterImageBinding

class TwitterImageAdapter(context: Context)
:BasicRecyclerAdapter<TwitterImageAdapter.ViewHolder,String>(context) {
    override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<*>? {
        val view: View =mInflater.inflate(R.layout.item_twitter_image,parent, false)
        return ViewHolder(view)
    }
    inner class ViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {
        override fun setData(data:String) {
            super.setData(data)
            binding.url = data
        }
        val binding = DataBindingUtil.bind<ItemTwitterImageBinding>(itemView)

    }

}

