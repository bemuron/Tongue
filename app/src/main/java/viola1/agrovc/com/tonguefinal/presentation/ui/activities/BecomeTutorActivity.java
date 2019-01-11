package viola1.agrovc.com.tonguefinal.presentation.ui.activities;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.app.MyApplication;
import viola1.agrovc.com.tonguefinal.constants.AppNums;
import viola1.agrovc.com.tonguefinal.data.network.Result;
import viola1.agrovc.com.tonguefinal.data.network.api.APIService;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.LocalRetrofitApi;
import viola1.agrovc.com.tonguefinal.helper.SessionManager;
import viola1.agrovc.com.tonguefinal.models.User;
import viola1.agrovc.com.tonguefinal.presentation.ui.fragments.BecomeTutorAlertDialog;
import viola1.agrovc.com.tonguefinal.presentation.ui.fragments.MeetingScheduleAlertDialog;
import viola1.agrovc.com.tonguefinal.presentation.viewmodels.UserProfileActivityViewModel;
import viola1.agrovc.com.tonguefinal.presentation.viewmodels.UserProfileActivityViewModelFactory;
import viola1.agrovc.com.tonguefinal.utilities.InjectorUtils;

public class BecomeTutorActivity extends AppCompatActivity {
    private static final String TAG = BecomeTutorActivity.class.getSimpleName();
    static final int PICK_LANGUAGE_REQUEST = 5;
    private UserProfileActivityViewModel userProfileActivityViewModel;
    private EditText aboutUser;
    private Button addLanguage, becomeTutorButton;
    private TextView addedLanguages, addAboutInstructions;
    private ProgressDialog pDialog;
    private SessionManager session;
    private String selectedLanguage;
    private int selectedLanguageId, userId;
    private List<Integer> languagesSpokenIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_tutor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        //get user id from shared prefs
        userId = session.getUserId();

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        aboutUser = findViewById(R.id.tutorAddAboutEv);
        addLanguage = findViewById(R.id.userAddLanguagesButton);
        becomeTutorButton = findViewById(R.id.becomeTutorButton);
        addedLanguages = findViewById(R.id.userAddedLanguagesTv);
        //addAboutInstructions = findViewById(R.id.instructionsWriteAboutTv);
        //addAboutInstructions.setVisibility(View.INVISIBLE);
        //aboutUser.setVisibility(View.INVISIBLE);

        UserProfileActivityViewModelFactory factory = InjectorUtils.provideUserProfileViewModelFactory(this.getApplicationContext());
        userProfileActivityViewModel = ViewModelProviders.of
                (this, factory).get(UserProfileActivityViewModel.class);

        userProfileActivityViewModel.getUserDetails().observe(this, user -> {

            //populateView(user);

//            Log.i(TAG, "User name "+user.get(0).getName());

        });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //handle adding languages
        handleAddLanguages();

        //become tutor button is handled here
        handleBecomeTutor();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }//close onCreate

    /*//populate the different views with the user data
    private void populateView(List<User> user){
        if (user.get(0).getDescription() == null){
            addAboutInstructions.setVisibility(View.VISIBLE);
            aboutUser.setVisibility(View.VISIBLE);
        }

    }*/

    //method to handle click on add languags button
    private void handleAddLanguages(){
        addLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickLanguageIntent = new Intent(BecomeTutorActivity.this, SearchLanguagesActivity.class);
                pickLanguageIntent.putExtra("becomeTutor", "becomeTutor");
                startActivityForResult(pickLanguageIntent, PICK_LANGUAGE_REQUEST);
            }
        });
    }

    //get the language the use has picked
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //check for our request code
        if (requestCode == PICK_LANGUAGE_REQUEST){
            //check if the request was successful
            if (resultCode == RESULT_OK){
                //selectedLanguage = data.getDataString();
                selectedLanguage = data.getStringExtra("language_name");
                selectedLanguageId = data.getIntExtra("language_id", 0);
                Log.i(TAG, "language selected = "+ selectedLanguage);

                //add the selected language to the array
                languagesSpokenIds.add(selectedLanguageId);

                //display the selected language in the text view
                addedLanguages.append(selectedLanguage + " | ");
            }
        }
    }

    //this method handles adding the languages spoken by the user to the db
    //it loops through the array of the languages added and on each index
    //calls the retrofit method to add the language
    private void handleAddUserLanguageSpoken(){
        for (int i=0; i < languagesSpokenIds.size(); i++){
            //call retrofit method
            addLanguageSpoken(languagesSpokenIds.get(i));
        }
    }

    //call to add the languages spoken by the user
    private void addLanguageSpoken(int selectedLanguageId){
        pDialog.setMessage("Adding language(s) ...");
        showDialog();


        becomeTutorButton.setEnabled(false);

        Log.d(TAG, "User become tutor process started");


        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        //Call<ResBeanLogin> call = service.authenticate(reqBean);
        Call<Result> call = service.addlanguageSpokenId(selectedLanguageId,userId);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                //if(response.code() == AppNums.STATUS_COD_SUCCESS) {
                    hideDialog();
                    //if response body is not null, we have some data
                    //successful addition
                    if (!response.body().getError()) {
                        Toast.makeText(BecomeTutorActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                //}
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                hideDialog();

                MDToast mdToast = MDToast.makeText(BecomeTutorActivity.this, t.getMessage(), Toast.LENGTH_SHORT, MDToast.TYPE_ERROR);

                mdToast.show();

                becomeTutorButton.setEnabled(true);

            }
        });
    }

    //retrofit call to update user role to tutor
    private void updateUserRole(){

        pDialog.setMessage("Updating role ...");
        showDialog();


        becomeTutorButton.setEnabled(false);

        Log.d(TAG, "Updating user role");


        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        //Call<ResBeanLogin> call = service.authenticate(reqBean);
        Call<Result> call = service.updateUserRole(userId, userId);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                //if(response.code() == AppNums.STATUS_COD_SUCCESS) {
                hideDialog();
                //if response body is not null, we have some data
                //successful addition
                if (!response.body().getError()) {
                    notifyUser();
                    Toast.makeText(BecomeTutorActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                //}
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                hideDialog();

                MDToast mdToast = MDToast.makeText(BecomeTutorActivity.this, t.getMessage(), Toast.LENGTH_SHORT, MDToast.TYPE_ERROR);

                mdToast.show();

                becomeTutorButton.setEnabled(true);

            }
        });
    }

    //method to handle click on become tutor button
    private  void handleBecomeTutor(){
        becomeTutorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String userDesc = aboutUser.getText().toString().trim();

                if (languagesSpokenIds != null){
                    //add the language ids
                    handleAddUserLanguageSpoken();
                    //update user role
                    updateUserRole();

                }else {
                    Toast.makeText(BecomeTutorActivity.this, "Please add " +
                            "some languages you can speak and teach comfortably", Toast.LENGTH_LONG).show();
                }
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

    //dialog to let user know when they are now a tutor
    public void notifyUser(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.mipmap.ic_launcher_foreground);
        alertDialog.setTitle("Congratulations: You are now a tutor");
        //To prevent dialog box from getting dismissed on back key pressed
        alertDialog.setCancelable(false);

        alertDialog.setMessage("You'll be logged out now " +
                "as we freshen things up for you, then you can log in.");

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //log out user when they become a tutor so that they login and their fresh
                //details are retrieved
                logoutUser();
            }
        });
        alertDialog.show();
    }

    //alert dialog
    /*void showAlertDialog(String title, String message) {
        FragmentManager fm = getFragmentManager();

        DialogFragment newFragment = BecomeTutorAlertDialog.newInstance(title, message);
        newFragment.show(fm, "Becom");
    }*/

  /*  *//**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * *//*
    private void logoutUser() {
        session.setLogin(false);
        session.logoutUser();
        userProfileActivityViewModel.delete();
    }*/

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);
        MyApplication.getInstance().logout();
        session.logoutUser();
        userProfileActivityViewModel.delete();
    }

}
