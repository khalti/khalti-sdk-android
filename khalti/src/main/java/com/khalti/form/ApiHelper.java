package com.khalti.form;


import com.google.gson.GsonBuilder;
import com.khalti.form.api.KhaltiApi;
import com.utila.ApiUtil;
import com.utila.EmptyUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ApiHelper {
    private static final int TIME_OUT = 30;
    private int HTTP_STATUS_CODE;
    private String HTTP_ERROR;

    public static KhaltiApi apiBuilder() {
//        String url = "http://a.khalti.com/";
        String url = "http://192.168.1.103:8000/";
//        String url = "https://kumarjewelersinc.com/";
        /*Logging*/
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttp3.OkHttpClient okHttpClient = new okhttp3.OkHttpClient.Builder()
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(KhaltiApi.class);
    }

    public <T> Subscription callApi(Observable<Response<T>> observable, ApiCallback callback) {
        try {
            return observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Response<T>>() {
                        @Override
                        public void onCompleted() {
                            if (ApiUtil.isSuccessFul(HTTP_STATUS_CODE)) {
                                callback.onComplete();
                            } else {
                                callback.onError(HTTP_ERROR);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (EmptyUtil.isNotNull(e)) {
                                e.printStackTrace();
                            }
                            callback.onError(EmptyUtil.isNotNull(e) ? e.getMessage() : "");
                        }

                        @Override
                        public void onNext(Response<T> response) {
                            HTTP_STATUS_CODE = response.code();
                            if (response.isSuccessful()) {
                                callback.onNext(response.body());
                            } else {
                                try {
                                    HTTP_ERROR = new String(response.errorBody().bytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError(e.getMessage());
        }

        return null;
    }

    public interface ApiCallback {
        void onComplete();

        void onError(String errorMessage);

        void onNext(Object o);
    }
}
