package viola1.agrovc.com.tonguefinal.presentation.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import viola1.agrovc.com.tonguefinal.data.network.PendingRequest;
import viola1.agrovc.com.tonguefinal.data.network.PendingRequests;
import viola1.agrovc.com.tonguefinal.data.network.UserPendingRequests;
import viola1.agrovc.com.tonguefinal.data.network.api.APIService;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.LocalRetrofitApi;
import viola1.agrovc.com.tonguefinal.helper.SessionManager;
import viola1.agrovc.com.tonguefinal.presentation.adapters.PendingRequestsRecyclerViewAdapter;
import viola1.agrovc.com.tonguefinal.presentation.ui.activities.TutorshipRequestsActivity;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnConfirmedRequestListInteractionListener}
 * interface.
 */
public class UserConfirmedRequestsListFragment extends Fragment implements
        PendingRequestsRecyclerViewAdapter.PendingRequestListenerAdapter {
    private static final String TAG = UserConfirmedRequestsListFragment.class.getSimpleName();

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private SessionManager sessionManager;
    private PendingRequestsRecyclerViewAdapter pendingRequestsRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private OnConfirmedRequestListInteractionListener mListener;
    private List<PendingRequest> pendingRequestList = new ArrayList<>();
    private int user_id, modified_by, actionId;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserConfirmedRequestsListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static UserConfirmedRequestsListFragment newInstance(int columnCount) {
        UserConfirmedRequestsListFragment fragment = new UserConfirmedRequestsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get the action id so that we can know which list to pull from the db
        //either pending requests for the user or for the tutor
        if (getArguments() != null) {
            actionId = getArguments().getInt("actionId");
        }

        // Session manager
        sessionManager = new SessionManager(getActivity());

        //get user id from shared prefs
        user_id = sessionManager.getUserId();

        /*if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_requests_list, container, false);

        //initialize the view widgets
        getAllWidgets(view);

        //set up the list adapter
        setupListAdapter();

        //first check if we have an internet connection
        if (isNetworkAvailable()) {
                getTutorPendingRequests(sessionManager.getUserId());
        }else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

        return view;
    }

    public void getAllWidgets(View view) {
        recyclerView = view.findViewById(R.id.pendingRequestListRecyclerView);
    }

    private void setupListAdapter() {
        pendingRequestsRecyclerViewAdapter = new PendingRequestsRecyclerViewAdapter(TutorshipRequestsActivity.instance,
                pendingRequestList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TutorshipRequestsActivity.instance);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(TutorshipRequestsActivity.instance, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(pendingRequestsRecyclerViewAdapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnConfirmedRequestListInteractionListener) {
            mListener = (OnConfirmedRequestListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnConfirmedRequestListInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnConfirmedRequestListInteractionListener {
        // get the id of the scheduled meeting
        void onConfirmedListFragmentInteraction(int meetingId, String tutorRequesterName);
    }

    @Override
    public void onPendingRequestRowClicked(int position){
        PendingRequest pendingRequest = pendingRequestList.get(position);
        Log.d(TAG,"user ID for requester = "+ pendingRequest.getUser_id());
        mListener.onConfirmedListFragmentInteraction(pendingRequest.getMeeting_id(), pendingRequest.getName());
    }

    //retrofit call to get all pending requests for the tutor
    private void getTutorPendingRequests(int user_id){

        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        //this method will be running on UI thread
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();

        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<UserPendingRequests> call = service.getUserConfirmedRequests(user_id);

        //calling the com.emtech.retrofitexample.api
        call.enqueue(new Callback<UserPendingRequests>() {
            @Override
            public void onResponse(Call<UserPendingRequests> call, Response<UserPendingRequests> response) {
                if(response.code() == AppNums.STATUS_COD_SUCCESS) {
                    pdLoading.dismiss();

                    if (response.body().getUser_confirmed_requests().size() > 0){

                        //clear the previous list if it has content
                        if (pendingRequestList != null) {
                            pendingRequestList.clear();
                        }

                        for (int i = 0; i < response.body().getUser_confirmed_requests().size(); i++) {

                            PendingRequest pendingRequest = new PendingRequest();
                            pendingRequest.setMeeting_id(response.body().getUser_confirmed_requests().get(i).getMeeting_id());
                            pendingRequest.setUser_id(response.body().getUser_confirmed_requests().get(i).getUser_id());
                            pendingRequest.setTutor_id(response.body().getUser_confirmed_requests().get(i).getTutor_id());
                            pendingRequest.setLanguage_name(response.body().getUser_confirmed_requests().get(i).getLanguage_name());
                            pendingRequest.setMeeting_date(response.body().getUser_confirmed_requests().get(i).getMeeting_date());
                            pendingRequest.setMeeting_time(response.body().getUser_confirmed_requests().get(i).getMeeting_time());
                            pendingRequest.setMeeting_location(response.body().getUser_confirmed_requests().get(i).getMeeting_location());
                            pendingRequest.setCreated_on(response.body().getUser_confirmed_requests().get(i).getCreated_on());
                            pendingRequest.setLast_modified_by(response.body().getUser_confirmed_requests().get(i).getLast_modified_by());
                            pendingRequest.setName(response.body().getUser_confirmed_requests().get(i).getName());
                            pendingRequest.setProfile_pic(response.body().getUser_confirmed_requests().get(i).getProfile_pic());

                            modified_by = response.body().getUser_confirmed_requests().get(i).getLast_modified_by();


                            pendingRequest.setColor(getRandomMaterialColor("400"));

                            pendingRequestList.add(pendingRequest);
                            Log.d(TAG,"confirmed request user ID = "+ response.body().getUser_confirmed_requests().get(i).getUser_id());
                        }

                        pendingRequestsRecyclerViewAdapter.notifyDataSetChanged();

                    }else if (response.body().getUser_confirmed_requests().size() == 0){

                        Toast.makeText(getActivity(), "No confirmed requests at the moment", Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<UserPendingRequests> call, Throwable t) {
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
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", TutorshipRequestsActivity.instance.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }

    //method to check for internet connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
