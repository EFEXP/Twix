package xyz.donot.quetzal.view.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import twitter4j.UserList
import xyz.donot.quetzal.R
import java.util.*

class UsersListAdapter(private val mContext: Context, private val userList: LinkedList<UserList>) : RecyclerView.Adapter<xyz.donot.quetzal.view.adapter.UsersListAdapter.ViewHolder>() {
  private val mInflater: LayoutInflater by lazy { LayoutInflater.from(mContext) }
  override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
    // 表示するレイアウトを設定
    return ViewHolder(mInflater.inflate(R.layout.item_users_list, viewGroup, false))
  }
  override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
    if (userList.size > i ) {
      val  item=userList[i]
      //ビューホルダー
      viewHolder.apply {
        listName.text=item.name
        author.text="${item.user.name}が作成"
        Picasso.with(mContext).load(item.user.biggerProfileImageURLHttps).into(icon)
        cardView.setOnClickListener{

        }
      }


    }
  }

  override fun getItemCount(): Int {
    return userList.size
  }
  fun add(user:UserList)
  {
    Handler(Looper.getMainLooper()).post {  userList.add(user)
      this.notifyItemInserted(userList.size)}
  }

  fun insert(user: UserList)
  {
    Handler(Looper.getMainLooper()).post { userList.addFirst(user)
      this.notifyItemInserted(0)}
  }

  fun clear(){
    Handler(Looper.getMainLooper()).post {  userList.clear()
      this.notifyDataSetChanged()}
  }
  fun remove(user: UserList){
    Handler(Looper.getMainLooper()).post {   userList.remove(user)
      this.notifyDataSetChanged()}
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
