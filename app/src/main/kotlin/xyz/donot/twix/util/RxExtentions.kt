package xyz.donot.twix.util;

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers



fun <T> Observable<T>.basicNetworkTask()=onBackpressureBuffer().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())

