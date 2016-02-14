package xyz.donot.twix.model



import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class DBAccount : RealmObject() {
    open  var id: Long = 0
    @PrimaryKey open   var name: String? = null
    open  var isMain: Boolean = false
    open  var key: String? = null
    open  var secret: String? = null
}
