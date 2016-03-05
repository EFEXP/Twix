package xyz.donot.quetzal.view.adapter
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import twitter4j.User
import xyz.donot.quetzal.R
import xyz.donot.quetzal.databinding.ItemUserBinding
import xyz.donot.quetzal.util.RoundCorner
import xyz.donot.quetzal.view.activity.UserActivity

class UsersAdapter( context: Context, list: MutableList<User>) :BasicRecyclerAdapter<xyz.donot.quetzal.view.adapter.UsersAdapter.ViewHolder,User>(context,list) {
  override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
    // 表示するレイアウトを設定
    return ViewHolder(mInflater.inflate(R.layout.item_user, viewGroup, false))
  }
  override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
    if (list.size > i ) {
     val  item= list[i]
      //ビューホルダー
      viewHolder.binding.apply {
        screenName.text=item.screenName
        description.text=item.description
        userName.text=item.name
        Picasso.with(context).load(item.biggerProfileImageURLHttps).transform(RoundCorner()).into(icon)
        cardView.setOnClickListener{ context.startActivity(Intent(context, UserActivity::class.java).putExtra("user_id",item.id))}
        }
    }
  }
  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding:ItemUserBinding
    init {
    binding=DataBindingUtil.bind(itemView)
    }
  }

}

