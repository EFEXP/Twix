package xyz.donot.quetzal.model.realm

import io.realm.RealmObject
import io.realm.annotations.RealmClass


@RealmClass
open  class DBMute : RealmObject() {
    open  var id: Long = 0L
    open  var myid: Long = 0L
}