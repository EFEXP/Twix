package xyz.donot.quetzal.view.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import io.realm.RealmBaseAdapter
import io.realm.RealmResults
import xyz.donot.quetzal.R
import xyz.donot.quetzal.model.DBAccount


class MyUserAccountAdapter(context: Context, resId: Int,
                           realmResults: RealmResults<DBAccount>,
                           automaticUpdate: Boolean) : RealmBaseAdapter<DBAccount>(context, realmResults, automaticUpdate), ListAdapter {
    override fun getView(position: Int, convertView_: View?, parent: ViewGroup): View {
        var convertView = convertView_
        val viewHolder: ViewHolder
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_accounts,parent, false)
            viewHolder = ViewHolder()
            viewHolder.screenName = convertView.findViewById(R.id.screen_name) as TextView
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        val item = realmResults[position]
      viewHolder.screenName?.text = item.screenName
        Log.d("Realm", item.toString())
        return convertView!!
    }
  inner class  ViewHolder {

    var screenName: TextView?=null

  }
}


