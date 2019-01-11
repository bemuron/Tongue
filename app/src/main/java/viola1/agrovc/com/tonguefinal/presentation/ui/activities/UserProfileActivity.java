package viola1.agrovc.com.tonguefinal.presentation.ui.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.helper.SessionManager;
import viola1.agrovc.com.tonguefinal.models.User;
import viola1.agrovc.com.tonguefinal.presentation.viewmodels.UserProfileActivityViewModel;
import viola1.agrovc.com.tonguefinal.presentation.viewmodels.UserProfileActivityViewModelFactory;
import viola1.agrovc.com.tonguefinal.utilities.InjectorUtils;

public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = UserProfileActivity.class.getSimpleName();
    private UserProfileActivityViewModel userProfileActivityViewModel;
    private List<User> mUserDetails;
    private CircleImageView mUserProfilePic;
    private TextView mUserName, mUserDob, mUserGender,
            mUserPhoneNumber, mUserAbout, mUserEmail, mUserRole, aboutTextView;
    private Button mBecomeTutor;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        mUserProfilePic = findViewById(R.id.userProfileImageView);
        mUserName = findViewById(R.id.userProfileNameTv);
        mUserDob = findViewById(R.id.userProfileDobTv);
        mUserGender = findViewById(R.id.userProfileGenderTv);
        mUserPhoneNumber = findViewById(R.id.userProfileNumberTv);
        mUserPhoneNumber.setVisibility(View.INVISIBLE);
        mUserAbout = findViewById(R.id.userProfileAboutTv);
        aboutTextView = findViewById(R.id.userProfileAboutTextView);
        mUserEmail = findViewById(R.id.userProfileEmailTv);
        mUserRole = findViewById(R.id.userProfileRoleTv);
        mBecomeTutor = findViewById(R.id.userProfileBecomeTutorButton);


        mBecomeTutor.setVisibility(View.INVISIBLE);

        UserProfileActivityViewModelFactory factory = InjectorUtils.provideUserProfileViewModelFactory(this.getApplicationContext());
        userProfileActivityViewModel = ViewModelProviders.of
                (this, factory).get(UserProfileActivityViewModel.class);

        userProfileActivityViewModel.getUserDetails().observe(this, user -> {

            mUserDetails = user;

            populateViews(user);

            Log.i(TAG, "User name "+user.get(0).getName());

        });
        //mUserDetails = userProfileActivityViewModel.getUserDetails();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, EditUserProfileActivity.class);
                startActivity(intent);
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }//close onCreate

    private void populateViews(List<User> user){
        mUserName.setText(user.get(0).getName());
        mUserDob.setText(user.get(0).getDate_of_birth());
        mUserGender.setText(user.get(0).getGender());
        mUserPhoneNumber.setText(user.get(0).getPhone_number());

        //set user profile pic
        try {
            if (!TextUtils.isEmpty(user.get(0).getProfile_pic())) {
                Glide.with(UserProfileActivity.this)
                        .load("http://apps.incubation.billbrain.tech/tongueApp/assets/images/profile_pics/" + user.get(0).getProfile_pic())
                        .thumbnail(0.5f)
                        .into(mUserProfilePic);
                mUserProfilePic.setColorFilter(null);
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

        if (user.get(0).getDescription() != null) {
            mUserAbout.setText(user.get(0).getDescription());
        }else{
            mUserAbout.setVisibility(View.INVISIBLE);
            aboutTextView.setVisibility(View.INVISIBLE);
        }

        mUserEmail.setText(user.get(0).getEmail());
        mUserRole.setText(user.get(0).getRole());

        if (user.get(0).getRole().equals("user")){
            mBecomeTutor.setVisibility(View.VISIBLE);
        }

        //handle click on become a tutor button
        mBecomeTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, BecomeTutorActivity.class);
                startActivity(intent);

            }
        });

    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);
        session.logoutUser();
        userProfileActivityViewModel.delete();
    }



}
