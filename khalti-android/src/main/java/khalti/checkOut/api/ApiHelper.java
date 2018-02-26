package khalti.checkOut.api;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import khalti.BuildConfig;
import khalti.utils.ApiUtil;
import khalti.utils.AppUtil;
import khalti.utils.Constant;
import khalti.utils.EmptyUtil;
import khalti.utils.ErrorUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class ApiHelper {
    private static final int TIME_OUT = 30;
    private int HTTP_STATUS_CODE;
    private String HTTP_ERROR;

    public static KhaltiApi apiBuilder() {
        /*Logging*/
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttp3.OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("checkout-version", BuildConfig.VERSION_NAME)
                            .addHeader("checkout-source", "android")
                            .addHeader("checkout-android-version", AppUtil.getOsVersion())
                            .addHeader("checkout-android-api-level", AppUtil.getApiLevel() + "")
                            .build();
                    return chain.proceed(request);
                })
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(KhaltiApi.class);
    }

    public <T> Observable<Object> callApi(Observable<Response<T>> observable) {
        PublishSubject<Object> ps = PublishSubject.create();
        observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<T>>() {
                    @Override
                    public void onCompleted() {
                        if (ApiUtil.isSuccessFul(HTTP_STATUS_CODE)) {
                            ps.onCompleted();
                        } else {
                            ps.onError(new Throwable(ErrorUtil.parseError(HTTP_ERROR)));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (EmptyUtil.isNotNull(e)) {
                            e.printStackTrace();
                        }
                        ps.onError(EmptyUtil.isNotNull(e) ? e : new Throwable(ErrorUtil.parseError("")));
                    }

                    @Override
                    public void onNext(Response<T> response) {
                        HTTP_STATUS_CODE = response.code();
                        if (response.isSuccessful()) {
                            ps.onNext(response.body());
                        } else {
                            try {
                                HTTP_ERROR = new String(response.errorBody().bytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        return ps;
    }

    public interface ApiCallback {
        void onComplete();

        void onError(String errorMessage);

        void onNext(Object o);
    }
}
