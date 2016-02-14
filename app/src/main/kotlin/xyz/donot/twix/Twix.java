package xyz.donot.twix;

import android.app.Application;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class Twix extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.twitter_consumer_key),getString(R.string.twitter_consumer_secret));
    Fabric.with(this, new com.twitter.sdk.android.Twitter(authConfig), new Twitter(authConfig));
    Realm.setDefaultConfiguration(new RealmConfiguration.Builder(this).build());
  }

}
