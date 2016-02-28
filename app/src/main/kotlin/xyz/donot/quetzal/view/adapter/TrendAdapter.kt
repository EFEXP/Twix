package xyz.donot.quetzal.view.adapter

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import twitter4j.Trend
import xyz.donot.quetzal.R
import xyz.donot.quetzal.view.activity.SearchActivity
import java.util.*

class TrendAdapter(private val mContext: Context, private val trendList: LinkedList<Trend>) : RecyclerView.Adapter<xyz.donot.quetzal.view.adapter.TrendAdapter.ViewHolder>() {
  private val mInflater: LayoutInflater by lazy { LayoutInflater.from(mContext) }
  override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
    // 表示するレイアウトを設定
    return ViewHolder(mInflater.inflate(R.layout.item_trend, viewGroup, false))
  }
  override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
    if (trendList.size > i ) {
      val  item= trendList[i]
      //ビューホルダー
      viewHolder.apply {
        trendTxt.text=item.name
        cardView.setOnClickListener({
          mContext.startActivity(Intent(mContext,SearchActivity::class.java).putExtra("query_txt",item.query))
        })
        }
      }


    }


  override fun getItemCount(): Int {
    return trendList.size
  }
  fun add(trend:Trend)
  {
    Handler(Looper.getMainLooper()).post {  trendList.add(trend)
      this.notifyItemInserted(trendList.size)}
  }



  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cardView: CardView
    val trendTxt: TextView
    init {
      cardView=itemView.findViewById(R.id.cardView)as CardView
      trendTxt=itemView.findViewById(R.id.trend_txt)as TextView
    }
  }
}