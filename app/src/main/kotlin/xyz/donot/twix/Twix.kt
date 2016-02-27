package xyz.donot.twix

import android.app.Application
import com.twitter.sdk.android.Twitter

import com.twitter.sdk.android.core.TwitterAuthConfig
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.realm.RealmConfiguration
import org.greenrobot.eventbus.EventBus


class Twix : Application() {
    override fun onCreate() {
        super.onCreate()
        val authConfig = TwitterAuthConfig(getString(xyz.donot.twix.R.string.twitter_consumer_key), getString(xyz.donot.twix.R.string.twitter_consumer_secret))
        Fabric.with(this, com.twitter.sdk.android.Twitter(authConfig), Twitter(authConfig))
        Realm.setDefaultConfiguration(RealmConfiguration.Builder(applicationContext).build())
        EventBus.builder().installDefaultEventBus()
    }

}
