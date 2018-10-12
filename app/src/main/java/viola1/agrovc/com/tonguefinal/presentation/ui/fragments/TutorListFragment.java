package viola1.agrovc.com.tonguefinal.presentation.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.constants.AppNums;
import viola1.agrovc.com.tonguefinal.constants.EnumAppMessages;
import viola1.agrovc.com.tonguefinal.data.network.Tutor;
import viola1.agrovc.com.tonguefinal.data.network.Tutors;
import viola1.agrovc.com.tonguefinal.data.network.api.APIService;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.LocalRetrofitApi;
import viola1.agrovc.com.tonguefinal.presentation.adapters.TutorListAdapter;
import viola1.agrovc.com.tonguefinal.presentation.ui.activities.TutorActivity;

public class TutorListFragment extends Fragment
        implements TutorListAdapter.TutorListAdapterListener{
    private static final String TAG = TutorListFragment.class.getSimpleName();
    private OnTutorSelectedListener tutorSelectedListener;
    private RecyclerView recyclerView;
    private TutorListAdapter tutorListAdapter;
    private List<Tutor> tutorList = new ArrayList<>();

    public TutorListFragment(){

    }

    public  static  TutorListFragment newInstance(int languageId, String languageName){
        Bundle arguments = new Bundle();
        arguments.putInt("language_id", languageId);
        arguments.putString("language_name", languageName);
        TutorListFragment fragment = new TutorListFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        //tutorListAdapter.notifyDataSetChanged();
        //sectionsViewModel.getSections(getArguments().getString("catname"));
        Toast.makeText(getActivity(), ""+getArguments().getInt("language_id"), Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutor_list,container,false);

        //setting the title of the fragment
        getActivity().setTitle(getArguments().getString("language_name")+" tutors");

        //initialize the view widgets
        getAllWidgets(view);

        //set up the list adapter
        setupListAdapter();

        //retrofit call to get tutor list for specific language
        getTutors(getArguments().getInt("language_id"));

        return view;
    }

    // Container Activity must implement this interface
    public interface OnTutorSelectedListener {
        public void tutorSelectionCallback(int tutorId);
    }

    @Override
    public void onAttach(Context context) { //Try Context context as the parameter. It is not deprecated
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            tutorSelectedListener = (OnTutorSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnTutorSelectedListener");
        }
    }

    public void getAllWidgets(View view) {
        recyclerView = view.findViewById(R.id.tutorListRecyclerView);
    }

    private void setupListAdapter() {
        tutorListAdapter = new TutorListAdapter(TutorActivity.instance, tutorList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TutorActivity.instance);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(TutorActivity.instance, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(tutorListAdapter);
    }

    //adapter method to handle clicks on list items
    //this method calls the interface OnTutorSelectedListener which is
    //implemented in the TutorActivity
    @Override
    public void onTutorRowClicked(int position){
        Tutor tutor = tutorList.get(position);
        Log.d(TAG,"Tutor ID from List to details = "+ tutor.getUser_id());
        tutorSelectedListener.tutorSelectionCallback(tutor.getUser_id());
    }

    //retrofit call to get the list of tutors
    private void getTutors(int language_id){

        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        //this method will be running on UI thread
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();

        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<Tutors> call = service.getTutorsForLanguage(language_id);

        //calling the com.emtech.retrofitexample.api
        call.enqueue(new Callback<Tutors>() {
            @Override
            public void onResponse(Call<Tutors> call, Response<Tutors> response) {
                if(response.code() == AppNums.STATUS_COD_SUCCESS) {
                    pdLoading.dismiss();

                    if (response.body().getTutorList().size() > 0){

                        //clear the previous search list if it has content
                        if (tutorList != null) {
                            tutorList.clear();
                        }

                        for (int i = 0; i < response.body().getTutorList().size(); i++) {

                            Tutor tutor = new Tutor();
                            tutor.setUser_id(response.body().getTutorList().get(i).getUser_id());
                            tutor.setName(response.body().getTutorList().get(i).getName());
                            tutor.setDescription(response.body().getTutorList().get(i).getDescription());
                            tutor.setProfile_pic(response.body().getTutorList().get(i).getProfile_pic());

                            tutor.setColor(getRandomMaterialColor("400"));

                            tutorList.add(tutor);
                            Log.d(TAG,"Tutor ID from network = "+ response.body().getTutorList().get(i).getUser_id());
                        }

                        tutorListAdapter.notifyDataSetChanged();

                    }else if (response.body().getTutorList().size() == 0){

                        Toast.makeText(getActivity(), "No tutors for this language yet", Toast.LENGTH_LONG).show();
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
        public void onFailure(Call<Tutors> call, Throwable t) {
            pdLoading.dismiss();

            Toast.makeText(getActivity(), "Ooops something went wrong. Please " +
                    "try again", Toast.LENGTH_SHORT).show();
            //print out any error we may get
            //probably server connection
            Log.e(TAG, t.getMessage());
        }
    });
    }

    /**
     * chooses a random color from array.xml
     */
    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", TutorActivity.instance.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }

}
