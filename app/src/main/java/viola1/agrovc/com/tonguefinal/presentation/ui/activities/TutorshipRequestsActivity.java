package viola1.agrovc.com.tonguefinal.presentation.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.helper.SessionManager;
import viola1.agrovc.com.tonguefinal.presentation.ui.fragments.ConfirmedRequestDetailFragment;
import viola1.agrovc.com.tonguefinal.presentation.ui.fragments.PendingRequestDetailFragment;
import viola1.agrovc.com.tonguefinal.presentation.ui.fragments.TutorConfirmedRequestsListFragment;
import viola1.agrovc.com.tonguefinal.presentation.ui.fragments.TutorPendingRequestsListFragment;
import viola1.agrovc.com.tonguefinal.presentation.ui.fragments.UpdateMeetingScheduleFragment;
import viola1.agrovc.com.tonguefinal.presentation.ui.fragments.UserConfirmedRequestDetailFragment;
import viola1.agrovc.com.tonguefinal.presentation.ui.fragments.UserConfirmedRequestsListFragment;
import viola1.agrovc.com.tonguefinal.presentation.ui.fragments.UserPendingRequestDetailFragment;
import viola1.agrovc.com.tonguefinal.presentation.ui.fragments.UserPendingRequestsListFragment;

/*
* this activity hosts the fragments that show the user/tutor pending request lists and user/tutor
* pending request details
*
* It determines which details to show by using the id passed into the fragment when created.
* the ids passed are: 1 - user pending requests, 2 - user confirmed requests, 3 - tutor pending requests
* 4 - tutor confirmed requests
* */

public class TutorshipRequestsActivity extends AppCompatActivity implements
        TutorPendingRequestsListFragment.OnPendingRequestListInteractionListener,
        TutorConfirmedRequestsListFragment.OnPendingRequestListInteractionListener,
        PendingRequestDetailFragment.OnPendingRequestDetailInteractionListener,
        UpdateMeetingScheduleFragment.OnFragmentInteractionListener,
        UserPendingRequestsListFragment.OnUserPendingRequestListInteractionListener,
        UserConfirmedRequestsListFragment.OnConfirmedRequestListInteractionListener,
        ConfirmedRequestDetailFragment.OnConfirmedRequestDetailInteractionListener,
        UserConfirmedRequestDetailFragment.OnUserConfirmedRequestDetailListener,
        UserPendingRequestDetailFragment.OnUserPendingRequestDetailInteractionListener{

    private static final String TAG = TutorshipRequestsActivity.class.getSimpleName();
    public static TutorshipRequestsActivity instance;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorship_requests);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        instance = this;

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.tutorship_requests_fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
        }

        //get intent from which this activity is called
        //the fragment id is used to determine which fragment we should launch
        int fragment_id = getIntent().getIntExtra("fragment_id", 0);

        //show the appropriate fragment
        showRightFragment(fragment_id);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    //method to determine the fragment to show
    private void showRightFragment(int actionId){
        int meeting_id;
        String requester_name, tutor_name;
        switch (actionId){
            case 1:
                setUpUserPendingRequestsFragment();
                break;
            case 2:
                setUpUserConfirmedRequestsFragment();
                break;
            case 3:
                setUpTutorPendingRequestsFragment();
                break;
            case 4:
                setUpTutorConfirmedRequestsFragment();
                break;
            case 5:
                meeting_id = getIntent().getIntExtra("meeting_id", 0);
                requester_name = getIntent().getStringExtra("requester_name");
                setUpTutorPendingRequestDetailsFragmentForNotification(meeting_id, requester_name);
                break;
            case 6:
                meeting_id = getIntent().getIntExtra("meeting_id", 0);
                tutor_name = getIntent().getStringExtra("tutor_name");
                setUpUserPendingRequestDetailsFragmentForNotification(meeting_id, tutor_name);
                break;
            case 7:
                meeting_id = getIntent().getIntExtra("meeting_id", 0);
                requester_name = getIntent().getStringExtra("requester_name");
                setUpTutorConfirmedRequestDetailsFragmentForNotification(meeting_id, requester_name);
                break;
            case 8:
                meeting_id = getIntent().getIntExtra("meeting_id", 0);
                tutor_name = getIntent().getStringExtra("tutor_name");
                setUpUserConfirmedRequestDetailsFragmentForNotification(meeting_id, tutor_name);
                break;
        }
        /*if (fragId == 1){
            setUpTutorPendingRequestsFragment();
        }else {
            setUpTutorConfirmedRequestsFragment();
        }*/

    }

    //set up the tutor pending requests fragment
    private void setUpTutorPendingRequestsFragment(){
        TutorPendingRequestsListFragment tutorPendingRequestsListFragment = new TutorPendingRequestsListFragment();
        /*Bundle args = new Bundle();
        args.putInt("actionId", actionId);
        tutorPendingRequestsListFragment.setArguments(args);*/

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                //.addToBackStack(null)
                .replace(R.id.tutorship_requests_fragment_container, tutorPendingRequestsListFragment)
                .commit();
    }

    //set up the user pending requests fragment
    private void setUpUserPendingRequestsFragment(){
        UserPendingRequestsListFragment userPendingRequestsListFragment = new UserPendingRequestsListFragment();
        /*Bundle args = new Bundle();
        args.putInt("actionId", actionId);
        tutorPendingRequestsListFragment.setArguments(args);*/

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                //.addToBackStack(null)
                .replace(R.id.tutorship_requests_fragment_container, userPendingRequestsListFragment)
                .commit();
    }

    //set up the tutor confirmed requests fragment
    private void setUpTutorConfirmedRequestsFragment(){
        TutorConfirmedRequestsListFragment tutorConfirmedRequestsListFragment = new TutorConfirmedRequestsListFragment();
        /*Bundle args = new Bundle();
        args.putInt("actionId", actionId);
        tutorPendingRequestsListFragment.setArguments(args);*/

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                //.addToBackStack(null)
                .replace(R.id.tutorship_requests_fragment_container, tutorConfirmedRequestsListFragment)
                .commit();

    }

    //set up the user confirmed requests fragment
    private void setUpUserConfirmedRequestsFragment(){
        UserConfirmedRequestsListFragment userConfirmedRequestsListFragment = new UserConfirmedRequestsListFragment();
        /*Bundle args = new Bundle();
        args.putInt("actionId", actionId);
        tutorPendingRequestsListFragment.setArguments(args);*/

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                //.addToBackStack(null)
                .replace(R.id.tutorship_requests_fragment_container, userConfirmedRequestsListFragment)
                .commit();
    }

    @Override
    public void onListFragmentInteraction(int meetingId, String requesterName){
        ConfirmedRequestDetailFragment confirmedRequestDetailFragment = new ConfirmedRequestDetailFragment();

        Bundle args = new Bundle();
        args.putInt("meeting_id", meetingId);
        args.putString("requester_name", requesterName);

        confirmedRequestDetailFragment.setArguments(args);

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                //.addToBackStack(null)
                .replace(R.id.tutorship_requests_fragment_container, confirmedRequestDetailFragment)
                .commit();
    }

    @Override
    public void onPendingRequestInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(String meetingDate, String meetingTime) {

    }

    @Override
    public void onConfirmedListFragmentInteraction(int meetingId, String tutorName) {
        UserConfirmedRequestDetailFragment userConfirmedRequestDetailFragment = new UserConfirmedRequestDetailFragment();

        Bundle args = new Bundle();
        args.putInt("meeting_id", meetingId);
        args.putString("tutor_name", tutorName);

        userConfirmedRequestDetailFragment.setArguments(args);

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                //.addToBackStack(null)
                .replace(R.id.tutorship_requests_fragment_container, userConfirmedRequestDetailFragment)
                .commit();
    }

    @Override
    public void onConfirmedRequestDetailInteraction(int meeting_id) {
        Intent intent = new Intent(TutorshipRequestsActivity.this, MeetingSessionActivity.class);
        intent.putExtra("meeting_id", meeting_id);
        startActivity(intent);
    }

    @Override
    public void onUserConfirmedRequestDetailInteraction(Uri uri) {

    }

    @Override
    public void onUserPendingRequestInteraction(Uri uri) {

    }

    @Override
    public void onUserPendingListFragmentInteraction(int meetingId, String tutorName) {
        UserPendingRequestDetailFragment userPendingRequestDetailFragment = new UserPendingRequestDetailFragment();

        Bundle args = new Bundle();
        args.putInt("meeting_id", meetingId);
        args.putString("tutor_name", tutorName);

        userPendingRequestDetailFragment.setArguments(args);

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                //.addToBackStack(null)
                .replace(R.id.tutorship_requests_fragment_container, userPendingRequestDetailFragment)
                .commit();
    }

    //this method launches the the tutor pending request details fragment from a notification received
    private void setUpTutorPendingRequestDetailsFragmentForNotification(int meetingId, String requesterName){
        PendingRequestDetailFragment pendingRequestDetailFragment = new PendingRequestDetailFragment();

        Bundle args = new Bundle();
        args.putInt("meeting_id", meetingId);
        args.putString("requester_name", requesterName);

        pendingRequestDetailFragment.setArguments(args);

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                //.addToBackStack(null)
                .replace(R.id.tutorship_requests_fragment_container, pendingRequestDetailFragment)
                .commit();
    }

    //this method launches the the user pending request details fragment from a notification received
    private void setUpUserPendingRequestDetailsFragmentForNotification(int meetingId, String tutorName){
        UserPendingRequestDetailFragment userPendingRequestDetailFragment = new UserPendingRequestDetailFragment();

        Bundle args = new Bundle();
        args.putInt("meeting_id", meetingId);
        args.putString("tutor_name", tutorName);

        userPendingRequestDetailFragment.setArguments(args);

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                //.addToBackStack(null)
                .replace(R.id.tutorship_requests_fragment_container, userPendingRequestDetailFragment)
                .commit();
    }

    //this method launches the the tutor confirmed request details fragment from a notification received
    private void setUpTutorConfirmedRequestDetailsFragmentForNotification(int meetingId, String requesterName){
        ConfirmedRequestDetailFragment confirmedRequestDetailFragment = new ConfirmedRequestDetailFragment();

        Bundle args = new Bundle();
        args.putInt("meeting_id", meetingId);
        args.putString("requester_name", requesterName);

        confirmedRequestDetailFragment.setArguments(args);

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                //.addToBackStack(null)
                .replace(R.id.tutorship_requests_fragment_container, confirmedRequestDetailFragment)
                .commit();
    }

    //this method launches the user confirmed request details from a notification received
    private void setUpUserConfirmedRequestDetailsFragmentForNotification(int meetingId, String tutorName){
        UserConfirmedRequestDetailFragment userConfirmedRequestDetailFragment = new UserConfirmedRequestDetailFragment();

        Bundle args = new Bundle();
        args.putInt("meeting_id", meetingId);
        args.putString("tutor_name", tutorName);

        userConfirmedRequestDetailFragment.setArguments(args);

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                //.addToBackStack(null)
                .replace(R.id.tutorship_requests_fragment_container, userConfirmedRequestDetailFragment)
                .commit();
    }

    @Override
    public void onTutorPendingRequestListInteraction(int meetingId, String tutorRequesterName) {
        PendingRequestDetailFragment pendingRequestDetailFragment =
                PendingRequestDetailFragment.newInstance(meetingId, tutorRequesterName);

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                //.addToBackStack(null)
                .replace(R.id.tutorship_requests_fragment_container, pendingRequestDetailFragment)
                .commit();
    }
}
