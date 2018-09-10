package viola1.agrovc.com.tonguefinal.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import viola1.agrovc.com.tonguefinal.AppExecutors;
import viola1.agrovc.com.tonguefinal.data.database.Language;
import viola1.agrovc.com.tonguefinal.data.network.api.APIService;
import viola1.agrovc.com.tonguefinal.data.network.api.APIUrl;
import viola1.agrovc.com.tonguefinal.models.Languages;

public class FetchLanguages {
    private static final String LOG_TAG = FetchLanguages.class.getSimpleName();

    // LiveData storing the latest downloaded weather forecasts
    private final MutableLiveData<Language[]> mDownloadedLanguages;
    private final AppExecutors mExecutors;

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static FetchLanguages sInstance;
    private final Context mContext;

    public FetchLanguages(Context context, AppExecutors executors) {
        mContext = context;
        mExecutors = executors;
        mDownloadedLanguages = new MutableLiveData<Language[]>();
    }

    /**
     * Get the singleton for this class
     */
    public static FetchLanguages getInstance(Context context, AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new FetchLanguages(context.getApplicationContext(), executors);
                Log.d(LOG_TAG, "Made new network data source");
            }
        }
        return sInstance;
    }

    public LiveData<Language[]> getCurrentLanguages() {
        return mDownloadedLanguages;
    }

    /**
     * Starts an intent service to fetch the languages.
     */
    public void startFetchLanguageService() {
        Intent intentToFetch = new Intent(mContext, LanguageSyncIntentService.class);
        mContext.startService(intentToFetch);
        Log.d(LOG_TAG, "Fetch categories service created");
    }

    public void GetAppLanguages() {
        Log.d(LOG_TAG, "Fetch languages started");

            //building retrofit object
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(APIUrl.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            //Defining retrofit api service
            APIService service = retrofit.create(APIService.class);

        //defining the call
        Call<Languages> call = service.getLanguages();

        //calling the api
        call.enqueue(new Callback<Languages>() {
            @Override
            public void onResponse(Call<Languages> call, Response<Languages> response) {

                //if response body is not null, we have some data
                //count what we have in the response
                if (response.body() != null && response.body().getLanguages().length > 0) {
                    Log.d(LOG_TAG, "JSON not null and has " + response.body().getLanguages().length
                            + " values");

                    // When you are off of the main thread and want to update LiveData, use postValue.
                    // It posts the update to the main thread.
                    mDownloadedLanguages.postValue(response.body().getLanguages());

                    // If the code reaches this point, we have successfully performed our sync
                    Log.d(LOG_TAG, "Successfully performed our sync");
                }
            }

            @Override
            public void onFailure(Call<Languages> call, Throwable t) {
                //print out any error we may get
                //probably server connection
                Log.e(LOG_TAG, t.getMessage());
            }
        });

    }

}
