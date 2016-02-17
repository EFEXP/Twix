package xyz.donot.twix.view.adapter

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView

import com.bumptech.glide.Glide
import io.realm.Realm

import io.realm.RealmBaseAdapter
import io.realm.RealmResults
import rx.lang.kotlin.observable
import twitter4j.Twitter
import twitter4j.User
import xyz.donot.twix.R
import xyz.donot.twix.model.DBAccount
import xyz.donot.twix.util.basicNetworkTask
import xyz.donot.twix.util.getMyId
import xyz.donot.twix.util.getTwitterInstance


class MyUserAccountAdapter(context: Context, resId: Int,
                           realmResults: RealmResults<DBAccount>,
                           automaticUpdate: Boolean) : RealmBaseAdapter<DBAccount>(context, realmResults, automaticUpdate), ListAdapter {


    override fun getView(position: Int, convertView_: View?, parent: ViewGroup): View {
        var convertView = convertView_
        val viewHolder: ViewHolder
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_accounts,
                    parent, false)
            viewHolder = ViewHolder()
            viewHolder.name = convertView!!.findViewById(R.id.name) as TextView
            viewHolder.screenName = convertView.findViewById(R.id.screen_name) as TextView
            viewHolder.icon = convertView.findViewById(R.id.icon) as AppCompatImageView
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        val item = realmResults[position]
      viewHolder.name?.text = item.name
      Glide.with(context).load(item.profileImageUrl).into(viewHolder.icon)
      viewHolder.screenName?.text = item.screenName

        Log.d("Realm", item.toString())
        return convertView
    }
}

internal class ViewHolder {
    var name: TextView?=null
    var screenName: TextView?=null
    var icon: AppCompatImageView?=null
}