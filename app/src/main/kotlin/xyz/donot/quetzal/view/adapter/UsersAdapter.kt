package xyz.donot.quetzal.view.adapter
import android.content.Context
import android.content.Intent
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
import twitter4j.User
import xyz.donot.quetzal.R
import xyz.donot.quetzal.util.RoundCorner
import xyz.donot.quetzal.view.activity.UserActivity
import java.util.*

class UsersAdapter(private val mContext: Context, private val userList: LinkedList<User>) : RecyclerView.Adapter<xyz.donot.quetzal.view.adapter.UsersAdapter.ViewHolder>() {
  private val mInflater: LayoutInflater by lazy { LayoutInflater.from(mContext) }
  override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
    // 表示するレイアウトを設定
    return ViewHolder(mInflater.inflate(R.layout.item_user, viewGroup, false))
  }
  override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
    if (userList.size > i ) {
     val  item=userList[i]
      //ビューホルダー
      viewHolder.apply {
        screenName.text=item.screenName
        description.text=item.description
        userName.text=item.name
        Picasso.with(mContext).load(item.biggerProfileImageURLHttps).transform(RoundCorner()).into(icon)
        cardView.setOnClickListener{mContext.startActivity(Intent(mContext, UserActivity::class.java).putExtra("user_id",item.id))}
        }


    }
  }

  override fun getItemCount(): Int {
    return userList.size
  }
  fun add(user: User)
  {
    Handler(Looper.getMainLooper()).post {  userList.add(user)
      this.notifyItemInserted(userList.size)}
  }
  fun reload(status : User)
  {
    Handler(Looper.getMainLooper()).post {
      userList.
        filter{ it.id==status.id }
        .mapNotNull {  userList.indexOf(it) }
        .forEach {
          userList[it] = status
          this.notifyItemChanged(it)
        }
    }
  }
  fun insert(user: User)
  {
    Handler(Looper.getMainLooper()).post { userList.addFirst(user)
      this.notifyItemInserted(0)}
  }

  fun clear(){
    Handler(Looper.getMainLooper()).post {  userList.clear()
      this.notifyDataSetChanged()}
  }
  fun remove(user: User){
    Handler(Looper.getMainLooper()).post {   userList.remove(user)
      this.notifyDataSetChanged()}
  }


  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val icon: ImageView
    val cardView: CardView
    val description:TextView
    val userName:TextView
    val screenName:TextView
    init {
      cardView=itemView.findViewById(R.id.cardView)as CardView
      icon =itemView.findViewById(R.id.icon) as ImageView
      description=itemView.findViewById(R.id.description)as TextView
      userName=itemView.findViewById(R.id.user_name)as TextView
      screenName=itemView.findViewById(R.id.screen_name)as TextView
    }
  }

}

