package viola1.agrovc.com.tonguefinal.presentation.ui.activities;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.valdesekamdem.library.mdtoast.MDToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.app.localserver.request.ReqBeanSignup;
import viola1.agrovc.com.tonguefinal.constants.AppErrors;
import viola1.agrovc.com.tonguefinal.constants.AppNums;
import viola1.agrovc.com.tonguefinal.data.network.Result;
import viola1.agrovc.com.tonguefinal.data.network.api.APIService;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.LocalRetrofitApi;
import viola1.agrovc.com.tonguefinal.helper.Encryption;
import viola1.agrovc.com.tonguefinal.helper.InputValidator;
import viola1.agrovc.com.tonguefinal.helper.SessionManager;
import viola1.agrovc.com.tonguefinal.presentation.viewmodels.LoginRegisterActivityViewModel;
import viola1.agrovc.com.tonguefinal.presentation.viewmodels.LoginRegistrationViewModelFactory;
import viola1.agrovc.com.tonguefinal.utilities.InjectorUtils;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private LoginRegisterActivityViewModel loginRegisterActivityViewModel;
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputEmail;
    private RadioGroup radioGender;
    private EditText dob;
    private EditText inputPassword;
    private EditText confirmPassword;
    private View focusView;
    private ProgressDialog pDialog;
    private SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        LoginRegistrationViewModelFactory factory = InjectorUtils.provideLoginRegistrationViewModelFactory(this.getApplicationContext());
        loginRegisterActivityViewModel = ViewModelProviders.of
                (this, factory).get(LoginRegisterActivityViewModel.class);

        inputEmail = findViewById(R.id.edit_text_register_email);
        inputPassword = findViewById(R.id.edit_text_register_password);
        confirmPassword = findViewById(R.id.edit_text_register_confirm_password);
        btnRegister = findViewById(R.id.btnRegister);
        btnLinkToLogin = findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    HomeActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        //get the info the user has typed in displaying errors where necessary
        btnRegister.setOnClickListener(new View.OnClickListener()  {
            public void onClick(View view) {
            //validating the user input values and attempting registration if
            // they are okay
            showDialog();
            if(isValidUserInput()){
                showDialog();
                setProgressBarIndeterminateVisibility(true);

                String mEmail = inputEmail.getText().toString().trim();

                String mPassword = inputPassword.getText().toString().trim();

                if (!mEmail.isEmpty() && !mPassword.isEmpty()) {
                    hideDialog();
                    registerUser(mEmail, mPassword);
                }else{
                    hideDialog();
                    MDToast mdToast = MDToast.makeText(RegisterActivity.this, "Password or email not received ",Toast.LENGTH_SHORT,MDToast.TYPE_SUCCESS);

                    mdToast.show();
                }

                //MDToast mdToast = MDToast.makeText(this, "Correct input",Toast.LENGTH_SHORT,MDToast.TYPE_SUCCESS);

                //mdToast.show();
            }else{
                focusView.requestFocus();
                hideDialog();
            }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }//close onCreate()

    /**
     * Method to call viewmodel method to post user reg details to database
     * */
    private void registerUser(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        //Defining retrofit api service*/
        //APIService service = retrofit.create(APIService.class);
        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<Result> call = service.createUser("bruno","1991-08-11","male",email,password);

        //calling the com.emtech.retrofitexample.api
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                hideDialog();

                    //ResBeanSignup resBean = response.body();
                    //String response_status = Encryption.decrypt(resBean.getResponse_status());

                    if (!response.body().getError()) {
                        hideDialog();
                        //Log.d(LOG_TAG, EnumAppMessages.REGISTER_SUCCESS_TITLE.getValue());
                        Log.d(TAG, response.body().getMessage());

                        MDToast mdToast = MDToast.makeText(RegisterActivity.this, "Registration successful. Login now", Toast.LENGTH_LONG, MDToast.TYPE_SUCCESS);

                        mdToast.show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                        /*edtEmail.getText().clear();

                        edtPassword.getText().clear();
                        edtPassword.getText().clear();

                        MDToast mdToast = MDToast.makeText(activity, EnumAppMessages.REGISTER_SUCCESS_TITLE.getValue(), Toast.LENGTH_LONG,MDToast.TYPE_SUCCESS);

                        mdToast.show();
                        startActivity(new Intent(SignUp.this,Login.class));
                        finish();
                        generalMethods.showLocationDialog(activity,EnumAppMessages.REGISTER_SUCCESS_TITLE.getValue(),Encryption.decrypt(resBean.getRegistration_status()));*/


                    }
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    hideDialog();
                    //print out any error we may get
                    //probably server connection
                    Log.e(TAG, t.getMessage());
                }
            });

        }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    /* Validates user input data*/
    private boolean isValidUserInput() {

        focusView = null;

        InputValidator validator = new InputValidator();

        String email = inputEmail.getText().toString();

        String password = inputPassword.getText().toString();
        String confirm_password = confirmPassword.getText().toString();



        if(!validator.isEmailValid(email)){
            focusView = inputEmail;
            // edtEmail.setError(AppErrors.INVALID_EMAIL);
            MDToast mdToast = MDToast.makeText(RegisterActivity.this, AppErrors.INVALID_EMAIL,Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            return false;
        }

        if(!password.equals(confirm_password)){
            focusView = confirmPassword;
            MDToast mdToast = MDToast.makeText(RegisterActivity.this,AppErrors.INVALID_PASSWORD_MISMATCH,MDToast.TYPE_ERROR);

            mdToast.show();
            // edtPassword.setError(AppErrors.INVALID_PASSWORD_MISMATCH);
            return false;
        }else{
            if(!validator.isPasswordValid(password)){
                focusView = inputPassword;
                MDToast mdToast = MDToast.makeText(RegisterActivity.this,AppErrors.INVALID_PASSWORD,MDToast.TYPE_ERROR);

                mdToast.show();
                //edtPassword.setError(AppErrors.INVALID_PASSWORD);
                return false;
            }
        }

        return true;

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
