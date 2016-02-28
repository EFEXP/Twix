package xyz.donot.quetzal.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import com.squareup.picasso.Picasso
import xyz.donot.quetzal.R


class TweetPictureGridAdapter(context: Context, resource: Int) : ArrayAdapter<String>(context, resource) {
 val inflater: LayoutInflater by lazy { getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater }



  override fun getView(position: Int, convertView_: View?, parent: ViewGroup): View {
    var convertView = convertView_
    val view: ViewHolder
    val item = getItem(position)
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.gridview_item_picture, RelativeLayout(context))
      view = ViewHolder()
      view.mainContent = convertView.findViewById(R.id.main_content_picture) as ImageView
      convertView.tag = view
    } else {
      view = convertView.tag as ViewHolder
    }

  Picasso.with(context).load(item).into(view.mainContent)
      if(convertView!=null){
        return convertView
      }
    else{throw IllegalStateException()}

  }
  internal inner  class ViewHolder {
    lateinit var mainContent: ImageView
  }
}
