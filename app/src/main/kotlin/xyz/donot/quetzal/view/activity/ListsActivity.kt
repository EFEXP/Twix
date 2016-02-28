package xyz.donot.quetzal.view.activity

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import xyz.donot.quetzal.R
import xyz.donot.quetzal.view.dialog.CreateListDialog
import xyz.donot.quetzal.view.fragment.UsersListFragment

class ListsActivity : AppCompatActivity() {

  val userId by lazy { intent.getLongExtra("user_id",0L) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lists)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationOnClickListener { finish() }
        supportFragmentManager.beginTransaction().add(R.id.container, UsersListFragment(userId)).commitAllowingStateLoss()
        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
              CreateListDialog().apply {
                isCancelable=false
                show(supportFragmentManager,"lit")
              }
        }
    }

}
