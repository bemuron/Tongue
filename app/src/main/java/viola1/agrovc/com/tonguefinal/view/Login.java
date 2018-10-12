package viola1.agrovc.com.tonguefinal.view;




import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.BootstrapProgressBar;




import com.valdesekamdem.library.mdtoast.MDToast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.Tabs.TabFragment;
import viola1.agrovc.com.tonguefinal.app.localserver.UserBioData;
import viola1.agrovc.com.tonguefinal.app.localserver.UserLocation;
import viola1.agrovc.com.tonguefinal.app.localserver.UserPaymentAccount;
import viola1.agrovc.com.tonguefinal.app.localserver.request.ReqBeanLogin;
import viola1.agrovc.com.tonguefinal.app.localserver.request.response.ResBeanLogin;
import viola1.agrovc.com.tonguefinal.app.localserver.request.response.ResBeanUserProfile;
import viola1.agrovc.com.tonguefinal.constants.AppNums;
import viola1.agrovc.com.tonguefinal.constants.AppPrefs;
import viola1.agrovc.com.tonguefinal.constants.AppProperties;
import viola1.agrovc.com.tonguefinal.constants.EnumAppMessages;
import viola1.agrovc.com.tonguefinal.database.DBHandler;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.LocalRetrofitApi;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.RetrofitService;
import viola1.agrovc.com.tonguefinal.helper.GeneralMethods;
import viola1.agrovc.com.tonguefinal.helper.InputValidator;
import viola1.agrovc.com.tonguefinal.presentation.ui.activities.HomeActivity;

//import com.example.viola1.afinal.activities.UsersListActivity;

/**
 * A login screen that offers login via email/password.
 */
public class Login extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    RelativeLayout mainContainer;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    // UI references.
    private BootstrapEditText mEmailView;


    private BootstrapEditText mPasswordView;
    //private EditText mEmailView;
    // private EditText mPasswordView;
    //private ProgressDialog progressDialog;
    private GeneralMethods generalMethods;
    private BootstrapProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        progressBar = (BootstrapProgressBar) findViewById(R.id.progressBar1);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        TabFragment tabs = new TabFragment();

        ft.commit();

        initialise();

        populateAutoComplete();

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        final BootstrapButton mEmailSignInButton = (BootstrapButton) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }
    @Override
    public void onBackPressed() {
        /** DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         if (drawer.isDrawerOpen(GravityCompat.START)) {
         drawer.closeDrawer(GravityCompat.START);
         } else {
         super.onBackPressed();
         }**/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id==R.id.help)
        {
            Intent intent=new Intent(Login.this,HomeActivity.class);

            // Intent intent=new Intent(Login.this,LoginActivity.class);

            //Intent intent=new Intent(Login.this,Tutor.class);
            startActivity(intent);
        }

        if (id==R.id.online_users){
            // Intent intent=new Intent(Login.this, EmployerActivity.class);
            // Intent intent=new Intent(Login.this, StudentHome.class);

            // startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        /**  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         drawer.closeDrawer(GravityCompat.START);**/
        return true;
    }



    private void initialise() {

        mPasswordView = (BootstrapEditText) findViewById(R.id.password);
        mEmailView = (BootstrapEditText) findViewById(R.id.email);

    }





    private void populateAutoComplete() {

        SharedPreferences sharedPreferences = getSharedPreferences(AppPrefs.APP_PREFS,MODE_PRIVATE);

        String email = sharedPreferences.getString(AppPrefs.PREF_EMAIL, null);

        if (email != null) {

            if (email.length() > 1) {

                mEmailView.setText(email.trim());
                mEmailView.setFocusable(true);
                mEmailView.setClickable(true);

                String password = sharedPreferences.getString(AppPrefs.PREF_PASSWORD, null);

                if(password != null) {
                    if(password.length() > 1) {

                        // mPasswordView.setText(password.trim());

                        //  attemptLogin();

                        //  new GeneralMethods().showToastToUser(Login.this,"Automatic Login");

                    }

                }

            }
        }

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        InputValidator inputValidator = new InputValidator();
        // Check for a valid password, if the user entered one.
        if ( !inputValidator.isPasswordValid(password)) {

            MDToast mdToast = MDToast.makeText(this, "This password is too short",Toast.LENGTH_LONG, MDToast.TYPE_ERROR);

            mdToast.show();
            // mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {

            MDToast mdToast = MDToast.makeText(this, "Password is required",Toast.LENGTH_LONG, MDToast.TYPE_ERROR);

            mdToast.show();
            // mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            MDToast mdToast = MDToast.makeText(this, "Email is required",Toast.LENGTH_LONG, MDToast.TYPE_ERROR);

            mdToast.show();
            //mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!inputValidator.isEmailValid(email)) {
            MDToast mdToast = MDToast.makeText(this, "This email address is invalid",Toast.LENGTH_LONG, MDToast.TYPE_ERROR);

            mdToast.show();
            //  mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            //   progressDialog = new GeneralMethods().progressDialog(Login.this, null,EnumAppMessages.DIALOG_LOADING.getValue());
            //  progressDialog.show();

            progressBar.setVisibility(View.VISIBLE);
            setProgressBarIndeterminateVisibility(true);

            authenticateUser(email,password);
            MDToast mdToast = MDToast.makeText(this, "Correct Details.",Toast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);

            mdToast.show();

        }
    }

    private void authenticateUser(final String email, final String password) {

        generalMethods = new GeneralMethods();

        final ReqBeanLogin reqBean = new ReqBeanLogin();
        reqBean.setEmail(email);
        reqBean.setPassword(password);
        reqBean.setType(AppProperties.SERVER_REQUEST_TYPE);
        reqBean.set_token(AppProperties.SERVER_REQUEST_TOKEN);
       /* RetrofitService service = new LocalRetrofitApi().getRetrofitService();

        Call<ResBeanLogin> call = service.authenticate(reqBean);
        call.enqueue(new Callback<ResBeanLogin>() {
            @Override
            public void onResponse(Call<ResBeanLogin> call, Response<ResBeanLogin> response) {
                //  progressDialog.dismiss();
                progressBar.setVisibility(View.GONE);

                if(response.code() == AppNums.STATUS_COD_SUCCESS){
                    ResBeanLogin resBean = response.body();

                    Log.e("Response status",resBean.getResponse_status());

                    if(resBean.getLogin_status().equalsIgnoreCase(Boolean.TRUE.toString())){

                        SharedPreferences sharedPreferences = getSharedPreferences(AppPrefs.APP_PREFS,MODE_PRIVATE);
                        final SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString(AppPrefs.PREF_EMAIL,email).putString(AppPrefs.PREF_PASSWORD,password);

                        editor.putString(AppPrefs.PREF_USER_TYPE,resBean.getProfile_details().getRole());

                        editor.apply();

                        // startThreadToStoreProfileInformation(resBean);

                        startActivity(new Intent(Login.this, HomeActivity.class));
                        finish();

                    }else{
                        //  Toast.makeText(Login.this,resBean.getError(),Toast.LENGTH_SHORT).show();
                        MDToast mdToast = MDToast.makeText(Login.this,resBean.getError(),Toast.LENGTH_SHORT, MDToast.TYPE_ERROR);

                        mdToast.show();
                    }
                }else if(response.code() == AppNums.STATUS_COD_FILE_NOT_FOUND){

                    generalMethods.showLocationDialog(Login.this, EnumAppMessages.LOGIN_ERROR_TITLE.getValue(),EnumAppMessages.ERROR_RESOURCE_NOT_FOUND.getValue());

                }else{

                    generalMethods.showLocationDialog(Login.this,EnumAppMessages.LOGIN_ERROR_TITLE.getValue(),EnumAppMessages.ERROR_INTERNAL_ERROR.getValue());

                }

            }

            @Override
            public void onFailure(Call<ResBeanLogin> call, Throwable t) {
                //  progressDialog.dismiss();

                progressBar.setVisibility(View.GONE);

                if(t instanceof IOException){
                    generalMethods.showLocationDialog(Login.this,EnumAppMessages.LOGIN_ERROR_TITLE.getValue(), EnumAppMessages.ERROR_INTERNET_CONNECTION.getValue());
                }else {
                    generalMethods.showLocationDialog(Login.this,EnumAppMessages.LOGIN_ERROR_TITLE.getValue(),EnumAppMessages.ERROR_UNKNOWN_ERROR.getValue());
                }
            }
        });*/
    }
    private void saveLoginDetails (String email,String password){
        new PrefManager(this).saveLoginDetails(email,password);
    }

    /*
     * Start a thread that Saves user profile information to SQlite DB forexample the biodata, the mandatory location and the optional
     * location , the Payment accounts
     * */
    private void startThreadToStoreProfileInformation(final ResBeanLogin resBean) {

        Thread threadSaveProfileDetails = new Thread(new Runnable() {
            @Override
            public void run() {

                ResBeanUserProfile resBeanUserProfile = resBean.getProfile_details();
                Log.e("Profile",resBeanUserProfile.getMember_firstname());

                storeUserProfileInformation(resBeanUserProfile,resBean);

            }
        });

        threadSaveProfileDetails.start();


    }

    private void storeUserProfileInformation(ResBeanUserProfile resBeanUserProfile, ResBeanLogin resBean) {

        String firstName = resBeanUserProfile.getMember_firstname();
        String lastName = resBeanUserProfile.getMember_lastname();
        String contact = resBeanUserProfile.getMember_contact();
        String sex = resBeanUserProfile.getMember_sex();
        String regDate = resBeanUserProfile.getReg_date();
        String userName = resBeanUserProfile.getMember_username();
        String businessName = resBeanUserProfile.getBusiness_name();
        String email = resBeanUserProfile.getMember_email();
        String role = resBeanUserProfile.getRole();
        String status = resBeanUserProfile.getStatus();

        UserBioData userBioData = new UserBioData(firstName, lastName, contact, sex, regDate, userName, businessName, email, role, status);
        UserLocation userLocationMandatory = resBean.getMandatory_location();
        UserLocation userLocationOptional = resBean.getOptional_location();
        UserPaymentAccount userPaymentAccount = resBean.getPayment_accounts();

        try{

            final DBHandler dbHandler = DBHandler.getInstance(Login.this);

            dbHandler.saveUserProfileInformation(userBioData,userLocationMandatory,userLocationOptional);

            // dbHandler.saveUserPaymentAccount(userPaymentAccount,email);


        }catch (Exception e){

            new GeneralMethods().getExceptionLocation("Loign","Login","storeUserProfileInformation",e);

        }


    }

    public void checkOption(View view){
        int id = view.getId();
        switch (id){
            case R.id.txt_signup:{
                //  startActivity(new Intent(Login.this,SignUp.class));
                startActivity(new Intent(Login.this, SignUp.class));
                finish();
                break;
            }
            case R.id.txt_forgot_password:{
                // startActivity(new Intent(Login.this,Home.class));

                // startActivity(new Intent(Login.this,SplashScreen.class));
                finish();
                break;
            }default:{
                break;
            }
        }
    }


}


