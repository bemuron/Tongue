package viola1.agrovc.com.tonguefinal.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.BootstrapProgressBar;
import com.beardedhen.androidbootstrap.TypefaceProvider;

import com.valdesekamdem.library.mdtoast.MDToast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import viola1.agrovc.com.tonguefinal.Others.AdminContact;
import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.Tabs.TabFragment;
import viola1.agrovc.com.tonguefinal.app.localserver.request.ReqBeanSignup;
import viola1.agrovc.com.tonguefinal.app.localserver.request.response.ResBeanSignup;
import viola1.agrovc.com.tonguefinal.constants.AppErrors;
import viola1.agrovc.com.tonguefinal.constants.AppNums;
import viola1.agrovc.com.tonguefinal.constants.EnumAppMessages;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.LocalRetrofitApi;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.RetrofitService;
import viola1.agrovc.com.tonguefinal.helper.Encryption;
import viola1.agrovc.com.tonguefinal.helper.GeneralMethods;
import viola1.agrovc.com.tonguefinal.helper.InputValidator;

public class SignUp extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RelativeLayout mainContainer;

    BootstrapEditText edtEmail;
    BootstrapEditText edtPassword;
    BootstrapEditText edtConfirmPassword;


    BootstrapButton btnSignup;
    TextView login;
    private View focusView;
    private BootstrapProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TypefaceProvider.registerDefaultIconSets();
        setContentView(R.layout.signup);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Account Registration");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (BootstrapProgressBar) findViewById(R.id.progressBar1);
        // toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_navigation_arrow_back));

        toolbar.setTitle("Sign Up");


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        // mainContainer = (RelativeLayout) findViewById(R.id.MainContainer);

        /**  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
         this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
         drawer.setDrawerListener(toggle);
         toggle.syncState();

         NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
         navigationView.setNavigationItemSelectedListener(this);

         /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        TabFragment tabs = new TabFragment();
        // mainContainer.removeAllViewsInLayout();
        //ft.replace(R.id.MainContainer, tabs, null);
        ft.commit();

        initialise();


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;}


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

        if (id==R.id.help){
            // Intent intent=new Intent(SignUp.this, HelpCenter.class);
            //startActivity(intent);
        }

        if (id==R.id.online_users){
            //Intent intent=new Intent(SignUp.this,AdminContact.class);

            // startActivity(intent);
        }

        if (item.getItemId() == android.R.id.home) {
            //finish();
            Intent intent=new Intent(SignUp.this,Login.class);
            startActivity(intent);
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


    /*
     * Get references to widgets
     * */
    private void initialise() {



        edtEmail = (BootstrapEditText) findViewById(R.id.edtEmail);

        edtPassword = (BootstrapEditText) findViewById(R.id.edtPassword);

        edtConfirmPassword = (BootstrapEditText) findViewById(R.id.edtConfirmPassword);

        btnSignup = (BootstrapButton) findViewById(R.id.btnSignup);

        login = (TextView) findViewById(R.id.login);

    }

    /*
     * Handles sign up button click, validates user input and initiates signup
     * */
    public void signUpOnClick(View view){
        progressBar.setVisibility(View.VISIBLE);
        if(isValidUserInput()){
            progressBar.setVisibility(View.VISIBLE);
            setProgressBarIndeterminateVisibility(true);

            String mEmail = edtEmail.getText().toString();

            String mPassword = edtPassword.getText().toString();



            ReqBeanSignup reqBean = new ReqBeanSignup();

            reqBean.setEmail(Encryption.encrypt(mEmail));
            reqBean.setPassword(Encryption.encrypt(mPassword));

            new DataLoaderSignUp(SignUp.this).userSignUp(reqBean);

            //  Toast.makeText(this,"Correct input",Toast.LENGTH_SHORT).show();


            MDToast mdToast = MDToast.makeText(this, "Correct input",Toast.LENGTH_SHORT,MDToast.TYPE_SUCCESS);

            mdToast.show();
            progressBar.setVisibility(View.VISIBLE);
        }else{
            focusView.requestFocus();
            progressBar.setVisibility(View.GONE);
        }

    }

    /* Validates user input languageSearchList*/
    private boolean isValidUserInput() {

        focusView = null;

        InputValidator validator = new InputValidator();

        String email = edtEmail.getText().toString();

        String password = edtPassword.getText().toString();
        String confirm_password = edtConfirmPassword.getText().toString();



        if(!validator.isEmailValid(email)){
            focusView = edtEmail;
            // edtEmail.setError(AppErrors.INVALID_EMAIL);
            MDToast mdToast = MDToast.makeText(SignUp.this, AppErrors.INVALID_EMAIL,Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            return false;
        }



        if(!password.equals(confirm_password)){
            focusView = edtPassword;
            MDToast mdToast = MDToast.makeText(SignUp.this,AppErrors.INVALID_PASSWORD_MISMATCH,MDToast.TYPE_ERROR);

            mdToast.show();
            // edtPassword.setError(AppErrors.INVALID_PASSWORD_MISMATCH);
            return false;
        }else{
            if(!validator.isPasswordValid(password)){
                focusView = edtConfirmPassword;
                MDToast mdToast = MDToast.makeText(SignUp.this,AppErrors.INVALID_PASSWORD,MDToast.TYPE_ERROR);

                mdToast.show();
                //edtPassword.setError(AppErrors.INVALID_PASSWORD);
                return false;
            }
        }

        return true;

    }




    public void checkOption(View view){
        int id = view.getId();
        switch (id){
            case R.id.login:{
                startActivity(new Intent(SignUp.this,Login.class));
                finish();
                break;
            }
            default:{
                break;
            }
        }
    }


    public class DataLoaderSignUp {
        Activity activity;
        //RetrofitService service = new LocalRetrofitApi().getRetrofitService();
        // ProgressDialog progressDialog;



        GeneralMethods generalMethods = new GeneralMethods();

        public DataLoaderSignUp(Activity activity) {
            this.activity = activity;
        }

        /*
         * Send registration details to the server
         * */
        public void userSignUp(ReqBeanSignup reqBean) {
            progressBar.setVisibility(View.VISIBLE);

            startProgressDialog();
            /*Call<ResBeanSignup> call = service.registerUser(reqBean);
            call.enqueue(new Callback<ResBeanSignup>() {
                @Override
                public void onResponse(Call<ResBeanSignup> call, Response<ResBeanSignup> response) {
                    progressBar.setVisibility(View.GONE);
                    // stopProgressDialog();

                    if (response.code() == AppNums.STATUS_COD_SUCCESS) {

                        ResBeanSignup resBean = response.body();
                        String response_status = Encryption.decrypt(resBean.getResponse_status());

                        if(response_status.equalsIgnoreCase(EnumAppMessages.RESPONSE_STATUS_SUCCESS.getValue())){


                            edtEmail.getText().clear();

                            edtPassword.getText().clear();
                            edtPassword.getText().clear();

                            MDToast mdToast = MDToast.makeText(activity, EnumAppMessages.REGISTER_SUCCESS_TITLE.getValue(), Toast.LENGTH_LONG,MDToast.TYPE_SUCCESS);

                            mdToast.show();
                            startActivity(new Intent(SignUp.this,Login.class));
                            finish();
                            generalMethods.showLocationDialog(activity,EnumAppMessages.REGISTER_SUCCESS_TITLE.getValue(),Encryption.decrypt(resBean.getRegistration_status()));



                        }else{
                            MDToast mdToast = MDToast.makeText(activity, EnumAppMessages.REGISTER_ERROR_TITLE.getValue(), Toast.LENGTH_LONG,MDToast.TYPE_ERROR);

                            mdToast.show();
                            generalMethods.showLocationDialog(activity,EnumAppMessages.REGISTER_ERROR_TITLE.getValue(),Encryption.decrypt(resBean.getError()));

                        }
                    }else if(response.code() == AppNums.STATUS_COD_FILE_NOT_FOUND){
                        MDToast mdToast = MDToast.makeText(activity, EnumAppMessages.REGISTER_ERROR_TITLE.getValue(), Toast.LENGTH_LONG,MDToast.TYPE_ERROR);

                        mdToast.show();
                        generalMethods.showLocationDialog(activity,EnumAppMessages.REGISTER_ERROR_TITLE.getValue(),EnumAppMessages.ERROR_RESOURCE_NOT_FOUND.getValue());
                    }else{
                        MDToast mdToast = MDToast.makeText(activity, EnumAppMessages.REGISTER_ERROR_TITLE.getValue(), Toast.LENGTH_LONG,MDToast.TYPE_ERROR);

                        mdToast.show();
                        generalMethods.showLocationDialog(activity,EnumAppMessages.REGISTER_ERROR_TITLE.getValue(),EnumAppMessages.ERROR_INTERNAL_ERROR.getValue());
                    }
                }

                @Override
                public void onFailure(Call<ResBeanSignup> call, Throwable t) {
                    stopProgressDialog();
                    if(t instanceof IOException){
                        MDToast mdToast = MDToast.makeText(activity, EnumAppMessages.REGISTER_ERROR_TITLE.getValue(), Toast.LENGTH_LONG,MDToast.TYPE_ERROR);

                        mdToast.show();
                        generalMethods.showLocationDialog(activity, EnumAppMessages.REGISTER_ERROR_TITLE.getValue(), EnumAppMessages.ERROR_INTERNET_CONNECTION.getValue());

                    }else {
                        MDToast mdToast = MDToast.makeText(activity, EnumAppMessages.REGISTER_ERROR_TITLE.getValue(), Toast.LENGTH_LONG,MDToast.TYPE_ERROR);

                        mdToast.show();
                        generalMethods.showLocationDialog(activity,EnumAppMessages.REGISTER_ERROR_TITLE.getValue(),EnumAppMessages.ERROR_UNKNOWN_ERROR.getValue());
                    }
                }
            });*/
        }


        private void startProgressDialog(){
            // progressDialog = new GeneralMethods().progressDialog(activity, null,EnumAppMessages.DIALOG_PROCESSING.getValue());
            // progressDialog.show();
            progressBar.setVisibility(View.VISIBLE);
            setProgressBarIndeterminateVisibility(true);
        };

        private void stopProgressDialog(){
            progressBar.setVisibility(View.GONE);
            //progressDialog.dismiss();
        };

    }

}


