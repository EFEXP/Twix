package xyz.donot.quetzal.viewmodel.activity

import android.content.Context
import android.preference.PreferenceManager
import jp.keita.kagurazaka.rxproperty.RxCommand
import jp.keita.kagurazaka.rxproperty.RxProperty
import jp.keita.kagurazaka.rxproperty.toRxCommand
import rx.lang.kotlin.filterNotNull
import twitter4j.Status
import xyz.donot.quetzal.model.StreamType
import xyz.donot.quetzal.model.TwitterStream
import xyz.donot.quetzal.notification.NotificationWrapper
import xyz.donot.quetzal.util.extrautils.mainThread
import xyz.donot.quetzal.util.extrautils.toast
import xyz.donot.quetzal.util.getTwitterInstance
import xyz.donot.quetzal.util.isMentionToMe

class MainViewModel(val context: Context) : ActivityViewModel() {
    override fun clean() {
        statusSend.unsubscribe()
        editStatus.unsubscribe()
        status.unsubscribe()
        connected.unsubscribe()
        stream.clean()
    }

    val twitter by lazy { getTwitterInstance() }

    companion object {
        val stream  by lazy { TwitterStream().run(StreamType.USER_STREAM) }
    }

    val connected: RxProperty<Boolean>
    val status: RxProperty<Status>
    val editStatus: RxProperty<String> = RxProperty("")
    val statusSend: RxCommand<() -> Unit>

    init {
        connected = RxProperty(stream.isConnected)
        status = RxProperty(stream.statusSubject)
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