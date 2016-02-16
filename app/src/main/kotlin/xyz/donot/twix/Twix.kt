package xyz.donot.twix

import android.app.Application
import com.twitter.sdk.android.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig

import org.greenrobot.eventbus.EventBus

import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.realm.RealmConfiguration
import xyz.donot.twix.util.haveToken


class Twix : Application() {
    override fun onCreate() {
        super.onCreate()
        val authConfig = TwitterAuthConfig(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret))
        Fabric.with(this, com.twitter.sdk.android.Twitter(authConfig), Twitter(authConfig))
        Realm.setDefaultConfiguration(RealmConfiguration.Builder(this).build())
        EventBus.builder().installDefaultEventBus()
    }

}
