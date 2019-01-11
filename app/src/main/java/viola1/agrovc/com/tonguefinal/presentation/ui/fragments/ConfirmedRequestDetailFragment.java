package viola1.agrovc.com.tonguefinal.presentation.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.constants.AppNums;
import viola1.agrovc.com.tonguefinal.data.network.ConfirmedRequestResult;
import viola1.agrovc.com.tonguefinal.data.network.PendingRequest;
import viola1.agrovc.com.tonguefinal.data.network.Result;
import viola1.agrovc.com.tonguefinal.data.network.api.APIService;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.LocalRetrofitApi;
import viola1.agrovc.com.tonguefinal.helper.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnConfirmedRequestDetailInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConfirmedRequestDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmedRequestDetailFragment extends Fragment {
    private static final String TAG = ConfirmedRequestDetailFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MEETING_NAME = "meeting_id";
    private static final String REQUESTER_NAME = "requester_name";
    private int mMeetingId, modified_by, user_id, requester_id;
    private String requesterName;
    private SessionManager sessionManager;
    private TextView prUserName, prUserAge, prGender, prDesc, prLanguagesSpoken,
            prMeetingTime, prMeetingDate, prMeetingLocation;
    private ProgressDialog pDialog;
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "kk:mm";
    private CircleImageView prUserProfileImage;
    private RatingBar userRatingBar;
    private PendingRequest pendingRequest;
    private Button confirmRequestButton, rejectRequestButton, editScheduleButton, startSessionButton;

    private OnConfirmedRequestDetailInteractionListener mListener;

    public ConfirmedRequestDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param meetingId meeting_schedule id.
     * @return A new instance of fragment PendingRequestDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfirmedRequestDetailFragment newInstance(int meetingId) {
        ConfirmedRequestDetailFragment fragment = new ConfirmedRequestDetailFragment();
        Bundle args = new Bundle();
        args.putInt(MEETING_NAME, meetingId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMeetingId = getArguments().getInt(MEETING_NAME);
            requesterName = getArguments().getString(REQUESTER_NAME);
        }

        // Session manager
        sessionManager = new SessionManager(getActivity());

        //get user id from shared prefs
        user_id = sessionManager.getUserId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_request_detail, container,false);

        getAllWidgets(view);

        //first check if we have an internet connection
        if (isNetworkAvailable()) {
            if (mMeetingId > 0) {
                //retrofit call to get the selected pending request details
                getConfirmedRequestDetails(mMeetingId);

            }
        }

        return view;
    }

    /*// TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onConfirmedRequestDetailInteraction(uri);
        }
    }*/

    //initialise the view widgets we have
    public void getAllWidgets(View view) {
        prUserName = view.findViewById(R.id.prdNameTextView);
        prUserAge = view.findViewById(R.id.prdAgeTextView);
        prGender = view.findViewById(R.id.prdGenderTextView);
        prDesc = view.findViewById(R.id.prdDescTextView);
        prLanguagesSpoken = view.findViewById(R.id.prdLanguagesSpokenTextView);
        prMeetingTime = view.findViewById(R.id.prdScheduledMeetingTime);
        prMeetingDate = view.findViewById(R.id.prdScheduledMeetingDate);
        prMeetingLocation = view.findViewById(R.id.prdMeetingLocation);
        userRatingBar = view.findViewById(R.id.prdRatingBar);
        prUserProfileImage = view.findViewById(R.id.prdProfilePicImageView);
        editScheduleButton = view.findViewById(R.id.prdEditMeetingscheduleButton);
        confirmRequestButton = view.findViewById(R.id.prdConfirmButton);
        rejectRequestButton = view.findViewById(R.id.prdRejectButton);
        startSessionButton = view.findViewById(R.id.startSessionButton);
        //make the button visible
        startSessionButton.setVisibility(View.VISIBLE);
        prUserProfileImage = view.findViewById(R.id.prdProfilePicImageView);

        //make edit and accept buttons invisible
        editScheduleButton.setVisibility(View.INVISIBLE);
        confirmRequestButton.setVisibility(View.INVISIBLE);

        //methods to handle clicks on the buttons
        showUpdateMeetingDialog(mMeetingId, requesterName, requester_id);
        confirmButtonClick(mMeetingId, requesterName, user_id, requester_id);
        rejectButtonClick(mMeetingId, requesterName, user_id);
        startSessionClick();

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnConfirmedRequestDetailInteractionListener) {
            mListener = (OnConfirmedRequestDetailInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnConfirmedRequestDetailInteractionListener");
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
    public interface OnConfirmedRequestDetailInteractionListener {
        // return the meeting id to the parent activity if today is the meeting date and time
        void onConfirmedRequestDetailInteraction(int meeting_id);
    }

    //retrofit call to get the details of the confirmed request user
    private void getConfirmedRequestDetails(int mMeetingId){
        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        //this method will be running on UI thread
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();


        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<ConfirmedRequestResult> call = service.getConfirmedRequestDetails(mMeetingId);

        //calling the com.emtech.retrofitexample.api
        call.enqueue(new Callback<ConfirmedRequestResult>() {
            @Override
            public void onResponse(Call<ConfirmedRequestResult> call, Response<ConfirmedRequestResult> response) {
                if(response.code() == AppNums.STATUS_COD_SUCCESS) {
                    pdLoading.dismiss();

                    if (response.body().getConfirmedRequestDetails().getMeeting_id() > 0) {
                        PendingRequest pendingRequest = new PendingRequest();
                        pendingRequest.setMeeting_id(response.body().getConfirmedRequestDetails().getMeeting_id());
                        pendingRequest.setUser_id(response.body().getConfirmedRequestDetails().getUser_id());
                        pendingRequest.setTutor_id(response.body().getConfirmedRequestDetails().getTutor_id());
                        pendingRequest.setLanguage_name(response.body().getConfirmedRequestDetails().getLanguage_name());
                        pendingRequest.setMeeting_date(response.body().getConfirmedRequestDetails().getMeeting_date());
                        pendingRequest.setMeeting_time(response.body().getConfirmedRequestDetails().getMeeting_time());
                        pendingRequest.setMeeting_location(response.body().getConfirmedRequestDetails().getMeeting_location());
                        pendingRequest.setCreated_on(response.body().getConfirmedRequestDetails().getCreated_on());
                        pendingRequest.setLast_modified_by(response.body().getConfirmedRequestDetails().getLast_modified_by());
                        pendingRequest.setName(response.body().getConfirmedRequestDetails().getName());
                        pendingRequest.setGender(response.body().getConfirmedRequestDetails().getGender());
                        pendingRequest.setDate_of_birth(response.body().getConfirmedRequestDetails().getDate_of_birth());
                        pendingRequest.setProfile_pic(response.body().getConfirmedRequestDetails().getProfile_pic());

                        modified_by = response.body().getConfirmedRequestDetails().getLast_modified_by();

                        //check who last modified the schedule and let them know
                        checkLastModifiedBy(modified_by);

                        //get id of user to be notified
                        requester_id = pendingRequest.getUser_id();

                        //compare today's date with the meeting schedule date to see if the session to start
                        checkForSessionStart(pendingRequest.getMeeting_date(), pendingRequest.getMeeting_time());

                        if (!TextUtils.isEmpty(pendingRequest.getProfile_pic())) {
                            Glide.with(getActivity()).load("http://apps.incubation.billbrain.tech/tongueApp/assets/images/profile_pics/"+pendingRequest.getProfile_pic())
                                    .thumbnail(0.5f)
                                    .into(prUserProfileImage);
                            prUserProfileImage.setColorFilter(null);
                        }

                        prMeetingDate.setText(pendingRequest.getMeeting_date());
                        prMeetingTime.setText(pendingRequest.getMeeting_time());
                        prMeetingLocation.setText(pendingRequest.getMeeting_location());
                        prUserName.setText(pendingRequest.getName());
                        prGender.setText(pendingRequest.getGender());
                        prUserAge.setText(pendingRequest.getDate_of_birth());
                        }


                    }else{

                        Toast.makeText(getActivity(), "Sorry, couldn't load details", Toast.LENGTH_LONG).show();
                    }

                }

            @Override
            public void onFailure(Call<ConfirmedRequestResult> call, Throwable t) {
                pdLoading.dismiss();

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                //print out any error we may get
                //probably server connection
                Log.e(TAG, t.getMessage());
            }
        });
    }

    //request button listener

    //show the update meeting fragment dialog
    private void showUpdateMeetingDialog(int meetingId, String user_name, int requester_id){

        editScheduleButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                showDialog(meetingId, user_name, requester_id);
            }

        });
    }

    //confirm button listener
    //handles what happens when the cofirm button is clicked
    private void confirmButtonClick(int meetingId, String requesterName, int modified_by, int requesterId){
        confirmRequestButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                //retrofit call to update details in db
                confirmMeetingSchedule(meetingId, requesterName, modified_by, requesterId);
            }

        });
    }

    //reject button listener
    //handles what happens when the reject button is clicked
    private void rejectButtonClick(int meetingId, String requesterName, int modified_by){
        rejectRequestButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                //retrofit call to delete scheduled meeting
                deleteMeetingSchedule(meetingId, requesterName, modified_by);
            }

        });
    }

    //handle click on start session
    private void startSessionClick(){
        startSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onConfirmedRequestDetailInteraction(mMeetingId);
                }
            }
        });
    }

    //method to show meeting schedule fragment
    void showDialog(int meetingId, String user_name, int requester_id) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("update_meeting_fragment_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = UpdateMeetingScheduleFragment.newInstance(meetingId, user_name, requester_id);
        newFragment.show(ft, "update_meeting_fragment_dialog");

    }

    //retrofit call to handle a confirmed meeting
    //changes the the isConfirmed value in the db to 1 - confirmed
    private void confirmMeetingSchedule(final int meeting_id, String requesterName, int modified_by, int requester_id) {
        //pass in the language id/ name the tutorship is wanted for

        pDialog.setMessage("Working on it ...");
        showDialog();

        //Defining retrofit api service*/
        //APIService service = retrofit.create(APIService.class);
        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<Result> call = service.confirmMeetingSchedule(meeting_id, modified_by, requester_id);

        //calling the com.emtech.retrofitexample.api
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                hideDialog();

                //ResBeanSignup resBean = response.body();
                //String response_status = Encryption.decrypt(resBean.getResponse_status());

                if (!response.body().getError()) {
                    hideDialog();
                    //Log.d(LOG_TAG, EnumAppMessages.REGISTER_SUCCESS_TITLE.getValue());
                    Log.d(TAG, response.body().getMessage());

                    //show alert dialog to inform user
                    showAlertDialog(1,"Meeting confirmed","Your meeting with "+requesterName+" is confirmed." +
                            "Save the date");

                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                hideDialog();
                //print out any error we may get
                //probably server connection
                Log.e(TAG, t.getMessage());
            }
        });

    }

    //retrofit call to handle a rejected meeting
    //changes the the isConfirmed value in the db to 2 - rejected/deleted
    private void deleteMeetingSchedule(final int meeting_id, String requesterName, int modified_by) {
        //pass in the language id/ name the tutorship is wanted for

        pDialog.setMessage("Working on it ...");
        showDialog();

        //Defining retrofit api service*/
        //APIService service = retrofit.create(APIService.class);
        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<Result> call = service.deleteMeetingSchedule(meeting_id, modified_by);

        //calling the com.emtech.retrofitexample.api
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                hideDialog();

                //ResBeanSignup resBean = response.body();
                //String response_status = Encryption.decrypt(resBean.getResponse_status());

                if (!response.body().getError()) {
                    hideDialog();
                    //Log.d(LOG_TAG, EnumAppMessages.REGISTER_SUCCESS_TITLE.getValue());
                    Log.d(TAG, response.body().getMessage());

                    //show alert dialog to inform user
                    showAlertDialog(2,"Drop Meeting","You are about to delete this " +
                            "request from "+requesterName + " Are you sure");

                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                hideDialog();
                //print out any error we may get
                //probably server connection
                Log.e(TAG, t.getMessage());
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

    //alert dialog
    void showAlertDialog(int action, String title, String message) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("meeting_alert_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment newFragment = MeetingScheduleAlertDialog.newInstance(action,title, message);
        newFragment.show(getFragmentManager(), "meeting_alert_dialog");
    }

    //method to check who last modified the meeting schedule
    //if it is the current user, disable the buttons and let them know
    //that we are waiting for a response from the other user
    private void checkLastModifiedBy(int lastModifiedBy){
        if (lastModifiedBy == user_id){
            //editScheduleButton.setClickable(false);
            //confirmRequestButton.setClickable(false);

            //show alert dialog to inform user
            showAlertDialog(1,"Awaiting response","Awaiting response from "+requesterName+" about the meeting.");

        }
    }

    //this method compares today's date with the date for the scheduled meeting
    //offers the user the option to start the session if the date is today or already passed
    private void checkForSessionStart(String meetingDate, String meetingTime){
        Date scheduledMeetingDate = null;
        Date todayDate;
        Date scheduledTimeForMeeting = null;
        Date nowTime;

        //getting today's date
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        todayDate = cal.getTime();

        //getting the time now
        Calendar cal2 = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
        nowTime = cal2.getTime();

        try {
            scheduledMeetingDate = dateFormat.parse(meetingDate);
            scheduledTimeForMeeting = timeFormat.parse(meetingTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (todayDate == scheduledMeetingDate){
            if (nowTime.equals(scheduledTimeForMeeting) || nowTime.after(scheduledTimeForMeeting)){
                //make the start session button visible
                startSessionButton.setVisibility(View.VISIBLE);

                if (mListener != null) {
                    mListener.onConfirmedRequestDetailInteraction(mMeetingId);
                }
            }

        }
    }

    //method to check for internet connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
