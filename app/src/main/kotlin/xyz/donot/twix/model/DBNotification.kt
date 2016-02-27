package xyz.donot.twix.model

import io.realm.RealmObject
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open  class DBNotification : RealmObject() {
    open  var isFavorite: Boolean = false
    open var isReTweet: Boolean = false
    open  var source: String? = null
    open  var text: String? = null
    open var tweetID: Long = 0
    open  var createdAt: Date? = null
}
