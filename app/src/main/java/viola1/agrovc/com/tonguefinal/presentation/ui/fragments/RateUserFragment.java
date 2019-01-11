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
import viola1.agrovc.com.tonguefinal.data.network.ConfirmedRequestResult;
import viola1.agrovc.com.tonguefinal.data.network.PendingRequest;
import viola1.agrovc.com.tonguefinal.data.network.Result;
import viola1.agrovc.com.tonguefinal.data.network.api.APIService;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.LocalRetrofitApi;
import viola1.agrovc.com.tonguefinal.presentation.ui.activities.BecomeTutorActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RateUserFragment.OnRateUserListener} interface
 * to handle interaction events.
 * Use the {@link RateUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * This class is shown to the tutor to rate the user
 */
public class RateUserFragment extends Fragment {
    private static final String TAG = RateUserFragment.class.getSimpleName();
    private static final String MEETING_ID = "meeting_id";
    //ID of the current user in this case a tutor
    private static final String USER_ID = "user_id";
    private int meeting_id, user_id, tutor_id, student_id;
    private float userRating;
    private String userName;
    private RatingBar userRatingBar;
    private Button submitUserRatingButton;
    private TextView userNameTv, rateUserInstruction;
    private CircleImageView userIcon;

    private OnRateUserListener mListener;

    public RateUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param meeting_id Parameter 1: id of the meeting.
     * @param user_id Parameter 2: id of the user.
     * @return A new instance of fragment RateUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RateUserFragment newInstance(int meeting_id, int user_id) {
        RateUserFragment fragment = new RateUserFragment();
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
            user_id = getArguments().getInt(USER_ID);
        }

        //set the name of this fragment in the toolbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Rate Student");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rate_user, container,false);

        getAllWidgets(view);

        //first check if we have an internet connection
        if (isNetworkAvailable()) {
            if (meeting_id > 0) {
                //retrofit call to get the selected pending request details
                getMeetingDetailsForTutor(meeting_id);

            }
        }
        return view;
    }

    private void getAllWidgets(View view){
        userNameTv = view.findViewById(R.id.rate_user_name);
        rateUserInstruction = view.findViewById(R.id.rate_user_instruction);
        rateUserInstruction.setText(R.string.rate_student);
        submitUserRatingButton = view.findViewById(R.id.submitUserRatingButton);
        userIcon = view.findViewById(R.id.rate_user_icon);
        userRatingBar = view.findViewById(R.id.userRatingBar);

        handleUserRating();
        handleSubmitRating();
    }

    //handles click on rating submit button
    private void handleSubmitRating(){
        submitUserRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if (userRating != 0) {
                        //user_id here is id of the tutor/app owner
                        submitUserRating(meeting_id, user_id, student_id, userRating);
                    }
                }catch (Exception e){
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRateUserListener) {
            mListener = (OnRateUserListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRateUserListener");
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
    public interface OnRateUserListener {
        void onRateUserFragmentInteraction(int meeting_id);
    }

    //handle the rating
    private void handleUserRating(){
        userRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                userRating = ratingBar.getRating();
                Log.e(TAG, "user rating = "+userRating);
            }
        });
    }

    //retrofit call to get the tutor details of the confirmed request user
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

                        //get id of the student
                        student_id = pendingRequest.getUser_id();

                        userNameTv.setText(pendingRequest.getName());

                        if (!TextUtils.isEmpty(pendingRequest.getProfile_pic())) {
                            Glide.with(getActivity()).load("http://apps.incubation.billbrain.tech/tongueApp/assets/images/profile_pics/"+pendingRequest.getProfile_pic())
                                    .thumbnail(0.5f)
                                    .into(userIcon);
                            userIcon.setColorFilter(null);
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

    //retrofit call to send user rating to db
    private void submitUserRating(int meeting_id, int tutor_id, int student_id, float ratingValue){
        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        //this method will be running on UI thread
        pdLoading.setMessage("\tSubmitting rating...");
        pdLoading.setCancelable(false);
        pdLoading.show();

        submitUserRatingButton.setEnabled(false);


        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<Result> call = service.submitUserRating(meeting_id, tutor_id, student_id, ratingValue);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                //if(response.code() == AppNums.STATUS_COD_SUCCESS) {
                pdLoading.dismiss();
                //if response body is not null, we have some data
                //successful addition
                if (!response.body().getError()) {
                    Log.e(TAG, "Rating submitted successfully");

                    //upon success head back to parent activity
                    if (mListener != null) {
                        mListener.onRateUserFragmentInteraction(meeting_id);
                    }
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    //}
            }
            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                submitUserRatingButton.setEnabled(true);
                Log.e(TAG, t.getMessage());
                pdLoading.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
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
