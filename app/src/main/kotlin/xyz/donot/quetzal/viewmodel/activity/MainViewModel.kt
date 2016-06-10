package xyz.donot.quetzal.viewmodel.activity

import android.content.Context
import android.preference.PreferenceManager
import jp.keita.kagurazaka.rxproperty.RxCommand
import jp.keita.kagurazaka.rxproperty.RxProperty
import jp.keita.kagurazaka.rxproperty.toRxCommand
import rx.lang.kotlin.filterNotNull
import twitter4j.Status
import twitter4j.StatusDeletionNotice
import xyz.donot.quetzal.Quetzal
import xyz.donot.quetzal.notification.NotificationWrapper
import xyz.donot.quetzal.util.extrautils.mainThread
import xyz.donot.quetzal.util.extrautils.toast
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
    val statusSend: RxCommand<() -> Unit>

    init {
        connected = RxProperty(stream.isConnected)
        status = RxProperty(stream.statusSubject)
        delete = RxProperty(stream.deleteSubject)
        status.asObservable().filterNotNull()
                .subscribe {
                    if (isMentionToMe(it)) {
                        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("notifications", false)) {
                            mainThread { NotificationWrapper(context).replyNotification(it) }
                        }
                    }
                }
        statusSend = editStatus.asObservable().map { !it.isNullOrBlank() && it.count() <= 140 }
                .toRxCommand { context.toast("Hello!") }
    }


}