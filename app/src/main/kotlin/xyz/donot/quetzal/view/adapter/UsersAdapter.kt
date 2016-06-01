package xyz.donot.quetzal.view.adapter
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.view.View
import android.view.ViewGroup
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.squareup.picasso.Picasso
import twitter4j.User
import xyz.donot.quetzal.R
import xyz.donot.quetzal.databinding.ItemUserBinding
import xyz.donot.quetzal.util.RoundCorner
import xyz.donot.quetzal.view.activity.UserActivity

class UsersAdapter(context: Context) :BasicRecyclerAdapter<UsersAdapter.ViewHolder,User>(context) {

  override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<*>? {
    return ViewHolder(mInflater.inflate(R.layout.item_user, parent, false))
  }


  inner class ViewHolder(itemView: View) :BaseViewHolder<User>(itemView) {
    val binding:ItemUserBinding
    override fun setData(data: User) {
      super.setData(data)
      val  item= data
      //ビューホルダー
      binding.apply {
        screenName.text=item.screenName
        description.text=item.description
        userName.text=item.name
        Picasso.with(context).load(item.biggerProfileImageURLHttps).transform(RoundCorner()).into(icon)
        cardView.setOnClickListener{ context.startActivity(Intent(context, UserActivity::class.java).putExtra("user_id",item.id))}
      }
    }

    init {
    binding=DataBindingUtil.bind(itemView)
    }
  }

}

