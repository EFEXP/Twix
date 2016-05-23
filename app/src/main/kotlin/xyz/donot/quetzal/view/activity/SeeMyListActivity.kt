package xyz.donot.quetzal.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import xyz.donot.quetzal.R
import xyz.donot.quetzal.view.fragment.SeeMyListFragment

class SeeMyListActivity : AppCompatActivity() {

   val  listId by lazy { intent.extras.getLong("list_id") }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_my_list)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
      toolbar.setNavigationOnClickListener { finish() }
      supportFragmentManager.beginTransaction().add(R.id.container, SeeMyListFragment()
      .apply {
          arguments= Bundle().apply { putLong("listId",listId) }
      }
      ).commitAllowingStateLoss()

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show();
      }
    });*/
    }

}
