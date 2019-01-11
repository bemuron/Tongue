package viola1.agrovc.com.tonguefinal.presentation.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.helper.SessionManager;
import viola1.agrovc.com.tonguefinal.presentation.ui.fragments.RateTutorFragment;
import viola1.agrovc.com.tonguefinal.presentation.ui.fragments.RateUserFragment;

public class RatingActivity extends AppCompatActivity implements
        RateTutorFragment.OnRateTutorInteractionListener, RateUserFragment.OnRateUserListener {
    private static final String TAG = RatingActivity.class.getSimpleName();
    private SessionManager session;
    private String userRole;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // session manager
        session = new SessionManager(getApplicationContext());
        userRole = session.getUserRole();
        userId = session.getUserId();

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.rating_fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
        }
        //show the right fragment based on the user role
        setUpRespectiveFragment(userRole);

    }//close onCreate

    //show the respective fragment based on the role of the user
    private void setUpRespectiveFragment(String role){

        //get intent from which this activity is called and the id of the meeting
        int meeting_id = getIntent().getIntExtra("meeting_id", 0);
        //get the id of the student
        int student_id = getIntent().getIntExtra("student_id", 0);

        if (role.equals("user") || userId == student_id) {
            //if the current user of the app is a user/student then the userId passed here is for the user
            RateTutorFragment rateTutorFragment = RateTutorFragment.newInstance(meeting_id, userId);

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.rating_fragment_container, rateTutorFragment)
                    .commit();

        }else if (role.equals("tutor")){
            //if the current user of the app is a tutor then the userId passed here is for the tutor
            RateUserFragment rateUserFragment = RateUserFragment.newInstance(meeting_id, userId);

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.rating_fragment_container, rateUserFragment)
                    .commit();
        }
    }

    //callback from rate tutor fragment
    //when this is called, the user has successfully rated the tutor
    //start home activity clearing the back stack
    @Override
    public void onRateTutorInteraction(int meeting_id) {
        Intent intent = new Intent(RatingActivity.this, HomeActivity.class );
        // Closing all the Activities
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }

    //callback from rate user fragment
    //when this is called, the tutor has successfully rated the user
    //start home activity clearing the back stack
    @Override
    public void onRateUserFragmentInteraction(int meeting_id) {
        Intent intent = new Intent(RatingActivity.this, HomeActivity.class );
        // Closing all the Activities
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }
}
