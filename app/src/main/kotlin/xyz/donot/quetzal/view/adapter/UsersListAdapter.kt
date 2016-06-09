package xyz.donot.quetzal.view.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.View
import android.view.ViewGroup
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.squareup.picasso.Picasso
import twitter4j.UserList
import xyz.donot.quetzal.R
import xyz.donot.quetzal.databinding.ItemUsersListBinding
import xyz.donot.quetzal.util.RoundCorner

class UsersListAdapter(context: Context)
: BasicRecyclerAdapter<xyz.donot.quetzal.view.adapter.UsersListAdapter.ViewHolder,UserList>(context) {
  override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<*>? {
    return ViewHolder(mInflater.inflate(R.layout.item_users_list, parent, false))
  }




  inner class ViewHolder(itemView: View) :BaseViewHolder<UserList>(itemView) {
    override fun setData(data: UserList) {
      super.setData(data)
      val  item= data
      //ビューホルダー
    this.apply {
      binding.list = item

      Picasso.with(context).load(item.user.biggerProfileImageURLHttps).transform(RoundCorner()).into(binding.icon)
      }
    }

    val binding: ItemUsersListBinding

    init {
      binding = DataBindingUtil.bind(itemView)
    }
  }

}
