package xyz.donot.quetzal.viewmodel.adapter
import android.content.Context
import android.databinding.DataBindingUtil
import android.view.View
import android.view.ViewGroup
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import twitter4j.User
import xyz.donot.quetzal.R
import xyz.donot.quetzal.databinding.ItemUserBinding

class UsersAdapter(context: Context) :BasicRecyclerAdapter<UsersAdapter.ViewHolder,User>(context) {

  override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<*>? {
    return ViewHolder(mInflater.inflate(R.layout.item_user, parent, false))
  }


  inner class ViewHolder(itemView: View) :BaseViewHolder<User>(itemView) {
    val binding:ItemUserBinding = DataBindingUtil.bind(itemView)
    override fun setData(data: User) {
      super.setData(data)
      val  item= data
      binding.user = data
      //ビューホルダー
      binding.apply {
        screenName.text=item.screenName
        description.text=item.description
        //  userName.text=item.name
        //   Picasso.with(context).load(item.biggerProfileImageURLHttps).transform(RoundCorner()).into(icon)
      }
    }

  }

}

