package viola1.agrovc.com.tonguefinal.presentation.ui.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.valdesekamdem.library.mdtoast.MDToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.constants.AppErrors;
import viola1.agrovc.com.tonguefinal.data.network.Result;
import viola1.agrovc.com.tonguefinal.data.network.api.APIService;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.LocalRetrofitApi;
import viola1.agrovc.com.tonguefinal.helper.InputValidator;
import viola1.agrovc.com.tonguefinal.helper.SessionManager;
import viola1.agrovc.com.tonguefinal.presentation.viewmodels.LoginRegisterActivityViewModel;
import viola1.agrovc.com.tonguefinal.presentation.viewmodels.LoginRegistrationViewModelFactory;
import viola1.agrovc.com.tonguefinal.utilities.InjectorUtils;

public class RegisterActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private LoginRegisterActivityViewModel loginRegisterActivityViewModel;
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText fullname;
    private EditText inputEmail;
    private DatePicker datePicker;
    private Dialog dateDialog;
    private RadioGroup genderRadioGroup;
    private RadioButton genderRadioButton;
    private ArrayList<String> radioOptions = new ArrayList<>();;
    private TypedArray gender;
    private String dobForMysql, genderSelected;
    private EditText dobEditText;
    private EditText inputPassword;
    private EditText confirmPassword;
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DOB = "dd MMM yyyy";
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

        fullname = findViewById(R.id.edit_text_register_name);
        dobEditText = findViewById(R.id.edit_text_register_dob);
        inputEmail = findViewById(R.id.edit_text_register_email);
        inputPassword = findViewById(R.id.edit_text_register_password);
        confirmPassword = findViewById(R.id.edit_text_register_confirm_password);
        btnRegister = findViewById(R.id.btnRegister);
        btnLinkToLogin = findViewById(R.id.btnLinkToLoginScreen);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        gender = getResources().obtainTypedArray(R.array.gender);
        onDobClick();
        setUpGenderRadios();

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

                String userName = fullname.getText().toString().trim();

                String date_of_birth = dobEditText.getText().toString().trim();

                String mEmail = inputEmail.getText().toString().trim();

                String mPassword = inputPassword.getText().toString().trim();

                if (!mEmail.isEmpty() && !mPassword.isEmpty() &&
                        !userName.isEmpty() && !date_of_birth.isEmpty() &&
                        !dobForMysql.isEmpty() &&
                        !genderSelected.isEmpty()) {
                    hideDialog();
                    registerUser(userName, dobForMysql, genderSelected, mEmail, mPassword);
                }else{
                    hideDialog();
                    MDToast mdToast = MDToast.makeText(
                            RegisterActivity.this, "Please fill out all the fields",
                            Toast.LENGTH_SHORT,MDToast.TYPE_SUCCESS);

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
    private void registerUser(final String name, final String date_of_birth, final String gender,
                              final String email, final String password) {
        //disable clicks on the register button during registration process
        btnRegister.setClickable(false);

        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        //Defining retrofit api service*/
        //APIService service = retrofit.create(APIService.class);
        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<Result> call = service.createUser(name,date_of_birth,gender,email,password);

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
                    Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                    btnRegister.setClickable(true);
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

    //set up the radio buttons for the user gender
    private void setUpGenderRadios(){
        genderRadioGroup.setOnCheckedChangeListener(this);
        genderRadioGroup.removeAllViews();
        for (int i = 0; i < gender.length(); i++) {
            if (gender != null) {
                genderRadioButton = new RadioButton(this);
                //opt_1.setGravity(Gravity.CENTER);
                // opt_1.setButtonDrawable(android.R.color.transparent);
                genderRadioButton.setText(gender.getText(i));
                genderRadioButton.setPadding(5, 0, 5, 0);
                genderRadioButton.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                genderRadioButton.setTextSize(18);
                genderRadioGroup.addView(genderRadioButton);
            }
        }
    }

    //handles click on the dobEditText field
    private void onDobClick(){
        //to handle click on dobEditText edit text field
        //shows date picker dialog for user to select their dobEditText
        dobEditText.setInputType(InputType.TYPE_NULL);
        dobEditText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                getUserDateOfBirth();
            }
        });

        dobEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getUserDateOfBirth();
                }
            }
        });
    }

    //handles user selected date of birth from popup dialog
    private void getUserDateOfBirth(){
        dateDialog = new Dialog(this);
        dateDialog.setContentView(R.layout.registration_dob_picker);

        //find our picker
        datePicker = dateDialog.findViewById(R.id.dobDatePicker);

        //find our buttons
        Button setDate = dateDialog.findViewById(R.id.button_picker_set_dob);

        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the date the user sets
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                //create a new calendar set to the date chosen
                Calendar calendar = Calendar.getInstance();
                //calendar.set(year,month,day,hour,minute);
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);

                System.out.println("Current time => " + calendar.getTime());
                SimpleDateFormat df = new SimpleDateFormat(DOB, Locale.getDefault());
                String date = df.format(calendar.getTime());

                //display the date on the edit text
                dobEditText.setText(date);

                //the date format expected by mysql
                SimpleDateFormat dobf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
                dobForMysql = dobf.format(calendar.getTime());

                dateDialog.dismiss();

            }
        });

        dateDialog.show();
    }

    //handle the radio button selected
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        int radioButtonId = genderRadioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = genderRadioGroup.findViewById(radioButtonId);
        //oSelectedCount++;
        genderSelected = (String) radioButton.getText();
        Log.i(TAG, "radio selected "+ genderSelected);
    }
}
