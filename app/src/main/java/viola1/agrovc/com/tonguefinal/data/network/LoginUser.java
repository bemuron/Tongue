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

public class LoginUser {
    private static final String LOG_TAG = LoginUser.class.getSimpleName();

    private final AppExecutors mExecutors;
    private UserLoginIntentService userLoginIntentService;

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static LoginUser sInstance;
    private final Context mContext;

    private boolean loginResponse;

    public LoginUser(Context context, AppExecutors executors) {
        mContext = context;
        mExecutors = executors;
    }

    /**
     * Get the singleton for this class
     */
    public static LoginUser getInstance(Context context, AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new LoginUser(context.getApplicationContext(), executors);
                Log.d(LOG_TAG, "Made new network data source");
            }
        }
        return sInstance;
    }

    /**
     * Starts an intent service to login the user.
     */
    public void startLoginUserService(String email, String password) {

        Intent intentToPost = new Intent(mContext, UserLoginIntentService.class);

        Bundle loginBundle = new Bundle();
        loginBundle.putString("email", email);
        loginBundle.putString("password", password);
        intentToPost.putExtras(loginBundle);

        mContext.startService(intentToPost);
        Log.d(LOG_TAG, "Login user service created");

        //return userLoginIntentService.isLoginSuccess();
    }

    public boolean UserLogIn(String email, String password) {
        Log.d(LOG_TAG, "User login started");

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit com.emtech.retrofitexample.api service
        APIService service = retrofit.create(APIService.class);

        //defining the call
        Call<Result> call = service.userLogin(email, password);

        //calling the com.emtech.retrofitexample.api
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                //if response body is not null, we have some data
                //count what we have in the response
                if (!response.body().getError()) {
                    //Log.d(LOG_TAG, response.body().getMessage());
                    //response.body().getUser();

                    // If the code reaches this point, we have successfully posted the job
                    Log.d(LOG_TAG, "Successful login");

                    loginResponse = true;
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                //print out any error we may get
                //probably server connection
                Log.e(LOG_TAG, t.getMessage());
            }
        });

        return loginResponse;
    }
}
