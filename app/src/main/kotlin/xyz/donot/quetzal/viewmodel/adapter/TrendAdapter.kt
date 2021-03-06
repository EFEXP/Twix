package xyz.donot.quetzal.viewmodel.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.View
import android.view.ViewGroup
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import twitter4j.Trend
import xyz.donot.quetzal.R
import xyz.donot.quetzal.databinding.ItemTrendBinding

class TrendAdapter(context: Context)
: BasicRecyclerAdapter<xyz.donot.quetzal.viewmodel.adapter.TrendAdapter.ViewHolder, Trend>(context) {
  override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<*>? {
    val view:View =mInflater.inflate(R.layout.item_trend,parent, false)
    return ViewHolder(view)
  }
  inner class ViewHolder(itemView: View) : BaseViewHolder<Trend>(itemView) {
    override fun setData(data: Trend) {
      super.setData(data)
      binding.trend = data
    }
    val binding:ItemTrendBinding = DataBindingUtil.bind(itemView)
  }

}
