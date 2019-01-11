package viola1.agrovc.com.tonguefinal.presentation.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.data.network.Result;
import viola1.agrovc.com.tonguefinal.data.network.api.APIService;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.LocalRetrofitApi;
import viola1.agrovc.com.tonguefinal.helper.SessionManager;
import viola1.agrovc.com.tonguefinal.presentation.ui.activities.PaymentActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PaymentSummaryFragment.OnPaymentSummaryListener} interface
 * to handle interaction events.
 * Use the {@link PaymentSummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentSummaryFragment extends Fragment {
    private static final String TAG = PaymentSummaryFragment.class.getSimpleName();
    private static final int TUTORSHIP_HOURLY_FEE = 25000;
    private static final String MEETING_ID = "meeting_id";
    private static final String TIME_TAKEN = "time_taken";
    private static final String STUDENT_ID = "student_id";
    private TextView sessionDuration, sessionTotalCost, sessionTotalCostLarge, paymentIstruction;
    private Button confirmPaymentButton;
    private SessionManager session;
    private String userRole;
    private int meeting_id, student_id, total_cost;
    private float time_taken;

    private OnPaymentSummaryListener mListener;

    public PaymentSummaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param meeting_id id of the meeting.
     * @return A new instance of fragment PaymentSummaryFragment.
     */
    // Start fragment passing required arguments
    public static PaymentSummaryFragment newInstance(int meeting_id, int student_id, float time_taken) {
        PaymentSummaryFragment fragment = new PaymentSummaryFragment();
        Bundle args = new Bundle();
        args.putInt(MEETING_ID, meeting_id);
        args.putInt(STUDENT_ID, student_id);
        args.putFloat(TIME_TAKEN, time_taken);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            meeting_id = getArguments().getInt(MEETING_ID);
            student_id = getArguments().getInt(STUDENT_ID);
            time_taken = getArguments().getFloat(TIME_TAKEN);
        }

        //set the name of this fragment in the toolbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Payment Summary");

        // session manager
        session = new SessionManager(getActivity());
        userRole = session.getUserRole();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_payment_summary, container, false);

        getAllWidgets(view);

        // Inflate the layout for this fragment
        return view;
    }

    private void getAllWidgets(View view){
        sessionDuration = view.findViewById(R.id.sessionDurationValue);
        sessionTotalCost = view.findViewById(R.id.sessionTotalCostValue);
        sessionTotalCostLarge = view.findViewById(R.id.sessionCostLargeTv);
        paymentIstruction = view.findViewById(R.id.paymentInstructionTv);
        confirmPaymentButton = view.findViewById(R.id.confirmPaymentButton);
        //only show this button if the role of the user is tutor
        if (userRole.equals("user")) {
            confirmPaymentButton.setVisibility(View.GONE);
            paymentIstruction.setText("Please pay the amount below to your tutor");
        }else{
            confirmPaymentButton.setVisibility(View.VISIBLE);
            paymentIstruction.setText("Amount to be received from student");
        }

        //calculate the cost of the session
        calculatePayment(time_taken);

        handleConfirmPayment();

        //format the meeting duration to be displayed
        int seconds = (int) (time_taken / 1000);
        int minutes = seconds / 60;
        int hours = minutes / 60;
        seconds = seconds % 60;
        sessionDuration.setText("" + hours + "hrs :"
                + minutes + "mins :"
                + String.format("%02d", seconds) +"secs");
    }

    //method to handle click on confirm payment button by tutor
    //just take us back to the parent activity which then launches the
    //rate user/tutor activity
    private void handleConfirmPayment(){
        confirmPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    //retrofit call to update db with cost of session
                    paymentReceived(meeting_id, student_id, total_cost);

                    mListener.onPaymentSummaryInteraction(meeting_id, student_id);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPaymentSummaryListener) {
            mListener = (OnPaymentSummaryListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPaymentSummaryInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPaymentSummaryListener {
        // send meeting id to parent activity
        void onPaymentSummaryInteraction(int meeting_id, int student_id);
    }

    //this meethod calculates the amount to be paid to the tutor by multiplying
    //the number of hours taken by the hourly fee
    private void calculatePayment(float time_taken){
        //get the hours taken from the total time
        //the total time here is in milliseconds
        float sessionSecs = (time_taken / 1000);
        float sessionMins = (sessionSecs/60);
        float sessionHrs = (sessionMins/60);
        float secs =  (sessionSecs/120);
        float mins = (sessionMins/60);
        Log.e(TAG, "session hours = "+ sessionHrs);
        Log.e(TAG, "session mins = "+ mins);
        Log.e(TAG, "session secs = "+ secs);

        //calculate the price
        total_cost = (int) (sessionHrs * TUTORSHIP_HOURLY_FEE);

        sessionTotalCost.setText("UGX."+total_cost);
        sessionTotalCostLarge.setText("UGX."+total_cost);

    }

    //retrofit call to add session cost to db table
    private void paymentReceived(final int meeting_id, int notify_user_id, int price) {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        //this method will be running on UI thread
        pdLoading.setMessage("\tWorking...");
        pdLoading.setCancelable(false);
        pdLoading.show();

        //Defining retrofit api service*/
        //APIService service = retrofit.create(APIService.class);
        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<Result> call = service.sessionPaymentReceived(meeting_id, notify_user_id, price);

        //calling the com.emtech.retrofitexample.api
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                pdLoading.dismiss();

                //ResBeanSignup resBean = response.body();
                //String response_status = Encryption.decrypt(resBean.getResponse_status());

                if (!response.body().getError()) {
                    //Log.d(LOG_TAG, EnumAppMessages.REGISTER_SUCCESS_TITLE.getValue());
                    Log.d(TAG, response.body().getMessage());

                    //save to prefs that the payment is received
                    session.spPaymentReceived(meeting_id);

                    //show toast to inform user
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                pdLoading.dismiss();
                //print out any error we may get
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, t.getMessage());
            }
        });

    }
}
