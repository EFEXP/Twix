package xyz.donot.quetzal.util;

import android.view.View
import com.trello.rxlifecycle.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers



fun <T> Observable<T>.basicNetworkTask()=onBackpressureBuffer().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.bindToLifecycle(activity: ActivityLifecycleProvider): Observable<T> = this.compose<T>(activity.bindToLifecycle<T>())

fun <T> Observable<T>.bindUntilEvent(activity: ActivityLifecycleProvider, event: ActivityEvent): Observable<T> = this.compose<T>(activity.bindUntilEvent(event))

fun <T> Observable<T>.bindToLifecycle(fragment: FragmentLifecycleProvider): Observable<T> = this.compose<T>(fragment.bindToLifecycle<T>())

fun <T> Observable<T>.bindUntilEvent(fragment: FragmentLifecycleProvider, event: FragmentEvent): Observable<T> = this.compose<T>(fragment.bindUntilEvent(event))

fun <T> Observable<T>.bindToLifecycle(view: View): Observable<T> = this.compose<T>(RxLifecycle.bindView(view))
