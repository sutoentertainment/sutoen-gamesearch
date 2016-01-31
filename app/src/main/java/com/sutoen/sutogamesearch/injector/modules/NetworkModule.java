package com.sutoen.sutogamesearch.injector.modules;

import com.sutoen.sutogamesearch.network.ApiConstants;

import dagger.Module;
import dagger.Provides;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;


/**
 * The module which provide dependencies
 */
@Module
public class NetworkModule {
    @Provides
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(ApiConstants.G2A_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
