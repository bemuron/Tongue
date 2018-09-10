package viola1.agrovc.com.tonguefinal.data.network;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import viola1.agrovc.com.tonguefinal.AppExecutors;
import viola1.agrovc.com.tonguefinal.data.network.api.APIService;
import viola1.agrovc.com.tonguefinal.data.network.api.APIUrl;

public class RegisterUser {
    private static final String LOG_TAG = RegisterUser.class.getSimpleName();

    private final AppExecutors mExecutors;
    private UserRegistrationIntentService userRegistrationIntentService;

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static RegisterUser sInstance;
    private final Context mContext;

    private boolean registerResponse;

    public RegisterUser(Context context, AppExecutors executors) {
        mContext = context;
        mExecutors = executors;
    }

    /**
     * Get the singleton for this class
     */
    public static RegisterUser getInstance(Context context, AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new RegisterUser(context.getApplicationContext(), executors);
                Log.d(LOG_TAG, "Made new network data source");
            }
        }
        return sInstance;
    }

    /**
     * Starts an intent service to register the user
     */
    public void startRegisterUserService(String name, String date_of_birth, String gender, String email, String password) {

        Intent intentToPost = new Intent(mContext, UserRegistrationIntentService.class);

        Bundle registerBundle = new Bundle();
        registerBundle.putString("name", name);
        registerBundle.putString("dob", date_of_birth);
        registerBundle.putString("gender", gender);
        registerBundle.putString("email", email);
        registerBundle.putString("password", password);
        intentToPost.putExtras(registerBundle);

        mContext.startService(intentToPost);
        Log.d(LOG_TAG, "Register user service created");

        userRegistrationIntentService.isRegistrationSuccess();
    }

    public void UserRegister(String name, String date_of_birth, String gender, String email, String password) {
        Log.d(LOG_TAG, "Register user started");

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit com.emtech.retrofitexample.api service
        APIService service = retrofit.create(APIService.class);

        //defining the call
        Call<Result> call = service.createUser(name, date_of_birth, gender, email, password);

        //calling the com.emtech.retrofitexample.api
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                //if response body is not null, we have some data
                //count what we have in the response
                if (!response.body().getError()) {
                    Log.d(LOG_TAG, response.body().getMessage());

                    // If the code reaches this point, we have successfully registered
                    Log.d(LOG_TAG, "Successful registration");

                    //registerResponse = true;
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                //print out any error we may get
                //probably server connection
                Log.e(LOG_TAG, t.getMessage());
            }
        });

        //return registerResponse;
    }
}
