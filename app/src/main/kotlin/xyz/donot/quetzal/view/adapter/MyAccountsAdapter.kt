package xyz.donot.quetzal.view.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.View
import android.view.ViewGroup
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.squareup.picasso.Picasso
import twitter4j.User
import xyz.donot.quetzal.R
import xyz.donot.quetzal.databinding.ItemUserBinding
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
        Picasso.with(context).load(item.biggerProfileImageURLHttps).placeholder(R.drawable.avater_place_holder).transform(RoundCorner()).into(icon)
      }
    }

    init {
      binding = DataBindingUtil.bind(itemView)
    }
  }
}
