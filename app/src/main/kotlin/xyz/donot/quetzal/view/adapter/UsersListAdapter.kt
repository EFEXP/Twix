package xyz.donot.quetzal.view.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.squareup.picasso.Picasso
import twitter4j.UserList
import xyz.donot.quetzal.R
import xyz.donot.quetzal.util.RoundCorner
import xyz.donot.quetzal.view.activity.SeeMyListActivity

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
        listName.text=item.name
        author.text="${item.user.name}が作成"
        Picasso.with(context).load(item.user.biggerProfileImageURLHttps).transform(RoundCorner()).into(icon)
        cardView.setOnClickListener{
          context.startActivity(Intent(context, SeeMyListActivity::class.java).putExtra("list_id",item.id))
        }
      }
    }

    val icon: ImageView
    val cardView: CardView
    val author: TextView
    val listName: TextView
    init {
      cardView=itemView.findViewById(R.id.cardView)as CardView
      icon =itemView.findViewById(R.id.icon) as ImageView
      author=itemView.findViewById(R.id.author)as TextView
      listName=itemView.findViewById(R.id.list_name)as TextView

    }
  }

}
