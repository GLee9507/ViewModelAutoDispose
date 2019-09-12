package com.gene.viewmodelautodispose;

import android.util.Log;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.io.Closeable;

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
import io.reactivex.functions.Action;

public class AutoDispose {


    public static <T> ObservableTransformer<T, T> forObservable(final AutoDisposeOwner viewModel) {
        return new ObservableTransformer<T, T>() {
            private Job job;

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.doOnSubscribe(disposable -> {
                    job = new Job(disposable);
                    viewModel.addJob(job);
                }).doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d("glee", "run() doFinally");
                    }
                });
            }
        };
    }

    public static <T> FlowableTransformer<T, T> forFlowable(final AutoDisposeOwner viewModel) {
        return new FlowableTransformer<T, T>() {
            private Job job;

            @Override
            public Publisher<T> apply(Flowable<T> upstream) {

                return upstream.doOnSubscribe(subscription -> {
                    subscription.cancel();
                    job = new Job(subscription);
                    viewModel.addJob(job);
                }).doFinally(() -> viewModel.removeJob(job));
            }
        };
    }

    public static <T> SingleTransformer<T, T> forSingle(final AutoDisposeOwner viewModel) {
        return new SingleTransformer<T, T>() {
            private Job job;

            @Override
            public SingleSource<T> apply(Single<T> upstream) {
                return upstream.doOnSubscribe(disposable -> {
                    job = new Job(disposable);
                    viewModel.addJob(job);
                }).doFinally(() -> viewModel.removeJob(job));
            }
        };
    }

    public static <T> MaybeTransformer<T, T> forMaybe(final AutoDisposeOwner viewModel) {
        return new MaybeTransformer<T, T>() {
            private Job job;

            @Override
            public MaybeSource<T> apply(Maybe<T> upstream) {
                return upstream.doOnSubscribe(disposable -> {
                    job = new Job(disposable);
                    viewModel.addJob(job);
                }).doFinally(() -> viewModel.removeJob(job));
            }
        };
    }

    public static CompletableTransformer forCompletable(final AutoDisposeOwner viewModel) {
        return new CompletableTransformer() {
            private Job job;

            @Override
            public CompletableSource apply(Completable upstream) {
                return upstream.doOnSubscribe(disposable -> {
                    job = new Job(disposable);
                    viewModel.addJob(job);
                }).doFinally(() -> viewModel.removeJob(job));
            }
        };
    }


    static class Job implements Closeable {
        private Subscription subscription;
        private Disposable disposable;

        private Job(Subscription subscription) {
            this.subscription = subscription;
        }

        private Job(Disposable disposable) {
            this.disposable = disposable;
        }

        @Override
        public void close() {
            if (subscription != null) {
                subscription.cancel();
            }

            if (disposable != null) {
                disposable.dispose();
            }
        }
    }
}
