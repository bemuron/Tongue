package viola1.agrovc.com.tonguefinal.presentation.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
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

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.constants.AppNums;
import viola1.agrovc.com.tonguefinal.constants.EnumAppMessages;
import viola1.agrovc.com.tonguefinal.data.network.LanguagesTaught;
import viola1.agrovc.com.tonguefinal.data.network.Result;
import viola1.agrovc.com.tonguefinal.data.network.Tutor;
import viola1.agrovc.com.tonguefinal.data.network.api.APIService;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.LocalRetrofitApi;

public class TutorDetailsFragment extends Fragment {
    private static final String TAG = TutorDetailsFragment.class.getSimpleName();
    private OnTutorRequestedListener tutorRequestedListener;
    private CircleImageView profilePicImageView;
    private TextView tutorName, tutorAge, tutorGender, tutorDesc, tutorLanguagesTaught,
            tutorLanguagesSpoken;
    private Button requestButton;
    private int tutorId;
    private float tutorRating;
    private RatingBar tutorRatingBar;
    private Tutor tutor;
    private String tutor_name, language_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutor_details,container,false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            tutorId = bundle.getInt("tutor_id");
            tutor_name = bundle.getString("tutor_name");
            tutorRating = bundle.getFloat("tutor_rating");
            language_name = bundle.getString("language_name");

            //retrofit call to get the selected tutor details
            getSelectedTutorDetails(bundle.getInt("tutor_id"));

            //retrofit call to get the list of languages spoken by the tutor
            getTutorLanguagesTaught(bundle.getInt("tutor_id"));
        }

        //initialize the view widgets
        getAllWidgets(view);

        return view;
    }

    // Container Activity must implement this interface
    public interface OnTutorRequestedListener {
        public void tutorRequestCallback(int tutorId);
    }

    @Override
    public void onAttach(Context context) { //Try Context context as the parameter. It is not deprecated
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            tutorRequestedListener = (OnTutorRequestedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnTutorRequestedListener");
        }
    }

    //initialise the view widgets we have
    private void getAllWidgets(View view) {
        profilePicImageView = view.findViewById(R.id.tutorProfilePicImageView);
        tutorName = view.findViewById(R.id.tutorNameTextView);
        tutorAge = view.findViewById(R.id.tutorAgeTextView);
        tutorGender = view.findViewById(R.id.tutorGenderTextView);
        tutorDesc = view.findViewById(R.id.tutorDescTextView);
        tutorRatingBar = view.findViewById(R.id.tutorRatingBar);
        tutorRatingBar.setRating(tutorRating);
        tutorLanguagesTaught = view.findViewById(R.id.tutorLanguagesTaughtTextView);
        //tutorLanguagesSpoken = view.findViewById(R.id.tutorLanguagesSpokenTextView);
        requestButton = view.findViewById(R.id.tutorRequestButton);

        showScheduleMeetingDialog(tutorId, tutor_name, language_name);

    }

    //retrofit call to get the details of a tutor
    private void getSelectedTutorDetails(int tutor_id) {

        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        //this method will be running on UI thread
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();


        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<Result> call = service.getTutorDetails(tutor_id);

        //calling the com.emtech.retrofitexample.api
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(response.code() == AppNums.STATUS_COD_SUCCESS) {
                    pdLoading.dismiss();

                    if (!response.body().getError()) {
                        tutor = new Tutor();
                        tutor.setName(response.body().getTutorDetails().getName());
                        tutor.setDate_of_birth(response.body().getTutorDetails().getDate_of_birth());
                        tutor.setDescription(response.body().getTutorDetails().getDescription());
                        tutor.setUser_id(response.body().getTutorDetails().getUser_id());
                        tutor.setProfile_pic(response.body().getTutorDetails().getProfile_pic());
                        //tutor.setRating(response.body().getTutorDetails().getRating());
                        tutor.setGender(response.body().getTutorDetails().getGender());

                        if (!TextUtils.isEmpty(tutor.getProfile_pic())) {
                            Glide.with(getActivity()).load("http://apps.incubation.billbrain.tech/tongueApp/assets/images/profile_pics/"+tutor.getProfile_pic())
                                    .thumbnail(0.5f)
                                    .into(profilePicImageView);
                            profilePicImageView.setColorFilter(null);
                        }

                        //get the tutor rating
                        //getTutorRating(tutor.getUser_id());

                        tutorName.setText(tutor.getName());
                        tutorAge.setText(tutor.getDate_of_birth());

                        /*Calendar dob = Calendar.getInstance();
                        Calendar today = Calendar.getInstance();
                        dob.set(Calendar.DATE, Integer.parseInt(tutor.getDate_of_birth()));
                        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

                        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
                            age--;
                        }

                        Integer ageInt = new Integer(age);
                        String ageS = ageInt.toString();

                        tutorAge.setText(ageS);*/

                        tutorDesc.setText(tutor.getDescription());
                        //tutor_id.setText(tutor.getUser_id());
                        //tutorRatingBar.setRating(4.0F);
                        tutorGender.setText(tutor.getGender());

                        //set title of fragment in toolbar
                        getActivity().setTitle(response.body().getTutorDetails().getName());

                    }else{

                        Toast.makeText(getActivity(), "Sorry, couldn't get tutor details", Toast.LENGTH_LONG).show();
                    }

                }
                else if(response.code() == AppNums.STATUS_COD_FILE_NOT_FOUND){
                    pdLoading.dismiss();

                    Log.e(TAG, EnumAppMessages.ERROR_RESOURCE_NOT_FOUND.getValue());


                }else{
                    pdLoading.dismiss();


                    //generalMethods.showLocationDialog(Login.this,EnumAppMessages.LOGIN_ERROR_TITLE.getValue(),EnumAppMessages.ERROR_INTERNAL_ERROR.getValue());
                    Log.e(TAG, EnumAppMessages.ERROR_INTERNAL_ERROR.getValue());

                }

            }
            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                pdLoading.dismiss();

                Toast.makeText(getActivity(), "Ooops something went wrong. Please " +
                        " try again", Toast.LENGTH_SHORT).show();
                //print out any error we may get
                //probably server connection
                Log.e(TAG, t.getMessage());
            }
        });
    }

    //retrofit call to get the list of languages taught by the user
    private void getTutorLanguagesTaught(int tutor_id) {

        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<LanguagesTaught> call = service.getLanguagesForTutor(tutor_id);

        //calling the com.emtech.retrofitexample.api
        call.enqueue(new Callback<LanguagesTaught>() {
            @Override
            public void onResponse(Call<LanguagesTaught> call, Response<LanguagesTaught> response) {
                if(response.code() == AppNums.STATUS_COD_SUCCESS) {

                    if (response.body().getTutorLanguages().size() > 0) {

                        for (int i = 0; i < response.body().getTutorLanguages().size(); i++) {
                            tutorLanguagesTaught.append(response.body().getTutorLanguages().get(i).getName()+ " | ");
                        }

                    }else{

                        Toast.makeText(getActivity(), "Sorry, couldn't get languages this " +
                                "tutor teaches", Toast.LENGTH_LONG).show();
                    }

                }
                else if(response.code() == AppNums.STATUS_COD_FILE_NOT_FOUND){

                    Log.e(TAG, EnumAppMessages.ERROR_RESOURCE_NOT_FOUND.getValue());


                }else{

                    //generalMethods.showLocationDialog(Login.this,EnumAppMessages.LOGIN_ERROR_TITLE.getValue(),EnumAppMessages.ERROR_INTERNAL_ERROR.getValue());
                    Log.e(TAG, EnumAppMessages.ERROR_INTERNAL_ERROR.getValue());

                }

            }
            @Override
            public void onFailure(Call<LanguagesTaught> call, Throwable t) {

                Toast.makeText(getActivity(), "Ooops something went wrong. Please " +
                        " try again", Toast.LENGTH_SHORT).show();
                //print out any error we may get
                //probably server connection
                Log.e(TAG, t.getMessage());
            }
        });
    }

    //request button listener

    //show the schedule fragment dialog
    private void showScheduleMeetingDialog(int tutorId, String tutor_name, String language_name){

        requestButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                showDialog(tutorId, tutor_name, language_name);
            }

        });
    }

    //method to show meeting schedule fragment
    void showDialog(int tutorId, String tutor_name, String language_name) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("schedule_meeting_fragment_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = ScheduleMeetingFragment.newInstance(tutorId, tutor_name, language_name);
        newFragment.show(ft, "schedule_meeting_fragment_dialog");

    }

    //retrofit call to get the tutor rating
    private void getTutorRating(int tutor_id){

        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<Tutor> call = service.getTutorRating(tutor_id);

        //calling the com.emtech.retrofitexample.api
        call.enqueue(new Callback<Tutor>() {
            @Override
            public void onResponse(Call<Tutor> call, Response<Tutor> response) {
                if(response.code() == AppNums.STATUS_COD_SUCCESS) {

                    tutor.setTutorRating(response.body().getTutorRating());

                    Log.d(TAG,"Tutor rating = "+ response.body().getTutorRating());

                }
            }

            @Override
            public void onFailure(Call<Tutor> call, Throwable t) {

                //Toast.makeText(getActivity(), "Could not get tutor rating.", Toast.LENGTH_SHORT).show();
                //print out any error we may get
                //probably server connection
                Log.e(TAG, t.getMessage());
            }
        });
    }
}
