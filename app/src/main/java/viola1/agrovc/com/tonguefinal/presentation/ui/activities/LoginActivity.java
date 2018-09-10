package viola1.agrovc.com.tonguefinal.presentation.ui.activities;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.helper.SessionManager;
import viola1.agrovc.com.tonguefinal.presentation.viewmodels.LoginRegisterActivityViewModel;
import viola1.agrovc.com.tonguefinal.presentation.viewmodels.LoginRegistrationViewModelFactory;
import viola1.agrovc.com.tonguefinal.utilities.InjectorUtils;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private LoginRegisterActivityViewModel loginRegisterActivityViewModel;
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
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

    }//close onCreate

    /**
     * function to verify login details in mysql db
     * Calls viewmodel method
     * */
    private void checkLogin(final String email, final String password) {

        pDialog.setMessage("Logging in ...");
        showDialog();

        //call viewmodel method in attempt to log in user
        loginRegisterActivityViewModel.loginUser(email, password);

        // user successfully logged in
        // Create login session
        session.setLogin(true);

        Log.d(TAG, "Login Success ");
        hideDialog();

        // Launch main activity
        Intent intent = new Intent(LoginActivity.this,
                HomeActivity.class);
        startActivity(intent);
        finish();

                /*
        if (loginRegisterActivityViewModel.loginUser(email, password)){
            Log.d(TAG, "Login Success ");
            hideDialog();

            // user successfully logged in
            // Create login session
            session.setLogin(true);

        }else {
            Toast.makeText(this, "Sorry couldn't log you in. " +
                    "Please check your email and password", Toast.LENGTH_LONG).show();
        }
        */

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
