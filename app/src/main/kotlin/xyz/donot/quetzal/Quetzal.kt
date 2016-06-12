package xyz.donot.quetzal

import android.app.Application
import android.app.UiModeManager
import android.support.v7.app.AppCompatDelegate
import io.realm.Realm
import io.realm.RealmConfiguration
import org.greenrobot.eventbus.EventBus
import xyz.donot.quetzal.model.StreamType
import xyz.donot.quetzal.model.TwitterStream
import xyz.donot.quetzal.model.realm.MyRealmMigration
import xyz.donot.quetzal.util.extrautils.defaultSharedPreferences
import xyz.donot.quetzal.util.extrautils.getUiModeManager
import java.io.FileNotFoundException



class Quetzal : Application() {
    companion object {
        val stream: TwitterStream by lazy { TwitterStream().run(StreamType.USER_STREAM) }
    }
    override fun onCreate() {
        super.onCreate()
           val config= RealmConfiguration.Builder(this)
                    .schemaVersion(2L)
                    .migration(MyRealmMigration())
                    .build()
        try {
            Realm.migrateRealm(config, MyRealmMigration())
        }
        catch(e:FileNotFoundException){}
        Realm.setDefaultConfiguration(config)
        EventBus.builder().installDefaultEventBus()
      val design=  when(defaultSharedPreferences.getString("night_mode","auto")){
            "black"->{
                AppCompatDelegate.MODE_NIGHT_YES}
            "white"->{AppCompatDelegate.MODE_NIGHT_NO}
            "auto"->{AppCompatDelegate.MODE_NIGHT_AUTO}
          else->{AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM}
        }
      AppCompatDelegate.setDefaultNightMode(design)
        getUiModeManager().nightMode = UiModeManager.MODE_NIGHT_AUTO
    }
}
