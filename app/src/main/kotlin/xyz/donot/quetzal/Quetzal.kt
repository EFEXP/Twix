package xyz.donot.quetzal


import android.app.Application
import android.app.UiModeManager
import android.content.Context
import android.support.v7.app.AppCompatDelegate
import com.crashlytics.android.Crashlytics
import com.twitter.sdk.android.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.realm.RealmConfiguration
import org.greenrobot.eventbus.EventBus


class Quetzal : Application() {
    override fun onCreate() {
        super.onCreate()
        val authConfig = TwitterAuthConfig(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret))
        Fabric.with(this, Twitter(authConfig),Crashlytics())
        Realm.setDefaultConfiguration(RealmConfiguration.Builder(applicationContext).build())
        EventBus.builder().installDefaultEventBus()
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
      (getSystemService(Context.UI_MODE_SERVICE)as UiModeManager).nightMode = UiModeManager.MODE_NIGHT_YES
    }
}
