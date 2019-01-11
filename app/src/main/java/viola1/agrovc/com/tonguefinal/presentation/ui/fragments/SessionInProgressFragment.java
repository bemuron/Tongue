package viola1.agrovc.com.tonguefinal.presentation.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

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
 * {@link SessionInProgressFragment.SessionInProgressFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SessionInProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SessionInProgressFragment extends Fragment {
    private static final String TAG = SessionInProgressFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MEETING_ID = "meeting_id";
    private static final String USER_ID = "user_id";
    private TextView sessionTimer, tutorName, userName, sessionLanguage, sessionInProgressInstruction;
    private Button pauseSession, finishSession, resumeSession;
    private CircleImageView userProfilePic, tutorProfilePic;
    private SessionManager session;
    private Handler customHandler = new Handler();
    private long timeInMillies = 0L;
    private long timeSwap = 0L;
    private long finalTime = 0L;
    private long startTime = 0L;

    private int meeting_id, requester_id, seconds, tutor_id, userId, user_id;
    private String userRole;

    private SessionInProgressFragmentInteractionListener mListener;

    public SessionInProgressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param meeting_id Parameter 1.
     * @return A new instance of fragment SessionInProgressFragment.
     */
    public static SessionInProgressFragment newInstance(int meeting_id, int user_id) {
        SessionInProgressFragment fragment = new SessionInProgressFragment();
        Bundle args = new Bundle();
        args.putInt(MEETING_ID, meeting_id);
        args.putInt(USER_ID, user_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            meeting_id = getArguments().getInt(MEETING_ID);
            //this id is the one received from the fcm notification
            //it can be for a user or a tutor who is going to be tutored
            user_id = getArguments().getInt(USER_ID);
        }

        try {
            //set the name of this fragment in the toolbar
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Session In Progress");
        }catch(NullPointerException e){
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

        // session manager
        session = new SessionManager(getActivity());
        userRole = session.getUserRole();
        userId = session.getUserId();

        //start timer as soon as this class is called
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerMethod, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session_in_progress, container, false);

        getAllWidgets(view);

        //first check if we have an internet connection
        if (isNetworkAvailable()) {
            if (meeting_id > 0) {
                //retrofit call to get the selected confirmed request details
                getMeetingDetailsForTutor(meeting_id);

                getMeetingDetailsForUser(meeting_id);
            }
        }

        // Inflate the layout for this fragment
        return view;
    }

    //initialise the view widgets we have
    private void getAllWidgets(View view) {
        sessionTimer = view.findViewById(R.id.session_timer);
        pauseSession = view.findViewById(R.id.pause_session_button);
        pauseSession.setText(R.string.pause_session);
        finishSession = view.findViewById(R.id.finish_session_button);
        userProfilePic = view.findViewById(R.id.student_icon_sip);
        tutorProfilePic = view.findViewById(R.id.tutor_icon_sip);
        tutorName = view.findViewById(R.id.tutor_name_sip);
        userName = view.findViewById(R.id.student_name_sip);
        sessionInProgressInstruction = view.findViewById(R.id.session_in_progress);
        sessionInProgressInstruction.setText(R.string.session_in_progress);
        sessionLanguage = view.findViewById(R.id.session_language_sip);
        resumeSession = view.findViewById(R.id.resumeSessionButton);

        if (userRole.equals("user") || userId == user_id) {
            resumeSession.setVisibility(View.INVISIBLE);
            finishSession.setVisibility(View.INVISIBLE);
            pauseSession.setVisibility(View.INVISIBLE);
            sessionTimer.setVisibility(View.INVISIBLE);
        }

        //disable resume and finish
        resumeSession.setEnabled(false);
        finishSession.setEnabled(false);

        //handle clicks on the buttons
        pauseSession();
        resumeSession();
        finishSession();
    }

    //handles click on the pause session button to pause the session
    private void pauseSession(){
        pauseSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //enable resume and finish buttons
                resumeSession.setEnabled(true);
                finishSession.setEnabled(true);

                timeSwap += timeInMillies;
                customHandler.removeCallbacks(updateTimerMethod);
            }
        });
    }

    //handles clicks on the resume session button
    private void resumeSession(){
        resumeSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //disable finish and resume
                resumeSession.setEnabled(false);
                finishSession.setEnabled(false);

                //resume timer
                startTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerMethod, 0);
            }
        });
    }

    //handles click on the finish session button to finish the session
    private void finishSession(){
        finishSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {

                    float sessionSecs = (finalTime / 1000);
                    float sessionMins = (sessionSecs/60);
                    float sessionHrs = (sessionMins/60);
                    float secs =  (sessionSecs/120);
                    float mins = (sessionMins/60);
                    Log.e(TAG, "session hours = "+ sessionHrs);
                    Log.e(TAG, "session mins = "+ mins);
                    Log.e(TAG, "session secs = "+ secs);
                    //retrofit call to end session
                    endMeetingSession(meeting_id, tutor_id, requester_id, finalTime);
                    mListener.onSessionInProgressInteraction(meeting_id, tutor_id, requester_id, finalTime);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SessionInProgressFragmentInteractionListener) {
            mListener = (SessionInProgressFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SessionInProgressFragmentInteractionListener");
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
    public interface SessionInProgressFragmentInteractionListener {
        // the current session meeting id
        void onSessionInProgressInteraction(int meeting_id, int tutor_id, int student_id, float hoursTaken);
    }

    private Runnable updateTimerMethod = new Runnable() {

        public void run() {
            timeInMillies = SystemClock.uptimeMillis() - startTime;
            finalTime = timeSwap + timeInMillies;

            int seconds = (int) (finalTime / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (finalTime % 1000);
            sessionTimer.setText("" + hours + "hrs :"
                    + minutes + "mins :"
                    + String.format("%02d", seconds) +"secs");
            /*sessionTimer.setText("" + minutes + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%03d", milliseconds));*/
            customHandler.postDelayed(this, 0);
        }

    };

    //retrofit call to get the user details of the confirmed request
    private void getMeetingDetailsForTutor(int mMeetingId){
        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        //this method will be running on UI thread
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();


        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<ConfirmedRequestResult> call = service.getMeetingDetailsForTutor(mMeetingId);

        //calling the com.emtech.retrofitexample.api
        call.enqueue(new Callback<ConfirmedRequestResult>() {
            @Override
            public void onResponse(Call<ConfirmedRequestResult> call, Response<ConfirmedRequestResult> response) {
                if(response.code() == AppNums.STATUS_COD_SUCCESS) {
                    pdLoading.dismiss();

                    if (response.body().getTutorMeetingDetails().getMeeting_id() > 0) {
                        PendingRequest pendingRequest = new PendingRequest();
                        pendingRequest.setMeeting_id(response.body().getTutorMeetingDetails().getMeeting_id());
                        pendingRequest.setUser_id(response.body().getTutorMeetingDetails().getUser_id());
                        pendingRequest.setTutor_id(response.body().getTutorMeetingDetails().getTutor_id());
                        pendingRequest.setLanguage_name(response.body().getTutorMeetingDetails().getLanguage_name());
                        pendingRequest.setMeeting_date(response.body().getTutorMeetingDetails().getMeeting_date());
                        pendingRequest.setMeeting_time(response.body().getTutorMeetingDetails().getMeeting_time());
                        pendingRequest.setMeeting_location(response.body().getTutorMeetingDetails().getMeeting_location());
                        pendingRequest.setCreated_on(response.body().getTutorMeetingDetails().getCreated_on());
                        pendingRequest.setLast_modified_by(response.body().getTutorMeetingDetails().getLast_modified_by());
                        pendingRequest.setName(response.body().getTutorMeetingDetails().getName());
                        pendingRequest.setGender(response.body().getTutorMeetingDetails().getGender());
                        pendingRequest.setDate_of_birth(response.body().getTutorMeetingDetails().getDate_of_birth());
                        pendingRequest.setProfile_pic(response.body().getTutorMeetingDetails().getProfile_pic());

                        //modified_by = response.body().getConfirmedRequestDetails().getLast_modified_by();
                        tutor_id = response.body().getTutorMeetingDetails().getTutor_id();

                        //get id of user to be notified
                        requester_id = pendingRequest.getUser_id();

                        userName.setText(pendingRequest.getName());
                        sessionLanguage.setText("Language: "+pendingRequest.getLanguage_name());

                        if (!TextUtils.isEmpty(pendingRequest.getProfile_pic())) {
                            RequestOptions options = new RequestOptions()
                                    .centerCrop()
                                    .placeholder(R.drawable.profile_image_placeholder)
                                    .error(R.drawable.profile_image_placeholder)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .priority(Priority.HIGH);

                            Glide.with(getActivity()).load("http://apps.incubation.billbrain.tech/tongueApp/assets/images/profile_pics/" + pendingRequest.getProfile_pic())
                                    .thumbnail(0.5f)
                                    .apply(options)
                                    .into(userProfilePic);
                        }
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

    //retrofit call to get the tutor details of the confirmed request user
    private void getMeetingDetailsForUser(int mMeetingId){
        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        //this method will be running on UI thread
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        //pdLoading.show();


        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<ConfirmedRequestResult> call = service.getMeetingDetailsForUser(mMeetingId);

        //calling the com.emtech.retrofitexample.api
        call.enqueue(new Callback<ConfirmedRequestResult>() {
            @Override
            public void onResponse(Call<ConfirmedRequestResult> call, Response<ConfirmedRequestResult> response) {
                if(response.code() == AppNums.STATUS_COD_SUCCESS) {
                    pdLoading.dismiss();

                    if (response.body().getUserMeetingDetails().getMeeting_id() > 0) {
                        PendingRequest pendingRequest = new PendingRequest();
                        pendingRequest.setMeeting_id(response.body().getUserMeetingDetails().getMeeting_id());
                        pendingRequest.setUser_id(response.body().getUserMeetingDetails().getUser_id());
                        pendingRequest.setTutor_id(response.body().getUserMeetingDetails().getTutor_id());
                        pendingRequest.setLanguage_name(response.body().getUserMeetingDetails().getLanguage_name());
                        pendingRequest.setMeeting_date(response.body().getUserMeetingDetails().getMeeting_date());
                        pendingRequest.setMeeting_time(response.body().getUserMeetingDetails().getMeeting_time());
                        pendingRequest.setMeeting_location(response.body().getUserMeetingDetails().getMeeting_location());
                        pendingRequest.setCreated_on(response.body().getUserMeetingDetails().getCreated_on());
                        pendingRequest.setLast_modified_by(response.body().getUserMeetingDetails().getLast_modified_by());
                        pendingRequest.setName(response.body().getUserMeetingDetails().getName());
                        pendingRequest.setGender(response.body().getUserMeetingDetails().getGender());
                        pendingRequest.setDate_of_birth(response.body().getUserMeetingDetails().getDate_of_birth());
                        pendingRequest.setProfile_pic(response.body().getUserMeetingDetails().getProfile_pic());

                        //modified_by = response.body().getUserConfirmedRequestDetails().getLast_modified_by();

                        tutorName.setText(pendingRequest.getName());

                        if (!TextUtils.isEmpty(pendingRequest.getProfile_pic())) {
                            Glide.with(getActivity()).load("http://apps.incubation.billbrain.tech/tongueApp/assets/images/profile_pics/" + pendingRequest.getProfile_pic())
                                    .thumbnail(0.5f)
                                    .into(tutorProfilePic);
                        }
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

    //retrofit call to stop/end the session
    //this method simply changes the meeting status/isConfirmed field value in the db to 3: completed
    private void endMeetingSession(final int meeting_id, int tutor_id, int notify_user_id, float time_taken) {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        //this method will be running on UI thread
        pdLoading.setMessage("\tEnding session...");
        pdLoading.setCancelable(false);
        pdLoading.show();

        //Defining retrofit api service*/
        //APIService service = retrofit.create(APIService.class);
        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<Result> call = service.endMeetingSession(meeting_id, tutor_id, notify_user_id, time_taken);

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

                    //save to prefs that the meeting is done
                    session.spMeetingFinished(meeting_id, notify_user_id);

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

    //method to check for internet connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

}
