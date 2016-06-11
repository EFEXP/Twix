package xyz.donot.quetzal.viewmodel.activity

import android.content.Context
import android.preference.PreferenceManager
import android.view.View
import jp.keita.kagurazaka.rxproperty.RxCommand
import jp.keita.kagurazaka.rxproperty.RxProperty
import jp.keita.kagurazaka.rxproperty.toRxCommand
import org.greenrobot.eventbus.EventBus
import rx.lang.kotlin.filterNotNull
import twitter4j.Status
import twitter4j.StatusDeletionNotice
import xyz.donot.quetzal.Quetzal
import xyz.donot.quetzal.event.StatusSubscriber
import xyz.donot.quetzal.notification.NotificationWrapper
import xyz.donot.quetzal.twitter.TwitterUpdateObservable
import xyz.donot.quetzal.util.extrautils.mainThread
import xyz.donot.quetzal.util.isMentionToMe

class MainViewModel(context: Context) : ActivityViewModel(context) {
    override fun clean() {
        statusSend.unsubscribe()
        editStatus.unsubscribe()
        status.unsubscribe()
        connected.unsubscribe()
        stream.clean()
    }

    companion object {
        val stream  by lazy { Quetzal.stream }
    }
    val connected: RxProperty<Boolean>
    val status: RxProperty<Status>
    val delete: RxProperty<StatusDeletionNotice>
    val editStatus: RxProperty<String> = RxProperty("")
    val statusSend: RxCommand<View.OnClickListener>
    init {
        connected = RxProperty(stream.isConnected)
        status = RxProperty(stream.statusSubject)
        delete = RxProperty(stream.deleteSubject)
        status.asObservable().filterNotNull()
                .subscribe {
                    if (isMentionToMe(it)) {
                        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("notifications", false)) {
                            mainThread { NotificationWrapper(context).replyReceived(status.value) }
                        }
                    }
                }
        statusSend = editStatus.asObservable().map { !it.isNullOrBlank() && it.count() <= 140 }
                .toRxCommand (View.OnClickListener {
                    EventBus.getDefault().post(hideKey())
                    TwitterUpdateObservable(context, twitter)
                            .updateStatusAsync(editStatus.value.toString())
                            .subscribe(StatusSubscriber(context, { EventBus.getDefault().post(sentStatus(it)) }))
                })
    }

    fun deleteStatus(long: Long) {
        TwitterUpdateObservable(context, twitter).deleteStatusAsync(long)
                .subscribe {
                    EventBus.getDefault().post(deletedStatus())
                }
    }
}

class deletedStatus()
class hideKey()
data class sentStatus(val status: Status)
