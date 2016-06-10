package xyz.donot.quetzal

import android.app.Application
import android.app.UiModeManager
import android.support.v7.app.AppCompatDelegate
import io.realm.Realm
import io.realm.RealmConfiguration
import xyz.donot.quetzal.model.realm.MyRealmMigration
import xyz.donot.quetzal.util.extrautils.defaultSharedPreferences
import java.io.FileNotFoundException



class Quetzal : Application() {
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
