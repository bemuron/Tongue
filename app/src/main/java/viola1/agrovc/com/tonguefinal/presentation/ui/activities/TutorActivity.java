package viola1.agrovc.com.tonguefinal.presentation.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.presentation.ui.fragments.TutorDetailsFragment;
import viola1.agrovc.com.tonguefinal.presentation.ui.fragments.TutorListFragment;

public class TutorActivity extends AppCompatActivity
        implements TutorListFragment.OnTutorSelectedListener, TutorDetailsFragment.OnTutorRequestedListener {
    private static final String TAG = TutorActivity.class.getSimpleName();
    private static final String TUTOR_LIST_FRAGMENT_TAG = "tutor_list_fragment";
    public static TutorActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_tutor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupActionBar();

        instance = this;

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.tutor_fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
        }

        TutorListFragment tutorListFragment = findOrCreateViewFragment();
        setupViewFragment(tutorListFragment);

    }//closing oncreate

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //set up the tutor list fragment
    private void setupViewFragment(TutorListFragment tutorListFragment) {

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(TUTOR_LIST_FRAGMENT_TAG )
                .replace(R.id.tutor_fragment_container, tutorListFragment)
                .commit();
    }

    @NonNull
    private TutorListFragment findOrCreateViewFragment() {
        //get intent from which this activity is called and the id of language selected
        //Bundle bundle = getArguments();
        int language_id = getIntent().getIntExtra("language_id", 0);
        String language_name = getIntent().getStringExtra("language_name");

        TutorListFragment tutorListFragment = (TutorListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.tutor_fragment_container);

        if (tutorListFragment == null) {
            tutorListFragment = tutorListFragment.newInstance(language_id, language_name);
        }
        return tutorListFragment;
    }

    //TutorListFragment callback
    //gets the tutor Id of the selected tutor
    //starts the tutor details fragment
    @Override
    public void tutorSelectionCallback(int tutorId){
        TutorDetailsFragment tutorDetailsFragment = new TutorDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("tutor_id", tutorId);
        Log.d(TAG,"Tutor ID from list = "+tutorId);

        tutorDetailsFragment.setArguments(args);

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.tutor_fragment_container, tutorDetailsFragment)
                .commit();
    }

    @Override
    public void tutorRequestCallback(int tutorId){
        Toast.makeText(this, "Tutor request received", Toast.LENGTH_SHORT).show();
    }
}
