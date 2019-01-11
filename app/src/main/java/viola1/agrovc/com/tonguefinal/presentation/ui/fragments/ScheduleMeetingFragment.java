package viola1.agrovc.com.tonguefinal.presentation.ui.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.data.network.Result;
import viola1.agrovc.com.tonguefinal.data.network.api.APIService;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.LocalRetrofitApi;
import viola1.agrovc.com.tonguefinal.helper.SessionManager;
import viola1.agrovc.com.tonguefinal.presentation.ui.activities.LoginActivity;
import viola1.agrovc.com.tonguefinal.presentation.ui.activities.RegisterActivity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduleMeetingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduleMeetingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleMeetingFragment extends DialogFragment {
    private static final String SCHEDULE_MEETING_FRAGMENT_TAG = "schedule_meeting_fragment_dialog";
    private static final String TAG = ScheduleMeetingFragment.class.getSimpleName();
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TUTOR_NAME = "tutorName";
    private static final String TUTOR_ID = "tutorId";
    private static final String LANGUAGE_NAME = "languageName";
    private SessionManager sessionManager;
    private TextView meetingTime, meetingDate;
    private Button buttonSetMeetingSchedule, buttonMeetingTime, buttonMeetingdate;
    private EditText meetingLocation;
    private Dialog dateDialog, timeDialog;
    private ProgressDialog pDialog;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "kk:mm";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";

    private String mTutorName, mLanguageName;
    private int user_id, tutor_id;

    private OnFragmentInteractionListener mListener;

    public ScheduleMeetingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tutorName Parameter 1.
     * @return A new instance of fragment ScheduleMeetingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleMeetingFragment newInstance(int tutorId, String tutorName, String languageName) {
        ScheduleMeetingFragment fragment = new ScheduleMeetingFragment();
        Bundle args = new Bundle();
        args.putString(TUTOR_NAME, tutorName);
        args.putInt(TUTOR_ID, tutorId);
        args.putString(LANGUAGE_NAME, languageName);
        //fragment.setCancelable(false);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTutorName = getArguments().getString(TUTOR_NAME);
            tutor_id = getArguments().getInt(TUTOR_ID);
            mLanguageName = getArguments().getString(LANGUAGE_NAME);
        }
        // Session manager
        sessionManager = new SessionManager(getActivity());

        //get user id from shared prefs
        user_id = sessionManager.getUserId();


        //setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_schedule_meeting, container, false);

        //initialise the view widgets
        getAllWidgets(view);
        setMeetingSchedule();

        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onPendingRequestInteraction(uri);
        }
    }*/

    private void getAllWidgets(View view){
        meetingDate = view.findViewById(R.id.meeting_date);
        meetingTime = view.findViewById(R.id.meeting_time);
        meetingLocation = view.findViewById(R.id.edit_text_meeting_location);
        buttonMeetingdate = view.findViewById(R.id.button_meeting_date);
        buttonMeetingTime = view.findViewById(R.id.button_meeting_time);
        buttonSetMeetingSchedule = view.findViewById(R.id.set_meeting_schedule);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String meetingDate, String meetingTime);
    }

    //method to handle clicks on the buttons
    private void setMeetingSchedule(){

        //meeting time
        buttonMeetingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMeetingTime();

            }
        });

        //meeting date
        buttonMeetingdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMeetingDate();

            }
        });

        //meeting location
        meetingLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMeetingLocation();

            }
        });

        //meeting schedule
        buttonSetMeetingSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = meetingDate.getText().toString();
                if (TextUtils.isEmpty(date)){
                    meetingDate.setError("Date is required");
                }

                String time = meetingTime.getText().toString();
                if (TextUtils.isEmpty(time)){
                    meetingTime.setError("Time is required");
                }

                String location = meetingLocation.getText().toString();
                if (TextUtils.isEmpty(time)){
                    meetingLocation.setError("A meeting location is required");
                }

                if (mListener != null) {
                    mListener.onFragmentInteraction(date, time);
                }

                createMeetingSchedule(user_id, tutor_id, mLanguageName, date, time, location, user_id);

                /*Fragment prev = getFragmentManager().findFragmentByTag("schedule_meeting_fragment_dialog");
                if (prev != null){
                    DialogFragment dialogFragment = (DialogFragment) prev;
                    dialogFragment.dismiss();
                }*/

            }
        });
    }

    //handles user selected date from popup dialog
    private void setMeetingDate(){
        dateDialog = new Dialog(getActivity());
        dateDialog.setContentView(R.layout.date_picker);

        //find our picker
        datePicker = dateDialog.findViewById(R.id.datePicker);

        //set past dates disabled
        datePicker.setMinDate(System.currentTimeMillis() - 1000);

        //find our buttons
        Button setDate = dateDialog.findViewById(R.id.button_picker_set_date);
        Button cancelDateDialog = dateDialog.findViewById(R.id.button_picker_cancel_date);

        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the date the user sets
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                //create a new calendar set to the date chosen
                Calendar calendar = Calendar.getInstance();
                //calendar.set(year,month,day,hour,minute);
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);

                System.out.println("Current time => " + calendar.getTime());
                SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
                String date = df.format(calendar.getTime());

                meetingDate.setText(date);

                dateDialog.dismiss();

            }
        });

        cancelDateDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateDialog.dismiss();
            }
        });

        dateDialog.show();
    }

    //handles user set time from popup dialog
    private void setMeetingTime(){
        timeDialog = new Dialog(getActivity());
        timeDialog.setContentView(R.layout.time_picker);

        //find our picker
        timePicker = timeDialog.findViewById(R.id.timePicker);

        //find our buttons
        Button setTime = timeDialog.findViewById(R.id.button_picker_set_time);
        Button cancelTimeDialog = timeDialog.findViewById(R.id.button_time_picker_cancel);

        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the time the user sets
                int hour;
                int minute;

                if (Build.VERSION.SDK_INT >= 23){
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                }else{
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }

                //create a new calendar set to the date chosen
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,hour);
                calendar.set(Calendar.MINUTE, minute);

                System.out.println("Current time => " + calendar.getTime());
                SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
                String time = df.format(calendar.getTime());

                meetingTime.setText(time);

                timeDialog.dismiss();
            }
        });

        cancelTimeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeDialog.dismiss();
            }
        });

        timeDialog.show();
    }

    //handles setting meeting location using google maps search places
    private void setMeetingLocation(){
        try{
            Intent intent = new PlaceAutocomplete
                    .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(getActivity());
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e){
            Log.e(TAG, e.getMessage());
        } catch (GooglePlayServicesNotAvailableException e){
            Log.e(TAG, "GooglePlayServicesNotAvailableException error");
            Log.e(TAG, e.getMessage());
        }

    }

    //to recieve notification when a user has selected a place, we override
    //the activity's onActivityResult(), checking for the request code
    //you have passed for your intent
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i(TAG, "Place: " + place.getName());
                meetingLocation.setText(place.getName());
            }else if (resultCode == PlaceAutocomplete.RESULT_ERROR){
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                //handle error
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED){
                //the user cancelled the operation
            }
        }
    }


    //retrofit call to post meeting schedule details to db
    private void createMeetingSchedule(final int user_id, final int tutor_id, final String language_name,
                                       final String meeting_date, final String meeting_time,
                                       final String meeting_location, int modified_by) {
        //pass in the language id/ name the tutorship is wanted for

        pDialog.setMessage("Working on it ...");
        showDialog();

        //Defining retrofit api service*/
        //APIService service = retrofit.create(APIService.class);
        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<Result> call = service.createMeetingSchedule(user_id, tutor_id, language_name,
                meeting_date, meeting_time, meeting_location, modified_by);

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
                    showAlertDialog(3,"Meeting schedule set","Meeting schedule set. " +
                            "Awaiting confirmation from "+mTutorName);

                    MDToast mdToast = MDToast.makeText(getActivity(),
                            "Meeting schedule set. Awaiting confirmation from tutor", Toast.LENGTH_LONG, MDToast.TYPE_SUCCESS);

                    mdToast.show();

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

        DialogFragment newFragment = MeetingScheduleAlertDialog.newInstance(action, title, message);
        newFragment.show(getFragmentManager(), "meeting_alert_dialog");
    }
}
