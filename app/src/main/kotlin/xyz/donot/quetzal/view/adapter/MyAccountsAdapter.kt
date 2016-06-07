package xyz.donot.quetzal.view.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.squareup.picasso.Picasso
import io.realm.Realm
import twitter4j.User
import xyz.donot.quetzal.R
import xyz.donot.quetzal.databinding.ItemUserBinding
import xyz.donot.quetzal.model.DBAccount
import xyz.donot.quetzal.util.RoundCorner

class MyAccountsAdapter(context: Context):BasicRecyclerAdapter<MyAccountsAdapter.ViewHolder, User>(context){
  override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<*>? {
    return ViewHolder(mInflater.inflate(R.layout.item_user, parent, false))
  }

  inner class ViewHolder(itemView: View) : BaseViewHolder<User>(itemView) {
    val binding : ItemUserBinding
    override fun setData(data: User) {
      super.setData(data)
      val  item= data
       binding.apply {
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
              Toast.makeText(context,"メインに設定しました、再起動してください",Toast.LENGTH_LONG).show()
            }
          }
        }
      }
    }

    init {
      binding = DataBindingUtil.bind(itemView)
    }
  }
}
