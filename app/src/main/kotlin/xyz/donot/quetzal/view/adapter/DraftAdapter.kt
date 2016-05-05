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
import xyz.donot.quetzal.model.DBDraft

class DraftAdapter(context: Context,
                   realmResults: RealmResults<DBDraft>,
                   automaticUpdate: Boolean) : RealmBaseAdapter<DBDraft>(context, realmResults, automaticUpdate), ListAdapter {
    override fun getView(position: Int, convertView_: View?, parent: ViewGroup): View {
        var convertView = convertView_
        val viewHolder: ViewHolder
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_draft,parent, false)
            viewHolder = ViewHolder()
            viewHolder.draft_text= convertView.findViewById(R.id.draft_txt) as TextView
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        val item = realmResults[position]
        viewHolder.draft_text?.text = item.text
        Log.d("Realm", item.toString())
        return convertView!!
    }
    inner class  ViewHolder {
        var draft_text: TextView?=null
    }
}
