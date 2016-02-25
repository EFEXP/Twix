package xyz.donot.twix.model


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open  class DBMuteUser : RealmObject() {

    @PrimaryKey
    open  var screenName: String? = null


}
