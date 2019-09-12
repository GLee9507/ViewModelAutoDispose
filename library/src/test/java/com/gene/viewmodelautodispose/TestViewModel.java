package com.gene.viewmodelautodispose;

import androidx.annotation.IntRange;
import androidx.lifecycle.ViewModel;

import junit.framework.Assert;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

class TestViewModel extends ViewModel implements AutoDisposeOwner {
    @Override
    protected void onCleared() {
        super.onCleared();
        clearJobs();
    }

    public void startObservable(@IntRange(from = 1, to = 100) int index) throws Throwable {
        final int[] lastInt = new int[1];
        new Thread(new Runnable() {
            @Override
            public void run() {

                Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
                    for (int i = 0; i < 100; i++) {
                        Thread.sleep(10);
                        if (i == index) {
                            onCleared();
                        }
                        emitter.onNext(i);
                    }
                    emitter.onComplete();
                }).compose(AutoDispose.forObservable(TestViewModel.this))
                        .subscribe(new Observer<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Integer integer) {
                                lastInt[0] = integer;
                                System.out.println("onNext() called attach: integer = [" + integer + "]");
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        }).start();
        Thread.sleep(10000);
        Assert.assertTrue(lastInt[0] < index);
    }
}
