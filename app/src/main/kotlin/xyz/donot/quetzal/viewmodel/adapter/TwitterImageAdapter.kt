package xyz.donot.quetzal.viewmodel.adapter

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.view.View
import android.view.ViewGroup
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.squareup.picasso.Picasso
import xyz.donot.quetzal.R

class TwitterImageAdapter(context: Context)
:BasicRecyclerAdapter<TwitterImageAdapter.ViewHolder,String>(context) {
    override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<*>? {
        val view: View =mInflater.inflate(R.layout.item_twitter_image,parent, false)
        return ViewHolder(view)
    }
    inner class ViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {
        override fun setData(data:String) {
            super.setData(data)
            val  item= data
            Picasso.with(context).load(item).placeholder(R.drawable.picture_place_holder).into(picture)
        }
        val picture by lazy { itemView.findViewById(R.id.picture) as AppCompatImageView}
    }

}

