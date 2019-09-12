package com.gene.sample;

import android.util.Log;

import com.gene.viewmodelautodispose.AutoDisposeViewModel;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends AutoDisposeViewModel {
    {
//        Log.d("gle", "instance initializer() called");
//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                for (int i = 0; i < 100; i++) {
//                    Log.d("gle", "subscribe() called with: emitter = [" + emitter + "]");
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    emitter.onNext(i);
//                }
//                emitter.onComplete();
//            }
//        })
//                .compose(AutoDispose.attach(this))
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribe(new Observer<Integer>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        Log.d("glee", "onNext() called with: integer = [" + integer + "]");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });

        new SingleTask()
                .subscribeOn(Schedulers.io())
                .compose(AutoDispose.attach(this))
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        Log.d("gle", "onSuccess() called with: aBoolean = [" + aBoolean + "]");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }


}