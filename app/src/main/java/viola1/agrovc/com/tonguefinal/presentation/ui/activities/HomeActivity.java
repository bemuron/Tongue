package viola1.agrovc.com.tonguefinal.presentation.ui.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.app.Config;
import viola1.agrovc.com.tonguefinal.app.MyApplication;
import viola1.agrovc.com.tonguefinal.fcm.MyFirebaseMessagingService;
import viola1.agrovc.com.tonguefinal.fcm.MyNotificationManager;
import viola1.agrovc.com.tonguefinal.helper.MyPreferenceManager;
import viola1.agrovc.com.tonguefinal.helper.SessionManager;
import viola1.agrovc.com.tonguefinal.models.NotificationMessage;
import viola1.agrovc.com.tonguefinal.presentation.adapters.LanguagesAdapter;
import viola1.agrovc.com.tonguefinal.presentation.viewmodels.HomeActivityViewModel;
import viola1.agrovc.com.tonguefinal.presentation.viewmodels.HomeViewModelFactory;
import viola1.agrovc.com.tonguefinal.utilities.InjectorUtils;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LanguagesAdapter.LanguagesAdapterOnItemClickHandler {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private RecyclerView mRV;
    private LanguagesAdapter mAdapter;
    private HomeActivityViewModel mViewModel;
    private int mPosition = RecyclerView.NO_POSITION;
    private ProgressBar mLoadingIndicator;
    private SessionManager session;
    private String userRole;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //check for play services
        checkPlayServices();

        // session manager
        session = new SessionManager(getApplicationContext());
        userRole = session.getUserRole();
        userId = session.getUserId();

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        //we use this to associate the lifecycle observer(MyApplication) with this class(lifecycle owner)
        //it'll help us know when the app is in back ground or foreground
        getLifecycle().addObserver(new MyApplication());
        /**
         * Broadcast receiver calls in two scenarios
         * 1. fcm registration is completed
         * 2. when new push notification is received
         * */
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.e(TAG, "Broadcast receiver action: "+intent.getAction());
                // checking for type intent filter
                if (Config.REGISTRATION_COMPLETE.equals(intent.getAction())) {
                    // fcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    subscribeToGlobalTopic();

                    displayFirebaseRegId();

                } else if (Config.PUSH_NOTIFICATION.equals(intent.getAction())) {
                    //this broadcast is received if the app is in the foreground
                    //if the session is in progress, launch the relevant activity
                    Boolean isSessionOn = intent.getBooleanExtra("isSessionOn", false);
                    Boolean isSessionFinished = intent.getBooleanExtra("session_finished", false);
                    Boolean rating_activity = intent.getBooleanExtra("rating_activity", false);
                    if (isSessionOn) {
                        //get the user id too, to be compared with the logged in users id
                        //so that if the requester is a tutor too we can still open the right activity
                        int user_id = getIntent().getIntExtra("user_id", 0);
                        int session_meeting_id = getIntent().getIntExtra("meeting_id", 0);
                        if (userRole.equals("user") || userId == user_id) {

                            intent = new Intent(getApplicationContext(), MeetingSessionActivity.class);
                            intent.putExtra("meeting_id", session_meeting_id);
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("start_session", true);
                            // new push notification is received
                            String message = intent.getStringExtra("message");

                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            startActivity(intent);
                        }
                    }
                    //if the session is complete, load the payment activity
                    else  if (isSessionFinished){
                        int user_id = getIntent().getIntExtra("user_id", 0);
                        int session_meeting_id = getIntent().getIntExtra("meeting_id", 0);
                        float time_taken = getIntent().getFloatExtra("time_taken", 0);
                        if (userRole.equals("user") || userId == user_id) {
                            intent = new Intent(getApplicationContext(), PaymentActivity.class);
                            intent.putExtra("meeting_id", session_meeting_id);
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("time_taken", time_taken);

                        }
                    }
                    //if payment is received by the tutor, launch the rating activity
                    else  if (rating_activity){
                        int user_id = getIntent().getIntExtra("user_id", 0);
                        int session_meeting_id = getIntent().getIntExtra("meeting_id", 0);
                        if (userRole.equals("user") || userId == user_id) {
                            intent = new Intent(getApplicationContext(), RatingActivity.class);
                            intent.putExtra("meeting_id", session_meeting_id);
                            intent.putExtra("student_id", user_id);

                        }
                    }
                }
            }
        };


        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        /*if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }*/
// [END handle_data_extras]

        mRV = findViewById(R.id.recyclerview_languages);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Setup and Handover data to recyclerview
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRV.setLayoutManager(mLayoutManager);
        mRV.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        mRV.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new LanguagesAdapter(this, this);
        mRV.setAdapter(mAdapter);

        HomeViewModelFactory factory = InjectorUtils.provideMainActivityViewModelFactory(this.getApplicationContext());
        mViewModel = ViewModelProviders.of
                (this, factory).get(HomeActivityViewModel.class);

        mViewModel.getAllLanguages().observe(this, languages -> {
            mAdapter.swapForecast(languages);

            if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
            mRV.smoothScrollToPosition(mPosition);

            Log.i(TAG, "Languages list size "+languages.size());

            // Show the weather list or the loading screen based on whether the forecast data exists
            // and is loaded
            if (languages != null && languages.size() != 0) showCategoryDataView();
            else showLoading();

        });

        /**
         * Always check for google play services availability before
         * proceeding further with FCM
         * */
        /*if (checkPlayServices()) {
            registerFCM(session.getUserId());
            //fetchChatRooms();
        }*/

        /*MyApplication myApp = (MyApplication)this.getApplication();
        if(myApp.myApplicationStatus){
            // register FCM registration complete receiver
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Config.REGISTRATION_COMPLETE));

            // register new push message receiver
            // by doing this, the activity will be notified each time a new message arrives
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Config.PUSH_NOTIFICATION));

        }else{
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        }*/
        isAppInBackground();
    }//close onCreate

    //this method gets the fcm device token from shared prefs
    private void displayFirebaseRegId(){
        String token = MyApplication.getInstance().getPrefManager().getDeviceToken();
        //if token is not null
        if (token != null) {
            //displaying the token
            Log.i(TAG, token);
            //Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
        } else {
            //if token is null that means something wrong
            Toast.makeText(this, "Token not generated", Toast.LENGTH_SHORT).show();
        }
    }

    private void isAppInBackground(){
        if(MyApplication.getInstance().myApplicationStatus){
            Log.e(TAG, "App resumed");
            //check for play services
            checkPlayServices();

            /*mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    // checking for type intent filter
                    if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                        // fcm successfully registered
                        // now subscribe to `global` topic to receive app wide notifications
                        subscribeToGlobalTopic();

                        displayFirebaseRegId();

                    } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                        // new push notification is received

                        String message = intent.getStringExtra("message");

                        Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    }
                }
            };*/

            // register FCM registration complete receiver
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Config.REGISTRATION_COMPLETE));

            // register new push message receiver
            // by doing this, the activity will be notified each time a new message arrives
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Config.PUSH_NOTIFICATION));

            // clear the notification area when the app is opened
            MyNotificationManager.clearNotifications(getApplicationContext());
        }else {
            Log.e(TAG, "App paused");
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        }
    }

    /*@Override
    protected void onResume() {
        super.onResume();

        // register FCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        MyNotificationManager.clearNotifications(getApplicationContext());
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }*/

    // subscribing to global topic
    private void subscribeToGlobalTopic() {
        Log.e(TAG, "Subscribing to global topic");
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, msg);
                        //Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
// [END subscribe_topics]
    }

    /**
     * This method will make the View for the languages data visible and hide the error message and
     * loading indicator.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't need to check whether
     * each view is currently visible or invisible.
     */
    private void showCategoryDataView() {
        // First, hide the loading indicator
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        // Finally, make sure the weather data is visible
        mRV.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the loading indicator visible and hide the languages View and error
     * message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't need to check whether
     * each view is currently visible or invisible.
     */
    private void showLoading() {
        // Then, hide the weather data
        mRV.setVisibility(View.INVISIBLE);
        // Finally, show the loading indicator
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        // Get Search item from action bar and Get Search service
        /*MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) HomeActivity.this.getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(HomeActivity.this.getComponentName()));
            searchView.setIconified(false);
        }*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        if (id == R.id.action_search_link){
            Intent intent = new Intent(this, SearchLanguagesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //handle clicks on the language grid items
    @Override
    public void onItemClick(int language_id, String language_name) {
        //start Tutor Activity passing in the id of the language clicked
        Intent intent = new Intent(HomeActivity.this, TutorActivity.class);
        intent.putExtra("language_id", language_id);
        intent.putExtra("language_name", language_name);
        startActivity(intent);
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);
        MyApplication.getInstance().logout();
        session.logoutUser();
        mViewModel.delete();
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        MyApplication myApp = (MyApplication)this.getApplication();
        if(myApp.myApplicationStatus){

        }

        // register FCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }*/

    // starting the service to register with FCM
    /*private void registerFCM(int userId) {
        Intent intent = new Intent(this, MyFirebaseMessagingService.class);
        intent.putExtra("key", "register");
        intent.putExtra("userId", userId);
        startService(intent);
    }*/

    private void checkPlayServices() {
        Log.e(TAG, "Checking for GPS");
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                finish();
            }
        }

        /*Toast.makeText(this,"isGPSAvailable " + resultCode,Toast.LENGTH_SHORT).show();
        switch (resultCode)
        {
            case ConnectionResult.API_UNAVAILABLE:
                //API is not available
                break;
            case ConnectionResult.NETWORK_ERROR:
                //Network error while connection
                break;
            case ConnectionResult.RESTRICTED_PROFILE:
                //Profile is restricted by google so can not be used for play services
                break;
            case ConnectionResult.SERVICE_MISSING:
                //service is missing
                break;
            case ConnectionResult.SIGN_IN_REQUIRED:
                //service available but user not signed in
                break;
                case ConnectionResult.SERVICE_INVALID:
            //  The version of the Google Play services installed on this device is not authentic
            break;
            case ConnectionResult.SUCCESS:
                break;
        }*/
    }

    /**
     * chooses a random color from array.xml
     */
    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", TutorActivity.instance.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    /**
     * Email client intent to send support mail
     * Appends the necessary device information to email body
     * useful when providing support
     */
    private static void sendFeedback(Context context) {
        String body = null;
        try {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
        } catch (PackageManager.NameNotFoundException e) {
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"tongueapp@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Query from Tongue app user");
        intent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.pending_requests) {
            // Handle user pending requests
            Intent intent = new Intent(HomeActivity.this, TutorshipRequestsActivity.class);
            intent.putExtra("fragment_id", 1);
            startActivity(intent);
        } else if (id == R.id.confirmed_requests) {
            //confirmed requests for the user
            Intent intent = new Intent(HomeActivity.this, TutorshipRequestsActivity.class);
            intent.putExtra("fragment_id", 2);
            startActivity(intent);

        } else if (id == R.id.tutor_pending_requests) {
            //tutor pending requests
            Intent intent = new Intent(HomeActivity.this, TutorshipRequestsActivity.class);
            intent.putExtra("fragment_id", 3);
            startActivity(intent);

        } else if (id == R.id.tutor_confirmed_requests) {
            //tutor confirmed requests
            Intent intent = new Intent(HomeActivity.this, TutorshipRequestsActivity.class);
            intent.putExtra("fragment_id", 4);
            startActivity(intent);

        } else if (id == R.id.nav_user_profile) {
            //user profile
            Intent intent = new Intent(HomeActivity.this, UserProfileActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_about_tongue) {
            //about tongue
            Intent intent = new Intent(HomeActivity.this, AboutTongueActivity.class);
            startActivity(intent);

        } /*else if (id == R.id.nav_share) {
            //share app

        }*/ else if (id == R.id.nav_feedback) {
            //feedback
            sendFeedback(this);

        } else if (id == R.id.nav_user_logout) {
            //user logout
            logoutUser();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
