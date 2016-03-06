package xyz.donot.quetzal.view.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.squareup.picasso.Picasso
import io.realm.Realm
import org.greenrobot.eventbus.EventBus
import twitter4j.User
import xyz.donot.quetzal.R
import xyz.donot.quetzal.databinding.ItemUserBinding
import xyz.donot.quetzal.event.OnAccountChanged
import xyz.donot.quetzal.model.DBAccount
import xyz.donot.quetzal.util.RoundCorner

class MyAccountsAdapter(context: Context,list: MutableList<User>):BasicRecyclerAdapter<MyAccountsAdapter.ViewHolder, User>(context,list){
  override fun onBindViewHolder( viewHolder: ViewHolder, i: Int) {
    if (list.size > i ) {
      val  item= list[i]
      viewHolder. binding.apply {
        screenName.text=item.screenName
        description.text=item.description
        userName.text=item.name
        Picasso.with(context).load(item.biggerProfileImageURLHttps).transform(RoundCorner()).into(icon)
        cardView.setOnClickListener{
          Realm.getDefaultInstance().use {
            it.executeTransaction {
              it.where(DBAccount::class.java).equalTo("isMain", true).findFirst().isMain = false
              it.where(DBAccount::class.java).equalTo("id", item.id).findFirst().apply {
                isMain = true
              }
              EventBus.getDefault().post(OnAccountChanged())
             Toast.makeText(context,"メインに設定しました、再起動してください",Toast.LENGTH_LONG).show()
            }
          }
        }
      }
    }
  }

  override fun onCreateViewHolder(viewGroup: ViewGroup?, p1: Int): ViewHolder? {
    return ViewHolder(mInflater.inflate(R.layout.item_user, viewGroup, false))
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding : ItemUserBinding
    init {
      binding = DataBindingUtil.bind(itemView)
    }
  }
}
