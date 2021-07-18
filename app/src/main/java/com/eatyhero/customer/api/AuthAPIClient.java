package com.eatyhero.customer.api;

import com.eatyhero.customer.api.crypto.DecryptionImpl;
import com.eatyhero.customer.api.crypto.EncryptionImpl;
import com.eatyhero.customer.api.interceptor.DecryptionInterceptor;
import com.eatyhero.customer.api.interceptor.EncryptionInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class AuthAPIClient {

    // Live URL
//    private static final String USER_AUTH_URL = "http://3.134.107.157:8000/usr/auth/v1/";

    // Local URL
    private static final String USER_AUTH_URL = "http://192.168.1.32/spin/usr/v1/";

    private static Retrofit retrofit = null;

    public static Retrofit getAuthUserClient() {
        Timber.plant(new Timber.DebugTree());

        //Encryption Interceptor
        EncryptionInterceptor encryptionInterceptor = new EncryptionInterceptor(new EncryptionImpl());
        //Decryption Interceptor
        DecryptionInterceptor decryptionInterceptor = new DecryptionInterceptor(new DecryptionImpl());

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
//                .addInterceptor(encryptionInterceptor)
//                .addInterceptor(decryptionInterceptor)
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(USER_AUTH_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}