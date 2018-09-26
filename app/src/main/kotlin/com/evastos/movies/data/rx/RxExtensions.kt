package com.evastos.movies.data.rx

import com.evastos.movies.data.exception.ExceptionMapper
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun <T> Single<T>.applySchedulers(): Single<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.applySchedulers(): Flowable<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T, E : Throwable> Single<T>.mapException(exceptionMapper: ExceptionMapper<E>): Single<T> {
    return retryWhen {
        return@retryWhen it.flatMap { throwable ->
            Flowable.error<Throwable> {
                exceptionMapper.map(throwable)
            }
        }
    }
}

fun <T> Single<T>.delayErrorMillis(delay: Long): Single<T> =
        this.retryWhen {
            it.delay(delay, TimeUnit.MILLISECONDS, Schedulers.computation())
                    .flatMapSingle { error -> Single.error<Unit>(error) }
        }
