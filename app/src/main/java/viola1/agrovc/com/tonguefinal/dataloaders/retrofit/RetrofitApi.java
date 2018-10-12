
package viola1.agrovc.com.tonguefinal.dataloaders.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class RetrofitApi {
    Retrofit retrofit1;

    public RetrofitApi() {
        //Here a logging interceptor is created
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //The logging interceptor will be added to the http client
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging).connectTimeout(14000, TimeUnit.SECONDS).readTimeout(14000, TimeUnit.SECONDS);

        //The Retrofit builder will have the client attached, in order to get connection logs
        this.retrofit1 = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())

                .build();
    }

    public Retrofit getRetrofit1() {
        return retrofit1;
    }

    public RetrofitService getRetrofitService(){
        return this.getRetrofit1().create(RetrofitService.class);
    }
}