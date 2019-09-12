package com.gene.viewmodelautodispose;

import androidx.lifecycle.ViewModel;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.CompletableTransformer;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

public class AutoDisposeViewModel extends ViewModel {
    private BehaviorSubject<Boolean> lifecycleSubject = BehaviorSubject.create();

    @Override
    protected void onCleared() {
        super.onCleared();
        clearJobs();
    }


    private void clearJobs() {
        lifecycleSubject.onNext(true);
        lifecycleSubject.onComplete();
        lifecycleSubject = null;
    }

    public static class AutoDispose<T> implements ObservableTransformer<T, T>,
            FlowableTransformer<T, T>,
            MaybeTransformer<T, T>,
            SingleTransformer<T, T>,
            CompletableTransformer {
        private final BehaviorSubject<Boolean> lifecycleSubject;

        private AutoDispose(BehaviorSubject<Boolean> lifecycleSubject) {
            this.lifecycleSubject = lifecycleSubject;
        }

        public static <T> AutoDispose<T> attach(AutoDisposeViewModel autoDisposeViewModel) {
            return new AutoDispose<>(autoDisposeViewModel.lifecycleSubject);
        }

        @Override
        public Publisher<T> apply(Flowable<T> upstream) {
            return upstream.takeUntil(lifecycleSubject.filter(aBoolean -> aBoolean)
                    .toFlowable(BackpressureStrategy.LATEST))
                    .doOnSubscribe(subscription -> {
                        if (Boolean.TRUE.equals(lifecycleSubject.getValue())) {
                            subscription.cancel();
                        }
                    });
        }

        @Override
        public ObservableSource<T> apply(Observable<T> upstream) {
            return upstream.takeUntil(lifecycleSubject.filter(aBoolean -> aBoolean))
                    .doOnSubscribe(disposable -> {
                        if (Boolean.TRUE.equals(lifecycleSubject.getValue()))
                            disposable.dispose();

                    });
        }

        @Override
        public CompletableSource apply(Completable upstream) {
            return upstream.takeUntil(lifecycleSubject.filter(aBoolean -> aBoolean)
                    .ignoreElements())
                    .doOnSubscribe(disposable -> {
                        if (Boolean.TRUE.equals(lifecycleSubject.getValue()))
                            disposable.dispose();
                    });
        }

        @Override
        public MaybeSource<T> apply(Maybe<T> upstream) {
            return upstream.takeUntil(lifecycleSubject.filter(aBoolean -> aBoolean)
                    .singleElement())
                    .doOnSubscribe(disposable -> {
                        if (Boolean.TRUE.equals(lifecycleSubject.getValue()))
                            disposable.dispose();

                    });
        }


        @Override
        public SingleSource<T> apply(Single<T> upstream) {
            return upstream.takeUntil(lifecycleSubject.filter(new Predicate<Boolean>() {
                @Override
                public boolean test(Boolean aBoolean) throws Exception {
                    return aBoolean;
                }
            })
                    .singleOrError())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            if (Boolean.TRUE.equals(lifecycleSubject.getValue()))
                                disposable.dispose();
                        }
                    });
        }
    }
}
