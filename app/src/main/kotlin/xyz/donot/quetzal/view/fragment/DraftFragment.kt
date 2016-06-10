package xyz.donot.quetzal.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import com.trello.rxlifecycle.components.support.RxDialogFragment
import io.realm.Realm
import xyz.donot.quetzal.R
import xyz.donot.quetzal.model.realm.DBDraft
import xyz.donot.quetzal.util.getMyId
import xyz.donot.quetzal.view.activity.EditTweetActivity
import xyz.donot.quetzal.viewmodel.adapter.DraftAdapter


class DraftFragment : RxDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_draft, container, false)
       val realm= Realm.getDefaultInstance()
               .where(DBDraft::class.java)
               .equalTo("accountId",getMyId())
        .findAll()
        val mAdapter=DraftAdapter(context = context,realmResults =realm)
        val list=view.findViewById(R.id.draft_list_view) as ListView
        list.adapter=mAdapter
        list.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val parent_list=parent as ListView
            val item=parent_list.getItemAtPosition(position)as DBDraft
            if(activity is EditTweetActivity){
                (activity as EditTweetActivity) .changeToDraft(item)
                this@DraftFragment.dismiss()
            }
            Realm.getDefaultInstance().executeTransaction { item.deleteFromRealm() }
        }
        return view
    }

}
