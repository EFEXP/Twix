package xyz.donot.quetzal.view.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import twitter4j.Trend
import xyz.donot.quetzal.BR
import xyz.donot.quetzal.R
import xyz.donot.quetzal.databinding.ItemTrendBinding

class TrendAdapter(context: Context, list: MutableList<Trend>)
:BasicRecyclerAdapter<xyz.donot.quetzal.view.adapter.TrendAdapter.ViewHolder,Trend>(context,list) {

  override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): TrendAdapter.ViewHolder {
    val view:View =mInflater.inflate(R.layout.item_trend, viewGroup, false)
    view.setOnClickListener(this)
    return ViewHolder(view)
  }
  override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

    if (list.size > i ) {
      val  item= list[i]
      viewHolder. binding.apply {
        setVariable(BR.trend,item)
        }
      }
    }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding:ItemTrendBinding
    init {
      binding=DataBindingUtil.bind(itemView)
    }
  }

}
