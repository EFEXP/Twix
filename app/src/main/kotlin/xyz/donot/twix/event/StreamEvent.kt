package xyz.donot.twix.event

import twitter4j.Status
import twitter4j.StatusDeletionNotice


data class OnStatusEvent(val status: Status)
data class OnDeleteEvent(val deletionNotice: StatusDeletionNotice)
data class OnExeptionEvent(val exception: Exception)
class OnFlickedEvent()
