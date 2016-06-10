package xyz.donot.quetzal.viewmodel.adapter

import android.content.Context
import android.view.LayoutInflater
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import xyz.donot.quetzal.view.customview.FixedRecyclerArrayAdapter


abstract class BasicRecyclerAdapter
<ViewHolder:BaseViewHolder<ListItem>,ListItem>
(con: Context)
:  FixedRecyclerArrayAdapter<ListItem>(con)
{
    val mInflater: LayoutInflater by lazy { LayoutInflater.from(context) }
    fun insertWithPosition(replacedItem:ListItem,replaceItem: ListItem)
    {
        replace(replacedItem,replaceItem)
    }

    }




