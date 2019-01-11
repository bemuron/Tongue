package viola1.agrovc.com.tonguefinal.presentation.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.helper.SessionManager;
import viola1.agrovc.com.tonguefinal.presentation.ui.fragments.PaymentSummaryFragment;

public class PaymentActivity extends AppCompatActivity implements PaymentSummaryFragment.OnPaymentSummaryListener {
    private static final String TAG = PaymentActivity.class.getSimpleName();
    private SessionManager session;
    private String userRole;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // session manager
        session = new SessionManager(getApplicationContext());
        userRole = session.getUserRole();
        userId = session.getUserId();

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.payment_fragment_container) != null) {

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
            boolean session_finished = getIntent().getBooleanExtra("session_finished", false);
            if (session_finished) {
                float time_taken = getIntent().getFloatExtra("time_taken", 0);
                PaymentSummaryFragment paymentSummaryFragment =
                        PaymentSummaryFragment.newInstance(session_meeting_id,user_id, time_taken);

                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.payment_fragment_container, paymentSummaryFragment)
                        .commit();
            }
        }else {
            //launch the payment summary fragment
            setUpPaymentSummaryFragment();
        }

    }// close oncreate

    //set up the payment summary fragment
    private void setUpPaymentSummaryFragment(){
        int meeting_id = getIntent().getIntExtra("meeting_id", 0);
        int tutor_id = getIntent().getIntExtra("tutor_id", 0);
        int student_id = getIntent().getIntExtra("student_id", 0);
        float time_taken = getIntent().getFloatExtra("time_taken", 0);
        PaymentSummaryFragment paymentSummaryFragment = PaymentSummaryFragment.newInstance(meeting_id, student_id, time_taken);

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.payment_fragment_container, paymentSummaryFragment)
                .commit();
    }

    @Override
    public void onPaymentSummaryInteraction(int meeting_id, int student_id) {
        Intent intent = new Intent(PaymentActivity.this, RatingActivity.class);
        intent.putExtra("meeting_id", meeting_id);
        intent.putExtra("student_id", student_id);
        startActivity(intent);
    }
}
