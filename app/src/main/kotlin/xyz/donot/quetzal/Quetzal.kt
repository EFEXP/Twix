package xyz.donot.quetzal

import android.app.Application
import android.app.UiModeManager
import android.support.v7.app.AppCompatDelegate

import io.realm.Realm
import io.realm.RealmConfiguration
import org.greenrobot.eventbus.EventBus
import xyz.donot.quetzal.util.extrautils.defaultSharedPreferences


@Suppress
class Quetzal : Application() {
    override fun onCreate() {
        super.onCreate()

        Realm.setDefaultConfiguration(RealmConfiguration.Builder(applicationContext).build())
        EventBus.builder().installDefaultEventBus()
      val design=  when(defaultSharedPreferences.getString("night_mode","auto")){
            "black"->{
                AppCompatDelegate.MODE_NIGHT_YES}
            "white"->{AppCompatDelegate.MODE_NIGHT_NO}
            "auto"->{AppCompatDelegate.MODE_NIGHT_AUTO}
          else->{AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM}
        }
      AppCompatDelegate.setDefaultNightMode(design)
      (getSystemService(UI_MODE_SERVICE)as UiModeManager).nightMode = UiModeManager.MODE_NIGHT_AUTO
    }
}
