package viola1.agrovc.com.tonguefinal.presentation.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.helper.SessionManager;
import viola1.agrovc.com.tonguefinal.presentation.ui.fragments.SessionInProgressFragment;
import viola1.agrovc.com.tonguefinal.presentation.ui.fragments.StartSessionFragment;

public class MeetingSessionActivity extends AppCompatActivity implements
        StartSessionFragment.OnStartSessionFragmentListener, SessionInProgressFragment.SessionInProgressFragmentInteractionListener{
    private SessionManager session;
    private String userRole;
    private int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_session);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // session manager
        session = new SessionManager(getApplicationContext());
        userRole = session.getUserRole();
        userId = session.getUserId();

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.meeting_session_fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
        }

        int user_id = getIntent().getIntExtra("user_id", 0);
        if (userRole.equals("user") || userId == user_id) {
            //get intent from which this activity is called and the id of the meeting
            //this mainly targets a user/student
            //this is coming from the fcm notification received
            int session_meeting_id = getIntent().getIntExtra("meeting_id", 0);
            boolean start_session = getIntent().getBooleanExtra("start_session", false);
            if (start_session) {

                //save the session in the shared prefs too
                session.spMeetingInProgress(session_meeting_id, user_id);

                SessionInProgressFragment sessionInProgressFragment =
                        SessionInProgressFragment.newInstance(session_meeting_id, user_id);

                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        //.addToBackStack(null)
                        .replace(R.id.meeting_session_fragment_container, sessionInProgressFragment)
                        .commit();
            }
        }else {
            //launch the start session fragment
            setUpStartSessionFragment();
        }

    }//close onCreate

    //set up the start session fragment to be displayed to the user
    private void setUpStartSessionFragment(){
        //get intent from which this activity is called and the id of the meeting

        int meeting_id = getIntent().getIntExtra("meeting_id", 0);
        StartSessionFragment startSessionFragment = StartSessionFragment.newInstance(meeting_id);

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.meeting_session_fragment_container, startSessionFragment)
                .commit();

    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);
        session.logoutUser();
        //mViewModel.delete();
    }

    //callback method from start session fragment
    //starts the session in progress fragment
    @Override
    public void onStartSessionInteraction(int meetingId) {
            //just start the session in progress fragment
            SessionInProgressFragment sessionInProgressFragment = SessionInProgressFragment.newInstance(meetingId, 0);

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    //.addToBackStack(null)
                    .replace(R.id.meeting_session_fragment_container, sessionInProgressFragment)
                    .commit();
    }

    //callback from session in progress fragment
    //received when the finish session button has been pressed
    //starts the payment activity passing the meeting_id and duration of session(in milliseconds) as arguments
    @Override
    public void onSessionInProgressInteraction(int meeting_id, int tutor_id, int student_id, float time_taken) {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("meeting_id", meeting_id);
        intent.putExtra("tutor_id", tutor_id);
        intent.putExtra("student_id", student_id);
        intent.putExtra("time_taken", time_taken);
        startActivity(intent);
    }
}
