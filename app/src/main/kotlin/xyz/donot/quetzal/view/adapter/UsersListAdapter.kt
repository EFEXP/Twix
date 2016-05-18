package xyz.donot.quetzal.view.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import twitter4j.UserList
import xyz.donot.quetzal.R
import xyz.donot.quetzal.util.RoundCorner
import xyz.donot.quetzal.view.activity.SeeMyListActivity
import java.util.*

class UsersListAdapter(val  context: Context, val list: LinkedList<UserList>)
: BasicRecyclerAdapter<xyz.donot.quetzal.view.adapter.UsersListAdapter.ViewHolder,UserList>(context,list) {

  override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
    // 表示するレイアウトを設定
    return ViewHolder(mInflater.inflate(R.layout.item_users_list, viewGroup, false))
  }
  override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
    if (list.size > i ) {
      val  item= list[i]
      //ビューホルダー
      viewHolder.apply {
        listName.text=item.name
        author.text="${item.user.name}が作成"
        Picasso.with(context).load(item.user.biggerProfileImageURLHttps).transform(RoundCorner()).into(icon)
        cardView.setOnClickListener{
          context.startActivity(Intent(context,SeeMyListActivity::class.java).putExtra("list_id",item.id))
        }
      }


    }
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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
