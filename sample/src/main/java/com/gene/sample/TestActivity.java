package com.gene.sample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TestActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;
    String TAG = "glee";
    Disposable d;

    @SuppressLint("AutoDispose")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
//
//        new SingleTask()
//                .subscribeOn(Schedulers.io())
//                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
//                .subscribe(new SingleObserver<Boolean>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        TestActivity.this.d = d;
//                    }
//
//                    @Override
//                    public void onSuccess(Boolean aBoolean) {
//                        Log.d("gle", "onSuccess() called with: aBoolean = [" + aBoolean + "]");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mainViewModel = null;

//        d.dispose();
//        d = null;
    }
}
