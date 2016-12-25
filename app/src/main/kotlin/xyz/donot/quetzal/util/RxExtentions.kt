package xyz.donot.quetzal.util

import android.view.View
import com.trello.rxlifecycle.android.ActivityEvent
import com.trello.rxlifecycle.android.FragmentEvent
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import com.trello.rxlifecycle.components.support.RxAppCompatDialogFragment
import com.trello.rxlifecycle.components.support.RxFragment
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers



fun <T> Observable<T>.basicNetworkTask()=onBackpressureBuffer().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.bindToLifecycle(activity: RxAppCompatActivity): Observable<T> = this.compose<T>(activity.bindToLifecycle<T>())

fun <T> Observable<T>.bindUntilEvent(activity: RxAppCompatActivity, event: ActivityEvent): Observable<T> = this.compose<T>(activity.bindUntilEvent(event))

fun <T> Observable<T>.bindToLifecycle(fragment: RxAppCompatDialogFragment): Observable<T> = this.compose<T>(fragment.bindToLifecycle<T>())


fun <T> Observable<T>.bindToLifecycle(fragment: RxFragment): Observable<T> = this.compose<T>(fragment.bindToLifecycle<T>())

fun <T> Observable<T>.bindUntilEvent(fragment: RxAppCompatDialogFragment, event: FragmentEvent): Observable<T> = this.compose<T>(fragment.bindUntilEvent(event))

fun <T> Observable<T>.bindToLifecycle(view: View): Observable<T> = this.bindToLifecycle(view)

