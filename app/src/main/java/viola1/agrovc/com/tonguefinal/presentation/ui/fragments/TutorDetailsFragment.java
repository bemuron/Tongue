package viola1.agrovc.com.tonguefinal.presentation.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private ImageView profilePicImageView;
    private TextView tutorName, tutorAge, tutorGender, tutorDesc, tutorLanguagesTaught,
            tutorLanguagesSpoken;
    private int tutorRating;
    private RatingBar tutorRatingBar;
    private Tutor tutor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutor_details,container,false);

        Bundle bundle = getArguments();
        if (bundle != null) {
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
    public void getAllWidgets(View view) {
        tutorName = view.findViewById(R.id.tutorNameTextView);
        tutorAge = view.findViewById(R.id.tutorAgeTextView);
        tutorGender = view.findViewById(R.id.tutorGenderTextView);
        tutorDesc = view.findViewById(R.id.tutorDescTextView);
        tutorRatingBar = view.findViewById(R.id.tutorRatingBar);
        tutorLanguagesTaught = view.findViewById(R.id.tutorLanguagesTaughtTextView);
        tutorLanguagesSpoken = view.findViewById(R.id.tutorLanguagesSpokenTextView);

    }

    //retrofit call to get the list of tutors
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
                        //tutor.setRating(response.body().getTutorDetails().getRating());
                        tutor.setGender(response.body().getTutorDetails().getGender());

                        tutorName.setText(tutor.getName());
                        tutorAge.setText(tutor.getDate_of_birth());
                        tutorDesc.setText(tutor.getDescription());
                        //tutor_id.setText(tutor.getUser_id());
                        tutorRatingBar.setRating(4.0F);
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

    //retrofit call to get the tutor rating
    private void getTutorRating(){
        float rating = 4.0F;

    }
}
