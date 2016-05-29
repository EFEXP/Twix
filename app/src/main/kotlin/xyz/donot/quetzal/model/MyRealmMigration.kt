package xyz.donot.quetzal.model


import io.realm.DynamicRealm
import io.realm.RealmMigration

class MyRealmMigration : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema = realm.schema
        var oldVersionCode=oldVersion
        if(oldVersionCode==0L){
            schema.create("DBMute")
                    .addField("id", Long::class.java)
                    .addField("myid", Long::class.java);
            oldVersionCode++
        }
    }
}
