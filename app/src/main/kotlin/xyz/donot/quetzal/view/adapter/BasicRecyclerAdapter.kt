package xyz.donot.quetzal.view.adapter

import android.content.Context
import android.view.LayoutInflater
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter



abstract class BasicRecyclerAdapter
<ViewHolder:BaseViewHolder<ListItem>,ListItem>
(con: Context)
: RecyclerArrayAdapter<ListItem>(con)
{
    val mInflater: LayoutInflater by lazy { LayoutInflater.from(context) }
  fun reload(item:ListItem)
  {

      allData.
        filter{ it== item }
        .mapNotNull { allData.indexOf(it) }
        .forEach {
            remove(it)
            insert(item,it)

        }

  }

    fun insertWithPosition(replacedItem:ListItem,replaceItem: ListItem)
    {

            allData.
                    filter{ it==replacedItem }
                    .mapNotNull {allData.indexOf(it) }
                    .forEach {
                        remove(it)
                        insert(replaceItem,it)
                    }

    }


    }




