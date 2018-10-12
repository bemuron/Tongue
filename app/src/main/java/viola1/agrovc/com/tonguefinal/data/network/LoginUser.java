package viola1.agrovc.com.tonguefinal.data.network;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.valdesekamdem.library.mdtoast.MDToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import viola1.agrovc.com.tonguefinal.AppExecutors;
import viola1.agrovc.com.tonguefinal.app.localserver.request.ReqBeanLogin;
import viola1.agrovc.com.tonguefinal.app.localserver.request.response.ResBeanLogin;
import viola1.agrovc.com.tonguefinal.constants.AppNums;
import viola1.agrovc.com.tonguefinal.constants.AppProperties;
import viola1.agrovc.com.tonguefinal.constants.EnumAppMessages;
import viola1.agrovc.com.tonguefinal.data.TongueRepository;
import viola1.agrovc.com.tonguefinal.data.network.api.APIService;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.LocalRetrofitApi;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.RetrofitService;
import viola1.agrovc.com.tonguefinal.helper.GeneralMethods;
import viola1.agrovc.com.tonguefinal.helper.SessionManager;
import viola1.agrovc.com.tonguefinal.models.User;
import viola1.agrovc.com.tonguefinal.presentation.ui.activities.HomeActivity;

public class LoginUser {
    private static final String LOG_TAG = LoginUser.class.getSimpleName();

    private final AppExecutors mExecutors;
    private UserLoginIntentService userLoginIntentService;
    private GeneralMethods generalMethods;
    private TongueRepository mRepository;
    private SessionManager session;
    // LiveData storing the latest user details
    private User mTongueUser;

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static LoginUser sInstance;
    private final Context mContext;

    private boolean loginResponse;

    public LoginUser(Context context, AppExecutors executors) {
        mContext = context;
        mExecutors = executors;

        mTongueUser = new User();

        // Session manager
        session = new SessionManager(mContext.getApplicationContext());
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

    public User getCurrentUser() {
        return mTongueUser;
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

    public void UserLogIn(String email, String password) {
        Log.d(LOG_TAG, "User login started");

        generalMethods = new GeneralMethods();

        final ReqBeanLogin reqBean = new ReqBeanLogin();
        reqBean.setEmail(email);
        reqBean.setPassword(password);
        reqBean.setType(AppProperties.SERVER_REQUEST_TYPE);
        reqBean.set_token(AppProperties.SERVER_REQUEST_TOKEN);
        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<Result> call = service.userLogin(email, password);

        //calling the com.emtech.retrofitexample.api
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                //if response body is not null, we have some data
                //successful login
                if (response.code() == AppNums.STATUS_COD_SUCCESS) {


                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                //print out any error we may get
                //probably server connection
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }

    // repository must implement this interface
    public interface OnCurrentListener {
        public void jobPostDataCallback(String jobTitle, String jobDesc, int categoryId, String categoryName );
    }
}
