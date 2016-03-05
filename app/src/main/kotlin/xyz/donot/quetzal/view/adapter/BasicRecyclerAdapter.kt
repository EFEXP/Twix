package xyz.donot.quetzal.view.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater

abstract class BasicRecyclerAdapter
<ViewHolder:RecyclerView.ViewHolder,ListItem>
(internal   val context: Context,internal   val list: MutableList<ListItem>)
: RecyclerView.Adapter<ViewHolder>()
{
 val mInflater: LayoutInflater by lazy { LayoutInflater.from(context) }
  fun addAll(item:List<ListItem>)
  {
    Handler(Looper.getMainLooper()).post()
    {
      item.forEach { list.add(it)
        notifyItemInserted(list.size)
      }
    }
  }

  fun add(item:ListItem)
  {
    Handler(Looper.getMainLooper()).post {list.add(item)
      this.notifyItemInserted(list.size)}
  }
  fun reload(item:ListItem)
  {
    Handler(Looper.getMainLooper()).post {
      list.
        filter{ it== item }
        .mapNotNull { list.indexOf(it) }
        .forEach {
          list[it] = item
          this.notifyItemChanged(it)
        }
    }
  }
  fun insert(item:ListItem)
  {
    Handler(Looper.getMainLooper()).post {list.add(0, item)
      this.notifyItemInserted(0)}
  }
  fun clear(){
    Handler(Looper.getMainLooper()).post {list.clear()
      this.notifyDataSetChanged()}
  }
  fun remove(item:ListItem){
    Handler(Looper.getMainLooper()).post {list.remove(item)
      this.notifyDataSetChanged()}
  }
  override fun getItemCount(): Int {
    return list.size
  }

}
