package viola1.agrovc.com.tonguefinal.dataloaders.retrofit;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import viola1.agrovc.com.tonguefinal.constants.AppProperties;
import viola1.agrovc.com.tonguefinal.data.network.api.APIService;
import viola1.agrovc.com.tonguefinal.data.network.api.APIUrl;


public class LocalRetrofitApi {
    Retrofit retrofit1;

    public LocalRetrofitApi() {
        //Here a logging interceptor is created
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //The logging interceptor will be added to the http client
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging).connectTimeout(80, TimeUnit.SECONDS).readTimeout(80, TimeUnit.SECONDS);

        //The Retrofit builder will have the client attached, in order to get connection logs
        this.retrofit1 = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(APIUrl.BASE_URL)
                .build();
    }

    public Retrofit getRetrofit1() {
        return retrofit1;
    }

    public APIService getRetrofitService(){
        return this.getRetrofit1().create(APIService.class);
    }
}
