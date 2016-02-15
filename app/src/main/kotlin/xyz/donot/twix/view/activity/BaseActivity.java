package xyz.donot.twix.view.activity;

import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;

public class BaseActivity extends AppCompatActivity {

  EventBus eventBus=EventBus.getDefault();

  @Override
  protected void onResume() {
    super.onResume();
    eventBus.register(this);
  }

  @Override
  protected void onPause() {
    eventBus.unregister(this);
    super.onPause();
  }

}
