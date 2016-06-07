package xyz.donot.quetzal.view.customview

import android.content.Context
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import xyz.donot.quetzal.util.extrautils.mainThread

abstract class  FixedRecyclerArrayAdapter<T>(context: Context): RecyclerArrayAdapter<T>(context) {

    var holderInsertListener:ItemInsertListener?=null

    override fun add(`object`: T) {
        mainThread {
            mObjects.add(`object`)
            notifyItemInserted(count)

        }

    }

    override fun remove(`object`: T) {
      val int=  mObjects.indexOf(`object`)
        mainThread {
            mObjects.removeAt(int)
            notifyItemRemoved(int)
        }


    }

    override fun remove(position: Int) {
        mainThread {
            mObjects.removeAt(position)
            notifyItemRemoved(position)
        }


    }


    override fun insert(`object`: T, index: Int) {
        mainThread {
            mObjects.add(index, `object`)
            if(holderInsertListener !=null){
                holderInsertListener?.itemInserted(index)

            }
            notifyItemInserted(index)
        }

        holderInsertListener=null
    }

    fun replace(replacedItem: T,replaceItem: T) {
        val int=  mObjects.indexOf(replacedItem)
        mainThread {
            mObjects.removeAt(int)
            mObjects.add(int,replaceItem)
            notifyItemChanged(int)
        }

    }




    fun setItemInsertListener(listener: ItemInsertListener) {
        this.holderInsertListener=listener
    }
}
interface ItemInsertListener{
    fun itemInserted(index: Int)
}