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
import com.valdesekamdem.library.mdtoast.MDToast;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.constants.AppNums;
import viola1.agrovc.com.tonguefinal.constants.EnumAppMessages;
import viola1.agrovc.com.tonguefinal.data.network.PendingRequest;
import viola1.agrovc.com.tonguefinal.data.network.PendingRequestResult;
import viola1.agrovc.com.tonguefinal.data.network.Result;
import viola1.agrovc.com.tonguefinal.data.network.Tutor;
import viola1.agrovc.com.tonguefinal.data.network.api.APIService;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.LocalRetrofitApi;
import viola1.agrovc.com.tonguefinal.helper.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnPendingRequestDetailInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PendingRequestDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PendingRequestDetailFragment extends Fragment {
    private static final String TAG = PendingRequestDetailFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MEETING_NAME = "meeting_id";
    private static final String REQUESTER_NAME = "requester_name";
    private int mMeetingId, modified_by, user_id;
    private int requester_id;//id of the user to be notified of any changes here
    private String requesterName;
    private SessionManager sessionManager;
    private TextView prUserName, prUserAge, prGender, prDesc, prLanguagesSpoken,
            prMeetingTime, prMeetingDate, prMeetingLocation;
    private ProgressDialog pDialog;
    private CircleImageView prUserProfileImage;
    private RatingBar userRatingBar;
    private PendingRequest pendingRequest;
    private Button confirmRequestButton, rejectRequestButton, editScheduleButton;

    private OnPendingRequestDetailInteractionListener mListener;

    public PendingRequestDetailFragment() {
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
    public static PendingRequestDetailFragment newInstance(int meetingId, String requesterName) {
        PendingRequestDetailFragment fragment = new PendingRequestDetailFragment();
        Bundle args = new Bundle();
        args.putInt(MEETING_NAME, meetingId);
        args.putString(REQUESTER_NAME, requesterName);
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
                getPendingRequestDetails(mMeetingId);

            }
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onPendingRequestInteraction(uri);
        }
    }

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
        prUserProfileImage = view.findViewById(R.id.prdProfilePicImageView);

        /*//methods to handle clicks on the buttons
        showUpdateMeetingDialog(mMeetingId, requesterName);
        confirmButtonClick(mMeetingId, requesterName, user_id);
        rejectButtonClick(mMeetingId, requesterName, user_id);*/

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPendingRequestDetailInteractionListener) {
            mListener = (OnPendingRequestDetailInteractionListener) context;
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
    public interface OnPendingRequestDetailInteractionListener {
        // TODO: Update argument type and name
        void onPendingRequestInteraction(Uri uri);
    }

    //retrofit call to get the details of the pending request user
    private void getPendingRequestDetails(int mMeetingId){
        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        //this method will be running on UI thread
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();


        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<PendingRequestResult> call = service.getPendingRequestDetails(mMeetingId);

        //calling the com.emtech.retrofitexample.api
        call.enqueue(new Callback<PendingRequestResult>() {
            @Override
            public void onResponse(Call<PendingRequestResult> call, Response<PendingRequestResult> response) {
                if(response.code() == AppNums.STATUS_COD_SUCCESS) {
                    pdLoading.dismiss();

                    if (response.body().getPendingRequestDetails().getMeeting_id() > 0) {
                        PendingRequest pendingRequest = new PendingRequest();
                        pendingRequest.setMeeting_id(response.body().getPendingRequestDetails().getMeeting_id());
                        pendingRequest.setUser_id(response.body().getPendingRequestDetails().getUser_id());
                        pendingRequest.setTutor_id(response.body().getPendingRequestDetails().getTutor_id());
                        pendingRequest.setLanguage_name(response.body().getPendingRequestDetails().getLanguage_name());
                        pendingRequest.setMeeting_date(response.body().getPendingRequestDetails().getMeeting_date());
                        pendingRequest.setMeeting_time(response.body().getPendingRequestDetails().getMeeting_time());
                        pendingRequest.setMeeting_location(response.body().getPendingRequestDetails().getMeeting_location());
                        pendingRequest.setCreated_on(response.body().getPendingRequestDetails().getCreated_on());
                        pendingRequest.setLast_modified_by(response.body().getPendingRequestDetails().getLast_modified_by());
                        pendingRequest.setName(response.body().getPendingRequestDetails().getName());
                        pendingRequest.setGender(response.body().getPendingRequestDetails().getGender());
                        pendingRequest.setDate_of_birth(response.body().getPendingRequestDetails().getDate_of_birth());
                        pendingRequest.setProfile_pic(response.body().getPendingRequestDetails().getProfile_pic());

                        modified_by = response.body().getPendingRequestDetails().getLast_modified_by();

                        //this is the user to be notified via fcm
                        requester_id = pendingRequest.getUser_id();

                        //check who last modified the schedule and let them know
                        checkLastModifiedBy(modified_by);

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

                        //methods to handle clicks on the buttons
                        showUpdateMeetingDialog(mMeetingId, requesterName, requester_id);
                        confirmButtonClick(mMeetingId, requesterName, user_id, requester_id);
                        rejectButtonClick(mMeetingId, requesterName, user_id);
                        }


                    }else{

                        Toast.makeText(getActivity(), "Sorry, couldn't load details", Toast.LENGTH_LONG).show();
                    }

                }

            @Override
            public void onFailure(Call<PendingRequestResult> call, Throwable t) {
                pdLoading.dismiss();

                Toast.makeText(getActivity(), "Ooops something went wrong. Please " +
                        " try again", Toast.LENGTH_SHORT).show();
                //print out any error we may get
                //probably server connection
                Log.e(TAG, t.getMessage());
            }
        });
    }

    //request button listener

    //show the update meeting fragment dialog
    private void showUpdateMeetingDialog(int meetingId, String user_name, int requesterId){

        editScheduleButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                showDialog(meetingId, user_name, requesterId);
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

    //method to show meeting schedule fragment
    void showDialog(int meetingId, String user_name, int requesterId) {
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
        DialogFragment newFragment = UpdateMeetingScheduleFragment.newInstance(meetingId, user_name, requesterId);
        newFragment.show(ft, "update_meeting_fragment_dialog");

    }

    //retrofit call to handle a confirmed meeting
    //changes the the isConfirmed value in the db to 1 - confirmed
    private void confirmMeetingSchedule(final int meeting_id, String requesterName, int modified_by, int requesterId) {
        //pass in the language id/ name the tutorship is wanted for

        pDialog.setMessage("Working on it ...");
        showDialog();

        //Defining retrofit api service*/
        //APIService service = retrofit.create(APIService.class);
        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<Result> call = service.confirmMeetingSchedule(meeting_id, modified_by, requesterId);

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

    //method to check for internet connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
