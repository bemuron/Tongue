package viola1.agrovc.com.tonguefinal.presentation.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.data.network.Result;
import viola1.agrovc.com.tonguefinal.data.network.api.APIService;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.LocalRetrofitApi;
import viola1.agrovc.com.tonguefinal.helper.SessionManager;
import viola1.agrovc.com.tonguefinal.models.User;
import viola1.agrovc.com.tonguefinal.presentation.viewmodels.UserProfileActivityViewModel;
import viola1.agrovc.com.tonguefinal.presentation.viewmodels.UserProfileActivityViewModelFactory;
import viola1.agrovc.com.tonguefinal.utilities.InjectorUtils;

public class EditUserProfileActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG = UserProfileActivity.class.getSimpleName();
    private UserProfileActivityViewModel userProfileActivityViewModel;
    private final static int WRITE_EXTERNAL_RESULT = 100;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 55;
    private static final int SELECT_IMAGE_REQUEST_CODE =25 ;
    private List<User> mUserDetails;
    private ImageView mUserProfilePic;
    private EditText mUserName, mUserDob, mUserGender,
            mUserPhoneNumber, mUserAbout, mUserRole;
    private DatePicker datePicker;
    private Dialog dateDialog;
    private ProgressDialog pDialog;
    private TypedArray gender;
    private TextView mUserEmail;
    private RadioGroup genderRadioGroup;
    private RadioButton genderRadioButton;
    private int mUserId;
    private String dobForMysql, genderSelected, userName, mediaPath;
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DOB = "dd MMM yyyy";
    private Button setProfileImage, saveProfile;
    private Bitmap bitmap;
    private User mTongueUser;
    private SessionManager session;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        mUserProfilePic = findViewById(R.id.editProfileImageView);//
        mUserName = findViewById(R.id.userProfileNameEv);
        mUserDob = findViewById(R.id.userProfileDobEv);
        genderRadioGroup = findViewById(R.id.editProfileGenderEv);
        setProfileImage = findViewById(R.id.buttonSetImage);
        saveProfile = findViewById(R.id.editProfileSaveButton);
        //mUserPhoneNumber = findViewById(R.id.userProfileNumberEv);//
        mUserAbout = findViewById(R.id.userProfileAboutEv);
        //mUserEmail = findViewById(R.id.userProfileEmailEv);//
        gender = getResources().obtainTypedArray(R.array.gender);
        //mUserRole = findViewById(R.id.userProfileRoleEv);
        //mBecomeTutor = findViewById(R.id.userProfileBecomeTutorButton);

        //mBecomeTutor.setVisibility(View.INVISIBLE);

        UserProfileActivityViewModelFactory factory = InjectorUtils.provideUserProfileViewModelFactory(this.getApplicationContext());
        userProfileActivityViewModel = ViewModelProviders.of
                (this, factory).get(UserProfileActivityViewModel.class);

        userProfileActivityViewModel.getUserDetails().observe(this, user -> {

            mUserDetails = user;

            populateViews(user);

            Log.i(TAG, "User name "+user.get(0).getName());

        });

        /*fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                String userName = mUserName.getText().toString().trim();
                String aboutUser = mUserAbout.getText().toString().trim();
                String date_of_birth = mUserDob.getText().toString().trim();

                try {
                    if (!userName.isEmpty() && !aboutUser.isEmpty() &&
                            !date_of_birth.isEmpty() &&
                            !dobForMysql.isEmpty() &&
                            !genderSelected.isEmpty()){

                        updateUserData(userName,aboutUser,dobForMysql,genderSelected);
                    }
                }catch (NullPointerException e){
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }
                *//*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*//*
            }
        });*/

        handleProfilePicClick();
        handleSetImage();
        onDobClick();
        setUpGenderRadios();
        handleSaveProfile();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //populate the different views with the user data
    private void populateViews(List<User> user){
        mUserId = user.get(0).getUser_id();
        mUserName.setText(user.get(0).getName());
        userName = user.get(0).getName();
        mUserDob.setText(user.get(0).getDate_of_birth());
        //mUserGender.setText(user.get(0).getGender());
        //mUserPhoneNumber.setText(user.get(0).getPhone_number());
        //set user profile pic
        try {
            if (!TextUtils.isEmpty(user.get(0).getProfile_pic())) {
                Glide.with(EditUserProfileActivity.this)
                        .load("http://apps.incubation.billbrain.tech/tongueApp/assets/images/profile_pics/" + user.get(0).getProfile_pic())
                        .thumbnail(0.5f)
                        .into(mUserProfilePic);
                mUserProfilePic.setColorFilter(null);
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

        mUserAbout.setText(user.get(0).getDescription());

        //mUserEmail.setText(user.get(0).getEmail());
        //mUserRole.setText(user.get(0).getRole());


    }

    //handles click on profile pic image view
    private void handleProfilePicClick(){
        mUserProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checking if we have been given the permission
                boolean result = checkAndRequestPermissions();
                if (result) {
                    selectProfileImage();
                    setProfileImage.setEnabled(true);
                }
            }
        });
    }

    //handles user selection of image
    private void selectProfileImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE);

    }

    //get the language the use has picked
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //check for our request code
        if (requestCode == SELECT_IMAGE_REQUEST_CODE){
            //check if the request was successful
            if (resultCode == RESULT_OK && data != null){
                //Uri path = data.getData();

                try {
                    Uri path = data.getData();
                    String[] filePathcolumn = {MediaStore.Images.Media.DATA};
                    //MediaStore.Images.Media.DATA
                    //MediaStore.Images.ImageColumns.DATA

                    Cursor cursor = EditUserProfileActivity.this.getContentResolver().query(path,
                            null, null, null, null);
                    if (cursor == null) {
                        mediaPath = path.getPath();
                    }else {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        mediaPath = cursor.getString(columnIndex);
                    }
                    //get the image from the gallery
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                    //set the image to our image view
                    mUserProfilePic.setImageBitmap(bitmap);

                    cursor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    //this method compresses the image and decodes it
    //usinf this method means that the image is uploaded as a string and therefore at your webservice
    //you have to decode it inorder to retrieve the image then you can save it
    private  String imageToString(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        //compress bitmap to jpeg format
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        //convert the bytearrayoutputstream to an array of bytes
        byte[] imageByte = byteArrayOutputStream.toByteArray();

        //encode the byte array, return the string
        return android.util.Base64.encodeToString(imageByte, android.util.Base64.DEFAULT);
    }

    //handle button click on setImage button to call method to
    //upload image
    private void handleSetImage(){
        setProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProfilePic();
            }
        });
    }

    //handle button click to save rest of the profile
    private void handleSaveProfile(){
        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = mUserName.getText().toString().trim();
                String aboutUser = mUserAbout.getText().toString().trim();
                String date_of_birth = mUserDob.getText().toString().trim();

                try {
                    if (dobForMysql == null && mUserDob.getText() != null){
                        dobForMysql = mUserDob.getText().toString().trim();
                    }else if(dobForMysql == null && mUserDob.getText() == null){
                        Toast.makeText(EditUserProfileActivity.this, "Please enter your date of birth", Toast.LENGTH_LONG).show();
                    }

                    if (!userName.isEmpty() && !aboutUser.isEmpty() &&
                            !date_of_birth.isEmpty() &&
                            !dobForMysql.isEmpty() &&
                            !genderSelected.isEmpty()){

                        updateUserData(userName,aboutUser,dobForMysql,genderSelected);
                    }
                }catch (NullPointerException e){
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    //set up the radio buttons for the user gender
    private void setUpGenderRadios(){
        genderRadioGroup.setOnCheckedChangeListener(this);
        genderRadioGroup.removeAllViews();
        //genderRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < gender.length(); i++) {
            if (gender != null) {
                genderRadioButton = new RadioButton(this);
                //opt_1.setGravity(Gravity.CENTER);
                // opt_1.setButtonDrawable(android.R.color.transparent);
                genderRadioButton.setText(gender.getText(i));
                genderRadioButton.setPadding(5, 0, 5, 0);
                genderRadioButton.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                genderRadioButton.setTextSize(18);
                genderRadioGroup.addView(genderRadioButton);
            }
        }
    }

    //handles click on the dobEditText field
    private void onDobClick(){
        //to handle click on dobEditText edit text field
        //shows date picker dialog for user to select their dobEditText
        mUserDob.setInputType(InputType.TYPE_NULL);
        mUserDob.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                getUserDateOfBirth();
            }
        });

        mUserDob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getUserDateOfBirth();
                }
            }
        });
    }

    //handles user selected date of birth from popup dialog
    private void getUserDateOfBirth(){
        dateDialog = new Dialog(this);
        dateDialog.setContentView(R.layout.registration_dob_picker);

        //find our picker
        datePicker = dateDialog.findViewById(R.id.dobDatePicker);

        //find our buttons
        Button setDate = dateDialog.findViewById(R.id.button_picker_set_dob);

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
                SimpleDateFormat df = new SimpleDateFormat(DOB, Locale.getDefault());
                String date = df.format(calendar.getTime());

                //display the date on the edit text
                mUserDob.setText(date);

                //the date format expected by mysql
                SimpleDateFormat dobf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
                dobForMysql = dobf.format(calendar.getTime());

                dateDialog.dismiss();

            }
        });

        dateDialog.show();
    }

    //handle the radio button selected
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        int radioButtonId = genderRadioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = genderRadioGroup.findViewById(radioButtonId);
        //oSelectedCount++;
        genderSelected = (String) radioButton.getText();
        Log.i(TAG, "radio selected "+ genderSelected);
    }

    //retrofit call to update user details on server
    private void updateUserData(final String userName, final String aboutUser,
                                final String dob, final String gender){
        fab.setEnabled(false);

        pDialog.setMessage("Updating ...");
        showDialog();

        //Defining retrofit api service*/
        //APIService service = retrofit.create(APIService.class);
        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<Result> call = service.updateUser(mUserId,userName,aboutUser,dob,gender);

        //calling the com.emtech.retrofitexample.api
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                hideDialog();

                if (!response.body().getError()) {
                    hideDialog();
                    //Log.d(LOG_TAG, EnumAppMessages.REGISTER_SUCCESS_TITLE.getValue());
                    Log.d(TAG, response.body().getMessage());
                    Toast.makeText(EditUserProfileActivity.this,
                            response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    //create new user object
                    mTongueUser = new User();
                    mTongueUser.setUser_id(response.body().getUser().getUser_id());
                    //mTongueUser.setEmail(response.body().getUser().getEmail());
                    //mTongueUser.setCreated_on(response.body().getUser().getCreated_on());
                    //mTongueUser.setRole(response.body().getUser().getRole());
                    mTongueUser.setDescription(response.body().getUser().getDescription());
                    //mTongueUser.setPhone_number(response.body().getUser().getPhone_number());
                    //mTongueUser.setProfile_pic(response.body().getUser().getProfile_pic());
                    mTongueUser.setDate_of_birth(response.body().getUser().getDate_of_birth());
                    mTongueUser.setGender(response.body().getUser().getGender());
                    mTongueUser.setName(response.body().getUser().getName());
                    //mTongueUser.setPassword(response.body().getUser().getPassword());

                    //update user details in local db
                    userProfileActivityViewModel.updateProfile(mTongueUser.getName(),
                            mTongueUser.getDescription(),mTongueUser.getDate_of_birth(), mTongueUser.getGender(),
                            mTongueUser.getUser_id());

                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                hideDialog();
                //print out any error we may get
                //probably server connection
                Log.e(TAG, t.getMessage());
                Toast.makeText(EditUserProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                fab.setEnabled(true);
            }
        });

    }

    //retrofit call to upload the profile image
    private void uploadProfilePic(){

        //String image = imageToString();
        //String imageName = userName;

        pDialog.setMessage("Uploading profile pic ...");
        showDialog();
        setProfileImage.setEnabled(false);

        //Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(mediaPath);

        //parsing any media file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*image/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody fileName = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        //Defining retrofit api service*/
        //APIService service = retrofit.create(APIService.class);
        APIService service = new LocalRetrofitApi().getRetrofitService();

        //defining the call
        Call<Result> call = service.uploadUserProfilePic(mUserId, fileToUpload, fileName);

        //calling the com.emtech.retrofitexample.api
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                hideDialog();

                if (!response.body().getError()) {
                    Toast.makeText(EditUserProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    setProfileImage.setEnabled(false);

                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                hideDialog();
                //print out any error we may get
                //probably server connection
                Log.e(TAG, t.getMessage());
                Toast.makeText(EditUserProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                setProfileImage.setEnabled(true);
            }
        });
    }

    //check if we have permission to write external storage
    private boolean checkAndRequestPermissions() {

        //checking for marshmallow devices and above in order to execute runtime
        //permissions
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            int permisionWriteExternalStorage = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int permissionReadExternalStorage = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            //declare a list to hold the permissions we want to ask the user for
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (permisionWriteExternalStorage != PackageManager.PERMISSION_GRANTED){
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (permissionReadExternalStorage != PackageManager.PERMISSION_GRANTED){
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            //if the permissions list is not empty, then request for the permission
            if (!listPermissionsNeeded.isEmpty()){
                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                        (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            }else {
                return true;
            }
        }else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called ----");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                int currentAPIVersion = Build.VERSION.SDK_INT;
                Map<String, Integer> perms = new HashMap<>();
                if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
                    //initialize the map with both permissions
                    perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                    perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                }

                //fill with actual results from the user
                if (grantResults.length > 0){
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    //check for both permissions
                    if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_GRANTED){
                        Log.d(TAG, "Write and Read external storage permissions granted");
                        selectProfileImage();
                    } else {
                        Log.d(TAG, "Some permissions are not granted, ask again");
                        //permission is denied (this is the first time, when "never ask again" is not checked)
                        //so ask again explaining the use of the permissions
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            new AlertDialog.Builder(this)
                                    .setTitle("Permission Request")
                                    .setMessage("Permission is required for the app to write and read from storage")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ActivityCompat.requestPermissions(EditUserProfileActivity.this,
                                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    WRITE_EXTERNAL_RESULT);
                                        }
                                    })
                                    .show();
                        }
                        //permission is denied and never ask again is checked
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }
                break;
            }
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);
        session.logoutUser();
        userProfileActivityViewModel.delete();
    }

}
