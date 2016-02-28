package xyz.donot.quetzal.model


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open  class DBMuteUser : RealmObject() {
  open  var id: Long = 0L
  @PrimaryKey  open  var myid: Long = 0L
}