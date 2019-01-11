package viola1.agrovc.com.tonguefinal.presentation.ui.activities;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.valdesekamdem.library.mdtoast.MDToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.app.Config;
import viola1.agrovc.com.tonguefinal.app.MyApplication;
import viola1.agrovc.com.tonguefinal.app.localserver.request.ReqBeanLogin;
import viola1.agrovc.com.tonguefinal.app.localserver.request.response.ResBeanLogin;
import viola1.agrovc.com.tonguefinal.constants.AppNums;
import viola1.agrovc.com.tonguefinal.constants.AppProperties;
import viola1.agrovc.com.tonguefinal.constants.EnumAppMessages;
import viola1.agrovc.com.tonguefinal.data.network.Result;
import viola1.agrovc.com.tonguefinal.data.network.api.APIService;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.LocalRetrofitApi;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.RetrofitService;
import viola1.agrovc.com.tonguefinal.helper.GeneralMethods;
import viola1.agrovc.com.tonguefinal.helper.InputValidator;
import viola1.agrovc.com.tonguefinal.helper.MyPreferenceManager;
import viola1.agrovc.com.tonguefinal.helper.SessionManager;
import viola1.agrovc.com.tonguefinal.models.User;
import viola1.agrovc.com.tonguefinal.presentation.viewmodels.LoginRegisterActivityViewModel;
import viola1.agrovc.com.tonguefinal.presentation.viewmodels.LoginRegistrationViewModelFactory;
import viola1.agrovc.com.tonguefinal.utilities.InjectorUtils;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LoginRegisterActivityViewModel loginRegisterActivityViewModel;
    public LoginActivity loginActivityInstance;
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private MyPreferenceManager preferenceManager;
    private GeneralMethods generalMethods;
    private User mTongueUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginActivityInstance = this;

        setContentView(R.layout.activity_login);

        preferenceManager = new MyPreferenceManager(getApplicationContext());

        LoginRegistrationViewModelFactory factory = InjectorUtils.provideLoginRegistrationViewModelFactory(this.getApplicationContext());
        loginRegisterActivityViewModel = ViewModelProviders.of
                (this, factory).get(LoginRegisterActivityViewModel.class);

        inputEmail = findViewById(R.id.login_email);
        inputPassword = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.btnLogin);
        btnLinkToRegister = findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                //first check if we have an internet connection
                if (isNetworkAvailable()) {
                    //validate user input details and
                    //attempt to login
                    attemptLogin();
                }else {
                    // show user that they may not be having an internet connection
                    Toast.makeText(getApplicationContext(),
                            "Something is not right, try checking your internet connection.", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });


        /*String token = MyPreferenceManager.getInstance(this).getDeviceToken();
        //if token is not null
        if (token != null) {
            //displaying the token
            Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
        } else {
            //if token is null that means something wrong
            Toast.makeText(this, "Token not generated", Toast.LENGTH_SHORT).show();
        }*/

    }//close onCreate

    /**
     * function to verify login details in mysql db
     * Calls viewmodel method
     * */
    private void checkLogin(final String email, final String password) {

        pDialog.setMessage("Logging in ...");
        showDialog();


        btnLogin.setClickable(false);

        Log.d(TAG, "User login started");

        generalMethods = new GeneralMethods();

        final ReqBeanLogin reqBean = new ReqBeanLogin();
        reqBean.setEmail(email);
        reqBean.setPassword(password);
        reqBean.setType(AppProperties.SERVER_REQUEST_TYPE);
        reqBean.set_token(AppProperties.SERVER_REQUEST_TOKEN);

        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        //Call<ResBeanLogin> call = service.authenticate(reqBean);
        Call<Result> call = service.userLogin(email, password);

        //calling the com.emtech.retrofitexample.api
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                if(response.code() == AppNums.STATUS_COD_SUCCESS) {
                    hideDialog();
                    //if response body is not null, we have some data
                    //successful login
                    if (!response.body().getError()) {
                        //Result resBean = response.body();

                        //Log.d("Response status", response.body().getMessage());

                        Log.d(TAG, "Successful login");

                        //create new user object
                        mTongueUser = new User();
                        mTongueUser.setUser_id(response.body().getUser().getUser_id());
                        mTongueUser.setEmail(response.body().getUser().getEmail());
                        mTongueUser.setCreated_on(response.body().getUser().getCreated_on());
                        mTongueUser.setRole(response.body().getUser().getRole());
                        mTongueUser.setDescription(response.body().getUser().getDescription());
                        mTongueUser.setPhone_number(response.body().getUser().getPhone_number());
                        mTongueUser.setProfile_pic(response.body().getUser().getProfile_pic());
                        mTongueUser.setDate_of_birth(response.body().getUser().getDate_of_birth());
                        mTongueUser.setGender(response.body().getUser().getGender());
                        mTongueUser.setName(response.body().getUser().getName());
                        mTongueUser.setPassword(response.body().getUser().getPassword());
                        Log.d(TAG, mTongueUser.getEmail() + " user email");

                        //insert user to the local db
                        loginRegisterActivityViewModel.insertUser(mTongueUser);

                        //String token = preferenceManager.getDeviceToken();
                        /*String token = MyApplication.getInstance().getPrefManager().getDeviceToken();
                        //if token is not null
                        if (token != null) {
                            Log.d(TAG, "token generated: "+token);
                            //register user device token for fcm
                            updateFcm(mTongueUser.getUser_id(), token);
                        } else {
                            //if token is null that means something wrong
                            Log.e(TAG, "Token not generated");
                        }*/

                        // user successfully logged in
                        // Create login session
                        session.createLoginSession(mTongueUser.getUser_id(), mTongueUser.getName(),
                                mTongueUser.getEmail(), mTongueUser.getRole(), mTongueUser.getProfile_pic());

                        Toast.makeText(LoginActivity.this, "Welcome "+mTongueUser.getName(), Toast.LENGTH_LONG).show();

                        /**
                         * Always check for google play services availability before
                         * proceeding further with FCM
                         * */
                        if (checkPlayServices()) {
                            registerFCM(mTongueUser.getUser_id());
                        }

                        //start home activity
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();


                        /*SharedPreferences sharedPreferences = mContext.getSharedPreferences(AppPrefs.APP_PREFS,MODE_PRIVATE);
                        final SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString(AppPrefs.PREF_EMAIL,email).putString(AppPrefs.PREF_PASSWORD,password);

                        editor.putString(AppPrefs.PREF_USER_TYPE,resBean.getProfile_details().getRole());

                        editor.apply();*/

                    }else{
                        btnLogin.setClickable(true);

                        //  Toast.makeText(Login.this,resBean.getError(),Toast.LENGTH_SHORT).show();
                        MDToast mdToast = MDToast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT, MDToast.TYPE_ERROR);

                        mdToast.show();
                        Log.e(TAG, response.body().getMessage());

                    }
                }
                else if(response.code() == AppNums.STATUS_COD_FILE_NOT_FOUND){
                    btnLogin.setClickable(true);
                    MDToast mdToast = MDToast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT, MDToast.TYPE_ERROR);

                    mdToast.show();

                    Log.e(TAG, EnumAppMessages.ERROR_RESOURCE_NOT_FOUND.getValue());
                    Log.e(TAG, response.body().getMessage());

                }else{
                    btnLogin.setClickable(true);
                    //generalMethods.showLocationDialog(Login.this,EnumAppMessages.LOGIN_ERROR_TITLE.getValue(),EnumAppMessages.ERROR_INTERNAL_ERROR.getValue());
                    Log.e(TAG, EnumAppMessages.ERROR_INTERNAL_ERROR.getValue());

                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                hideDialog();
                //print out any error we may get
                //probably server connection
                Log.e(TAG, t.getMessage());

                MDToast mdToast = MDToast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT, MDToast.TYPE_ERROR);

                mdToast.show();

                btnLogin.setClickable(true);
            }
        });

    }

    //retrofit call to register device token in mysql for fcm
    private void updateFcm(int userId, String fcm_registration_id){

        Log.d(TAG, "User device registration for fcm started");

        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<Result> call = service.updateFcm(userId, fcm_registration_id);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(response.code() == AppNums.STATUS_COD_SUCCESS) {
                    try {
                        //if response body is not null, we have some data
                        //successful login
                        if (!response.body().getError()) {
                            // broadcasting token sent to server
                            //Intent registrationComplete = new Intent(Config.SENT_TOKEN_TO_SERVER);
                            //LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(registrationComplete);

                            // Notify UI that registration has completed, so the progress indicator can be hidden.
                            Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
                            LocalBroadcastManager.getInstance(LoginActivity.this).sendBroadcast(registrationComplete);

                            Log.d(TAG, "Successful device fcm id registration");
                        } else {
                            Log.e(TAG, "Unable to send fcm registration id to our sever.");
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                //print out any error we may get
                //probably server connection
                Log.e(TAG, t.getMessage());

                MDToast mdToast = MDToast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT, MDToast.TYPE_ERROR);

                mdToast.show();
            }
        });

    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        inputEmail.setError(null);
        inputPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        InputValidator inputValidator = new InputValidator();
        // Check for a valid password, if the user entered one.
        if ( !inputValidator.isPasswordValid(password)) {

            MDToast mdToast = MDToast.makeText(this, "This password is too short",Toast.LENGTH_LONG, MDToast.TYPE_ERROR);

            mdToast.show();

            focusView = inputPassword;
            cancel = true;
        }
        //check if password field is empty
        if (TextUtils.isEmpty(password)) {

            MDToast mdToast = MDToast.makeText(this, "Password is required",Toast.LENGTH_LONG, MDToast.TYPE_ERROR);

            mdToast.show();
            // mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = inputPassword;
            cancel = true;
        }

        //check if email field is empty
        if (TextUtils.isEmpty(email)) {
            MDToast mdToast = MDToast.makeText(this, "Email is required",Toast.LENGTH_LONG, MDToast.TYPE_ERROR);

            mdToast.show();
            focusView = inputEmail;
            cancel = true;
            // Check for a valid email address.
        } else if (!inputValidator.isEmailValid(email)) {
            MDToast mdToast = MDToast.makeText(this, "This email address is invalid",Toast.LENGTH_LONG, MDToast.TYPE_ERROR);

            mdToast.show();
            focusView = inputEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            //login the user
            checkLogin(email,password);

            /*pDialog.setMessage("Logging in ...");
            showDialog();*/

        }
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     * @param userId the current user logged in id
     */
    private void sendRegistrationToServer(int userId, String token) {

        updateFcm(userId, token);

    }

    /**
     * Registering with FCM and obtaining the fcm registration id
     */
    private void registerFCM(int userId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try{
            String token = MyApplication.getInstance().getPrefManager().getDeviceToken();
            Log.e(TAG, "FCM Registration Token: " + token);

            sendRegistrationToServer(userId, token);

            sharedPreferences.edit().putBoolean(Config.SENT_TOKEN_TO_SERVER, true).apply();

        }catch (Exception e){
            Log.e(TAG, "Failed to complete token refresh", e);

            sharedPreferences.edit().putBoolean(Config.SENT_TOKEN_TO_SERVER, false).apply();
        }

    }

    //method to check for internet connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    //check for google play services in device
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }
}
