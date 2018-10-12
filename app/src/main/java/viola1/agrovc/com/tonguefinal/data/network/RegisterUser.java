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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import viola1.agrovc.com.tonguefinal.AppExecutors;
import viola1.agrovc.com.tonguefinal.app.localserver.request.ReqBeanSignup;
import viola1.agrovc.com.tonguefinal.app.localserver.request.response.ResBeanSignup;
import viola1.agrovc.com.tonguefinal.constants.AppNums;
import viola1.agrovc.com.tonguefinal.constants.EnumAppMessages;
import viola1.agrovc.com.tonguefinal.data.network.api.APIService;
import viola1.agrovc.com.tonguefinal.data.network.api.APIUrl;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.LocalRetrofitApi;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.RetrofitService;
import viola1.agrovc.com.tonguefinal.helper.Encryption;
import viola1.agrovc.com.tonguefinal.view.Login;
import viola1.agrovc.com.tonguefinal.view.SignUp;

public class RegisterUser {
    private static final String LOG_TAG = RegisterUser.class.getSimpleName();

    private final AppExecutors mExecutors;
    private UserRegistrationIntentService userRegistrationIntentService;

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static RegisterUser sInstance;
    private final Context mContext;

    private static boolean registerResponse = false;

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
    public boolean startRegisterUserService(String email, String password) {

        Intent intentToPost = new Intent(mContext, UserRegistrationIntentService.class);

        Bundle registerBundle = new Bundle();
        registerBundle.putString("email", email);
        registerBundle.putString("password", password);
        intentToPost.putExtras(registerBundle);

        mContext.startService(intentToPost);
        Log.d(LOG_TAG, "Register user service created");

        return userRegistrationIntentService.isRegistrationSuccess();
    }

    public boolean UserRegister(String email, String password) {
        Log.d(LOG_TAG, "Register user started");

        ReqBeanSignup reqBean = new ReqBeanSignup();

        reqBean.setEmail(Encryption.encrypt(email));
        reqBean.setPassword(Encryption.encrypt(password));

        //Defining retrofit api service*/
        //APIService service = retrofit.create(APIService.class);
        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<Result> call = service.createUser(null, null, null, email,password);

        //calling the com.emtech.retrofitexample.api
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                    //ResBeanSignup resBean = response.body();
                    //String response_status = Encryption.decrypt(resBean.getResponse_status());

                    if (!response.body().getError()) {
                        //Log.d(LOG_TAG, EnumAppMessages.REGISTER_SUCCESS_TITLE.getValue());
                        Log.d(LOG_TAG, response.body().getMessage());

                        /*edtEmail.getText().clear();

                        edtPassword.getText().clear();
                        edtPassword.getText().clear();

                        MDToast mdToast = MDToast.makeText(activity, EnumAppMessages.REGISTER_SUCCESS_TITLE.getValue(), Toast.LENGTH_LONG,MDToast.TYPE_SUCCESS);

                        mdToast.show();
                        startActivity(new Intent(SignUp.this,Login.class));
                        finish();
                        generalMethods.showLocationDialog(activity,EnumAppMessages.REGISTER_SUCCESS_TITLE.getValue(),Encryption.decrypt(resBean.getRegistration_status()));*/


                    }/*else{
                        Log.d(LOG_TAG, EnumAppMessages.REGISTER_ERROR_TITLE.getValue());
                        Log.d(LOG_TAG, Encryption.decrypt(resBean.getError()));
                       *//* MDToast mdToast = MDToast.makeText(activity, EnumAppMessages.REGISTER_ERROR_TITLE.getValue(), Toast.LENGTH_LONG,MDToast.TYPE_ERROR);

                        mdToast.show();
                        generalMethods.showLocationDialog(activity,EnumAppMessages.REGISTER_ERROR_TITLE.getValue(),Encryption.decrypt(resBean.getError()));
*//*
                       registerResponse = false;
                    }
                }else if(response.code() == AppNums.STATUS_COD_FILE_NOT_FOUND){
                    Log.d(LOG_TAG, EnumAppMessages.REGISTER_ERROR_TITLE.getValue());
                    Log.d(LOG_TAG, EnumAppMessages.ERROR_RESOURCE_NOT_FOUND.getValue());
                    *//*MDToast mdToast = MDToast.makeText(activity, EnumAppMessages.REGISTER_ERROR_TITLE.getValue(), Toast.LENGTH_LONG,MDToast.TYPE_ERROR);

                    mdToast.show();
                    generalMethods.showLocationDialog(activity,EnumAppMessages.REGISTER_ERROR_TITLE.getValue(),EnumAppMessages.ERROR_RESOURCE_NOT_FOUND.getValue());*//*

                    registerResponse = false;

                }else{
                    Log.d(LOG_TAG, EnumAppMessages.ERROR_INTERNAL_ERROR.getValue());
                    *//*MDToast mdToast = MDToast.makeText(activity, EnumAppMessages.REGISTER_ERROR_TITLE.getValue(), Toast.LENGTH_LONG,MDToast.TYPE_ERROR);

                    mdToast.show();
                    generalMethods.showLocationDialog(activity,EnumAppMessages.REGISTER_ERROR_TITLE.getValue(),EnumAppMessages.ERROR_INTERNAL_ERROR.getValue());*//*

                    registerResponse = false;
                }*/
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                //print out any error we may get
                //probably server connection
                Log.e(LOG_TAG, t.getMessage());
                registerResponse = false;
            }
        });

        return true;
    }
}
