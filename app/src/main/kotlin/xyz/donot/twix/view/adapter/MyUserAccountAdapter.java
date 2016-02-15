package xyz.donot.twix.view.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import xyz.donot.twix.R;
import xyz.donot.twix.model.DBAccount;


public class MyUserAccountAdapter extends RealmBaseAdapter<DBAccount> implements ListAdapter {


  public MyUserAccountAdapter(Context context, int resId,
                              RealmResults<DBAccount> realmResults,
                              boolean automaticUpdate) {
    super(context, realmResults, automaticUpdate);
  }


  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder;
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.item_accounts,
        parent, false);
      viewHolder = new ViewHolder();
      viewHolder.name = (TextView) convertView.findViewById(R.id.name);
      viewHolder.screenName= (TextView) convertView.findViewById(R.id.screen_name);
      viewHolder.icon=(AppCompatImageView)convertView.findViewById(R.id.icon) ;
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    DBAccount item = realmResults.get(position);
    viewHolder.name.setText(item.getName());
    return convertView;
  }

  private static  class ViewHolder{
      TextView name;
      TextView screenName;
      AppCompatImageView icon;
  }

  public RealmResults<DBAccount> getRealmResults() {
    return realmResults;
  }
}
