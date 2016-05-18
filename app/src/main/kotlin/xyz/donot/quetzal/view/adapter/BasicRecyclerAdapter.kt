package xyz.donot.quetzal.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import xyz.donot.quetzal.util.extrautils.d
import xyz.donot.quetzal.util.extrautils.mainThread

var mRecycler:RecyclerView?=null

abstract class BasicRecyclerAdapter
<ViewHolder:RecyclerView.ViewHolder,ListItem>
(private    val context: Context,private    val list: MutableList<ListItem>)
: RecyclerView.Adapter<ViewHolder>(),View.OnClickListener
{

    var mListener: BasicRecyclerAdapter.OnItemClickListener<ViewHolder, ListItem>?=null
    val mInflater: LayoutInflater by lazy { LayoutInflater.from(context) }

  fun addAll(item:List<ListItem>)
  {
      mainThread {

      item.forEach { list.add(it)
        notifyItemInserted(list.size)
      }
    }
  }

  fun add(item:ListItem)
  {
      mainThread {list.add(item)
      this.notifyItemInserted(list.size)}
  }
  fun reload(item:ListItem)
  {
      mainThread {
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
      mainThread {
          list.add(0, item)
          this.notifyItemInserted(0)
      }

  }
    fun insertWithPosition(replacedItem:ListItem,replaceItem: ListItem)
    {
        mainThread {
            list.
                    filter{ it==replacedItem }
                    .mapNotNull { list.indexOf(it) }
                    .forEach {
                        list[it] = replaceItem
                        this.notifyItemChanged(it)
                    }
        }
    }
  fun clear(){
      mainThread {list.clear()
      this.notifyDataSetChanged()}
  }
  fun remove(item:ListItem){
      mainThread {list.remove(item)
      this.notifyDataSetChanged()}
  }
  override fun getItemCount(): Int {
    return list.size
  }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        d("OnDetached!")
       mRecycler=null
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        d("onAttached!")
        mRecycler=recyclerView
    }
    fun setOnItemClickListener( listener: OnItemClickListener <ViewHolder,ListItem>) {
    mListener = listener;
}

    override fun onClick(v: View) {
        d("OnClick!")
        if (mRecycler == null) {
            d("mRecycler == null")
            return;
        }

        if (mListener != null) {
            d("mListener != null")
            val position=mRecycler?.getChildAdapterPosition(v)!!
            val item = list[position];
            mListener?.onItemClick(this, position, item);
        }
    }

    interface OnItemClickListener <ViewHolder:RecyclerView.ViewHolder,ListItem>{
    fun  onItemClick(adapter:BasicRecyclerAdapter<ViewHolder,ListItem>,  position:Int, item:ListItem);
}

}
