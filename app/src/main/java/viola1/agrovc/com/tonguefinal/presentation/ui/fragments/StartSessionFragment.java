package viola1.agrovc.com.tonguefinal.presentation.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
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
import viola1.agrovc.com.tonguefinal.presentation.ui.activities.RatingActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StartSessionFragment.OnStartSessionFragmentListener} interface
 * to handle interaction events.
 * Use the {@link StartSessionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartSessionFragment extends Fragment {
    private static final String TAG = StartSessionFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MEETING_ID = "meeting_id";
    private static final String ARG_PARAM2 = "param2";
    private CircleImageView tutorProfilePic, userProfilePic;
    private TextView tutorName, userName, sessionLanguage, startSessionInstruction;
    private Button startSessionButton;
    private ProgressDialog pDialog;
    private int mMeetingId, modified_by, tutor_id, requester_id;
    private String mParam2;

    private OnStartSessionFragmentListener mListener;

    public StartSessionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param meetingId id of the meeting.
     * @return A new instance of fragment StartSessionFragment.
     */
    // this gets the confirmed meeting id to use to get the rest of the details
    public static StartSessionFragment newInstance(int meetingId) {
        StartSessionFragment fragment = new StartSessionFragment();
        Bundle args = new Bundle();
        args.putInt(MEETING_ID, meetingId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMeetingId = getArguments().getInt(MEETING_ID);
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Start Session");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start_session, container,false);

        getAllWidgets(view);

        //first check if we have an internet connection
        if (isNetworkAvailable()) {
            if (mMeetingId > 0) {
                //retrofit call to get the selected confirmed request details
                getMeetingDetailsForTutor(mMeetingId);

                getMeetingDetailsForUser(mMeetingId);
            }
        }

        return view;
    }

    //initialise the view widgets we have
    public void getAllWidgets(View view) {
        tutorProfilePic = view.findViewById(R.id.tutor_icon);
        userProfilePic = view.findViewById(R.id.student_icon);
        tutorName = view.findViewById(R.id.tutor_name);
        userName = view.findViewById(R.id.student_name);
        startSessionInstruction = view.findViewById(R.id.start_session);
        startSessionInstruction.setText(R.string.start_session);
        sessionLanguage = view.findViewById(R.id.session_language);
        startSessionButton = view.findViewById(R.id.start_session_button);
        startSessionButton.setText(R.string.start_session_button);

        handleStartSessionButton();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStartSessionFragmentListener) {
            mListener = (OnStartSessionFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStartSessionFragmentListener");
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
    public interface OnStartSessionFragmentListener {
        void onStartSessionInteraction(int meetingId);
    }

    //send the meeting id to parent activity
    private void handleStartSessionButton(){
        startSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    startMeetingSession(mMeetingId, tutor_id, requester_id);
                    mListener.onStartSessionInteraction(mMeetingId);
                }
            }
        });
    }

    //retrofit call to get the user details of the confirmed request
    private void getMeetingDetailsForTutor(int mMeetingId){
        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        //this method will be running on UI thread
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        //pdLoading.show();


        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<ConfirmedRequestResult> call = service.getMeetingDetailsForTutor(mMeetingId);

        //calling the com.emtech.retrofitexample.api
        call.enqueue(new Callback<ConfirmedRequestResult>() {
            @Override
            public void onResponse(Call<ConfirmedRequestResult> call, Response<ConfirmedRequestResult> response) {
                if(response.code() == AppNums.STATUS_COD_SUCCESS) {
                    //pdLoading.dismiss();

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

                        //get id of user to be notified
                        requester_id = pendingRequest.getUser_id();

                        userName.setText(pendingRequest.getName());
                        sessionLanguage.append("Language: "+pendingRequest.getLanguage_name());

                        if (!TextUtils.isEmpty(pendingRequest.getProfile_pic())) {
                            Glide.with(getActivity()).load("http://apps.incubation.billbrain.tech/tongueApp/assets/images/profile_pics/" + pendingRequest.getProfile_pic())
                                    .thumbnail(0.5f)
                                    .into(userProfilePic);
                        }
                    }


                }else{

                    Toast.makeText(getActivity(), "Sorry, couldn't load details", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ConfirmedRequestResult> call, Throwable t) {
                //pdLoading.dismiss();

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
                    //pdLoading.dismiss();

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

                        tutor_id = response.body().getUserMeetingDetails().getTutor_id();

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
                //pdLoading.dismiss();

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                //print out any error we may get
                //probably server connection
                Log.e(TAG, t.getMessage());
            }
        });
    }

    //retrofit call to start the session
    //this method simply changes the meeting status value in the db to 2: started/in session
    private void startMeetingSession(final int meeting_id, int tutor_id, int notify_user_id) {
        //pass in the language id/ name the tutorship is wanted for

        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        //this method will be running on UI thread
        pdLoading.setMessage("\tStarting session...");
        pdLoading.setCancelable(false);
        //pdLoading.show();

        //Defining retrofit api service*/
        //APIService service = retrofit.create(APIService.class);
        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<Result> call = service.startMeetingSession(meeting_id, tutor_id, notify_user_id);

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

                    //show toast to inform user
                    //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                pdLoading.dismiss();
                //print out any error we may get
                //Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
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
