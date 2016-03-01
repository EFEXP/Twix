package xyz.donot.quetzal.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.trello.rxlifecycle.components.support.RxDialogFragment
import xyz.donot.quetzal.R
import xyz.donot.quetzal.twitter.TwitterUpdateObservable
import xyz.donot.quetzal.util.bindToLifecycle
import xyz.donot.quetzal.util.getTwitterInstance

class CreateListDialog : RxDialogFragment() {
  val twitter by lazy { activity.getTwitterInstance() }
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    val v = inflater.inflate(R.layout.fragment_create_list, container, false)
    val description=v.findViewById(R.id.description)as EditText
    val name =v.findViewById(R.id.name)as  EditText
    val isPublic= v.findViewById(R.id.isPublic)as CheckBox
    val button =v.findViewById(R.id.create_button)as Button
    val close =v.findViewById(R.id.close_buton)as Button
    button.setOnClickListener{
        if(!name.editableText.isNullOrBlank()&&!name.editableText.isNullOrEmpty()){
          TwitterUpdateObservable(twitter).createList(name.text.toString(),isPublic.isChecked,description.editableText.toString())
            .bindToLifecycle(this)
            .subscribe ({
            Toast.makeText(activity,"作成しました",Toast.LENGTH_LONG).show()
              dismiss()
          },{
              Toast.makeText(activity,"失敗しました",Toast.LENGTH_LONG).show()
              dismiss()
            }

            )
        }
    }
    close.setOnClickListener{
      dismiss()
    }
    return v
  }

}
