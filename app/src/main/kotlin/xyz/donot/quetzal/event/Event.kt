package xyz.donot.quetzal.event

import twitter4j.Status
import twitter4j.StatusDeletionNotice
import twitter4j.User

class OnAccountChanged
data class OnStatusEvent(val status: Status)
data class OnReplyEvent(val status: Status)
data class OnFavoritedEvent(val source: User, val status: Status)
data class OnRetweetEvent(val source: User, val status: Status)
data class OnDeleteEvent(val deletionNotice: StatusDeletionNotice)
data class OnExceptionEvent(val exception: Exception)
data class OnCustomtabEvent(val url: String)
data class OnCardViewTouchEvent(val status: Status)
class OnSaveIt
data class OnHashtagEvent(val tag:String)
