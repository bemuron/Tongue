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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RateTutorFragment.OnRateTutorInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RateTutorFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * this class is shown to the user to rate the tutor
 */
public class RateTutorFragment extends Fragment {
    private static final String TAG = RateTutorFragment.class.getSimpleName();
    private static final String MEETING_ID = "meeting_id";
    //ID of the current user in this case a user
    private static final String USER_ID = "user_id";
    private int meeting_id, user_id, tutor_id;
    private float tutor_rating;
    private String tutorName;
    private RatingBar tutorRatingBar;
    private Button submitTutorRatingButton;
    private TextView tutorNameTv, rateTutorInstruction;
    private CircleImageView tutorIcon;

    private OnRateTutorInteractionListener mListener;

    public RateTutorFragment() {
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
    public static RateTutorFragment newInstance(int meeting_id, int user_id) {
        RateTutorFragment fragment = new RateTutorFragment();
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Rate Tutor");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rate_tutor, container,false);

        getAllWidgets(view);

        //first check if we have an internet connection
        if (isNetworkAvailable()) {
            if (meeting_id > 0) {
                //retrofit call to get the selected pending request details
                getConfirmedRequestDetails(meeting_id);

            }
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int meeting_id) {
        if (mListener != null) {
            mListener.onRateTutorInteraction(meeting_id);
        }
    }

    private void getAllWidgets(View view){
        tutorNameTv = view.findViewById(R.id.rate_tutor_name);
        submitTutorRatingButton = view.findViewById(R.id.submitTutorRatingButton);
        tutorIcon = view.findViewById(R.id.rate_tutor_icon);
        rateTutorInstruction = view.findViewById(R.id.rate_tutor_instruction);
        rateTutorInstruction.setText(R.string.rate_tutor_instruction);
        tutorRatingBar = view.findViewById(R.id.tutorRatingBar);

        handleTutorRating();
        handleSubmitRating();
    }

    //handles click on rating submit button
    private void handleSubmitRating(){
        submitTutorRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if (tutor_rating != 0) {
                        submitTutorRating(meeting_id, user_id, tutor_id, tutor_rating);
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
        if (context instanceof OnRateTutorInteractionListener) {
            mListener = (OnRateTutorInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRateTutorInteractionListener");
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
    public interface OnRateTutorInteractionListener {
        void onRateTutorInteraction(int meeting_id);
    }

    //handle the rating
    private void handleTutorRating(){
        tutorRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                tutor_rating = ratingBar.getRating();
                Log.e(TAG, "tutor rating = "+tutor_rating);
            }
        });
    }

    //retrofit call to get the tutor details of the confirmed request
    private void getConfirmedRequestDetails(int mMeetingId){
        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        //this method will be running on UI thread
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();


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

                        //get id of tutor to be notified
                        tutor_id = pendingRequest.getTutor_id();

                        tutorNameTv.setText(pendingRequest.getName());

                        if (!TextUtils.isEmpty(pendingRequest.getProfile_pic())) {
                            Glide.with(getActivity()).load("http://apps.incubation.billbrain.tech/tongueApp/assets/images/profile_pics/"+pendingRequest.getProfile_pic())
                                    .thumbnail(0.5f)
                                    .into(tutorIcon);
                            tutorIcon.setColorFilter(null);
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

    //retrofit call to send tutor rating to db
    private void submitTutorRating(int meeting_id, int user_id, int tutor_id, float ratingValue){
        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        //this method will be running on UI thread
        pdLoading.setMessage("\tSubmitting rating...");
        pdLoading.setCancelable(false);
        pdLoading.show();

        submitTutorRatingButton.setEnabled(false);


        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<Result> call = service.submitTutorRating(meeting_id, user_id, tutor_id, ratingValue);
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
                        mListener.onRateTutorInteraction(meeting_id);
                    }
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                //}
            }
            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                submitTutorRatingButton.setEnabled(true);
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
