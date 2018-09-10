package viola1.agrovc.com.tonguefinal.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.BootstrapProgressBar;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.app.localserver.request.ReqBeanMaid;
import viola1.agrovc.com.tonguefinal.app.localserver.request.response.ResBeanMaid;
import viola1.agrovc.com.tonguefinal.constants.AppErrors;
import viola1.agrovc.com.tonguefinal.constants.AppNums;
import viola1.agrovc.com.tonguefinal.constants.AppPrefs;
import viola1.agrovc.com.tonguefinal.constants.EnumAppMessages;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.LocalRetrofitApi;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.RetrofitService;
import viola1.agrovc.com.tonguefinal.presentation.ui.fragments.DatePickerFragment;
import viola1.agrovc.com.tonguefinal.helper.Encryption;
import viola1.agrovc.com.tonguefinal.helper.GeneralMethods;
import viola1.agrovc.com.tonguefinal.helper.InputValidator;

public class MaidActivity extends AppCompatActivity  implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener {
    /**   private Button email;
     private Button sms;
     private Button watsapp;
     private Button call;**/
    Spinner spinner_country;



    Spinner spinner_user_type;



    BootstrapEditText description;
    Spinner spinner_sex;
    Spinner spinner_level;
    BootstrapEditText edtFirstname;
    BootstrapEditText edtLastname;
    BootstrapEditText edtContact;
    BootstrapEditText edtEmail;
    BootstrapEditText nin_number;
    BootstrapEditText district;
    BootstrapEditText address;
    BootstrapEditText language;
    BootstrapEditText next;
    BootstrapEditText nextContact;
    BootstrapEditText nextAddress;
    BootstrapEditText nextRelationship;

    private View focusView;
    TextView txtWPickDate;
    BootstrapEditText edtWDate;
    BootstrapButton btnSignup;
    ImageView buttonUpload;
    BootstrapButton submit;
    private Spinner countryCodeSpinner;
    private File profileImage;
    NumberPicker np;
    private BootstrapProgressBar progressBar;

    public String[] country = {
            "",
            "Uganda",
            "Kenya",
            "Tanzania",
            "Rwanda",
            "Burundi",
    };
    public String[] gender = {
            "",
            "Male",
            "Female",

    };

    public String[] level = {
            "",
            "Masters",
            "Degree",
            "Diploma",
            "Certificate",
            "A level",
            "O level",
            "Primary",
            "None"




    };




 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maid);

        initialize();
        np = findViewById(R.id.numberPicker);
        np.setMinValue(0);
        np.setMaxValue(20);


        np.setOnValueChangedListener(onValueChangeListener);
     if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Maid");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_navigation_arrow_back));

        toolbar.setTitle("Maid");


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    private void initialize() {

        edtFirstname= (BootstrapEditText) findViewById(R.id.edtFirstname);
        edtLastname= (BootstrapEditText) findViewById(R.id.edtLastname);
        edtContact= (BootstrapEditText) findViewById(R.id.edtContact);

        edtEmail= (BootstrapEditText) findViewById(R.id.edtEmail);
        nextAddress= (BootstrapEditText) findViewById(R.id.nextaddress);
        nextRelationship= (BootstrapEditText) findViewById(R.id.nextrelationship);
        nextContact= (BootstrapEditText) findViewById(R.id.nextContact);

        next= (BootstrapEditText) findViewById(R.id.next);
        language= (BootstrapEditText) findViewById(R.id.language);
        district= (BootstrapEditText) findViewById(R.id.district);

        address= (BootstrapEditText) findViewById(R.id.address);
        nin_number= (BootstrapEditText) findViewById(R.id.nin_number);

        description= (BootstrapEditText) findViewById(R.id.description);
        edtWDate= (BootstrapEditText) findViewById(R.id.edtWDate);
        txtWPickDate= (TextView) findViewById(R.id.txtWPickDate);
        progressBar = (BootstrapProgressBar) findViewById(R.id.progressBar1);

        spinner_country= (Spinner) findViewById(R.id.spinner_country);
        spinner_sex= (Spinner) findViewById(R.id.spinner_sex);
        spinner_sex.setPrompt("Select Gender");
        spinner_level= (Spinner) findViewById(R.id.spinner_level);


        // ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.sex, R.layout.spinner_item);

        ///  ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.role, R.layout.spinner_item);
        //  adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        // spinner_user_type.setAdapter(adapter2);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, gender){
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View v = null;

                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                }
                else {

                    v = super.getDropDownView(position, null, parent);
                }

                parent.setVerticalScrollBarEnabled(false);
                return v;
            }
        };
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner_sex.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, country){
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View v = null;

                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                }
                else {

                    v = super.getDropDownView(position, null, parent);
                }

                parent.setVerticalScrollBarEnabled(false);
                return v;
            }
        };
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner_country.setAdapter(adapter2);

        ArrayAdapter<String> adapterlevel = new ArrayAdapter<String>(this,
                R.layout.spinner_item, level){
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View v = null;

                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                }
                else {

                    v = super.getDropDownView(position, null, parent);
                }

                parent.setVerticalScrollBarEnabled(false);
                return v;
            }
        };
        adapterlevel.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner_level.setAdapter(adapterlevel);
        spinner_level.setPrompt("Select Level");

        submit = (BootstrapButton) findViewById(R.id.submit);


    }





    public void signUpOnClick(View view){
        progressBar.setVisibility(View.VISIBLE);
        if(isValidUserInput()){
            progressBar.setVisibility(View.VISIBLE);
            setProgressBarIndeterminateVisibility(true);
            String Firstname = edtFirstname.getText().toString();

            String Lastname = edtLastname.getText().toString();
            String Email = edtEmail.getText().toString();
            String NextAddress = nextAddress.getText().toString();
            String NextRelationship = nextRelationship.getText().toString();
            String  NextContact =  nextContact.getText().toString();

            String Next = next.getText().toString();
            String Language = language.getText().toString();
            String Address = address.getText().toString();

            String Description = description.getText().toString();
            String District = district.getText().toString();
            String Enin_number = nin_number.getText().toString();
            String Contact = edtContact.getText().toString();
            String Date = edtWDate.getText().toString();

            int mnp = np.getValue();

            String mspinner_country = spinner_country.getSelectedItem().toString();
            String mspinner_sex = spinner_sex.getSelectedItem().toString();
            String mspinner_level = spinner_level.getSelectedItem().toString();


            ReqBeanMaid reqBean = new ReqBeanMaid();
            reqBean.setFirstname(Encryption.encrypt(Firstname));
            reqBean.setLastname(Encryption.encrypt(Lastname));
            reqBean.setNext(Encryption.encrypt(Next));
            reqBean.setLanguage(Encryption.encrypt(Language));
            reqBean.setNextcontact(Encryption.encrypt(NextContact));
            reqBean.setNextaddress(Encryption.encrypt(NextAddress));
            reqBean.setDescription(Encryption.encrypt(Description));
            reqBean.setNextrelationship(Encryption.encrypt(NextRelationship));
            reqBean.setAddress(Encryption.encrypt(Address));
            reqBean.setDistrict(Encryption.encrypt(District));
            reqBean.setyears(Encryption.encrypt2(mnp));

            reqBean.setNIN(Encryption.encrypt( Enin_number));
            reqBean.setEmail(Encryption.encrypt(Email));

            reqBean.setContact(Encryption.encrypt(Contact));
            reqBean.setCountry(Encryption.encrypt(mspinner_country));
            reqBean.setDate(Encryption.encrypt(Date));
            reqBean.setGender(Encryption.encrypt(mspinner_sex));
            reqBean.setLevel(Encryption.encrypt(mspinner_level));

            new DataLoaderMaid(MaidActivity.this).userSignUp(reqBean);

            //  Toast.makeText(this,"Correct input",Toast.LENGTH_SHORT).show();


            MDToast mdToast = MDToast.makeText(this, "Correct input",Toast.LENGTH_SHORT,MDToast.TYPE_SUCCESS);

            mdToast.show();
            progressBar.setVisibility(View.VISIBLE);
        }else{
            focusView.requestFocus();
            progressBar.setVisibility(View.GONE);
        }

    }

    /* Validates user input data*/
    private boolean isValidUserInput() {

      focusView = null;

        InputValidator validator = new InputValidator();

        String email = edtEmail.getText().toString();




        if(!validator.isEmailValid(email)){
            focusView = edtEmail;
            // edtEmail.setError(AppErrors.INVALID_EMAIL);
            MDToast mdToast = MDToast.makeText(MaidActivity.this, AppErrors.INVALID_EMAIL,Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            return false;
        }
        if(edtFirstname.getText().toString().isEmpty()){
            focusView = edtFirstname;
            MDToast mdToast = MDToast.makeText(MaidActivity.this, "First Name is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }
        if(edtLastname.getText().toString().isEmpty()){
            focusView = edtLastname;
            MDToast mdToast = MDToast.makeText(MaidActivity.this, "Last Name is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }


        if( edtContact.getText().toString().isEmpty()){
            focusView =  edtContact;
            MDToast mdToast = MDToast.makeText(MaidActivity.this, "Contact is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }

        if( edtContact.getText().toString().isEmpty()){
            focusView =  edtContact;
            MDToast mdToast = MDToast.makeText(MaidActivity.this, "Contact is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }


        if( edtEmail.getText().toString().isEmpty()){
            focusView = edtEmail;
            MDToast mdToast = MDToast.makeText(MaidActivity.this, "Email is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }

        if( nextAddress.getText().toString().isEmpty()){
            focusView = nextAddress;
            MDToast mdToast = MDToast.makeText(MaidActivity.this, "Next of kin's address is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }

        if(nextRelationship.getText().toString().isEmpty()){
            focusView = nextRelationship;
            MDToast mdToast = MDToast.makeText(MaidActivity.this, "Next of kin's relationship is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }

        if(nextContact.getText().toString().isEmpty()){
            focusView = nextContact;
            MDToast mdToast = MDToast.makeText(MaidActivity.this, "Next of kin's relationship is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }

        if(next.getText().toString().isEmpty()){
            focusView = next;
            MDToast mdToast = MDToast.makeText(MaidActivity.this, "Next of kin is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }
        if(language.getText().toString().isEmpty()){
            focusView = language;
            MDToast mdToast = MDToast.makeText(MaidActivity.this, "language is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }

        if(district.getText().toString().isEmpty()){
            focusView = district;
            MDToast mdToast = MDToast.makeText(MaidActivity.this, "District is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }


        if(address.getText().toString().isEmpty()){
            focusView = address;
            MDToast mdToast = MDToast.makeText(MaidActivity.this, "Address is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }
        if(description.getText().toString().isEmpty()){
            focusView = description;
            MDToast mdToast = MDToast.makeText(MaidActivity.this, "Add your description",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }
        if(nin_number.getText().toString().isEmpty()){
            focusView = nin_number;
            MDToast mdToast = MDToast.makeText(MaidActivity.this, "NIN number is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }

        if(edtWDate.getText().toString().isEmpty()){
            focusView = edtWDate;
            MDToast mdToast = MDToast.makeText(MaidActivity.this, "This date is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }

        if( spinner_country.getSelectedItem().toString()==""){
            focusView = spinner_country;
            MDToast mdToast = MDToast.makeText(MaidActivity.this,"Select country",MDToast.TYPE_ERROR);

            mdToast.show();
            // edtContact.setError(AppErrors.INVALID_CONTACT);
            return false;
        }
        if( spinner_sex.getSelectedItem().toString()==""){
            focusView = spinner_sex;
            MDToast mdToast = MDToast.makeText(MaidActivity.this,"Select country",MDToast.TYPE_ERROR);

            mdToast.show();
            // edtContact.setError(AppErrors.INVALID_CONTACT);
            return false;
        }
        if(  spinner_level.getSelectedItem().toString()==""){
            focusView =  spinner_level;
            MDToast mdToast = MDToast.makeText(MaidActivity.this,"Select country",MDToast.TYPE_ERROR);

            mdToast.show();
            // edtContact.setError(AppErrors.INVALID_CONTACT);
            return false;
        }



        return true;

    }










    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
    private void showAppCloseDialog() {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(Home.this,R.style.StyledDialog);*/
        AlertDialog.Builder builder = new AlertDialog.Builder(MaidActivity.this);
        builder.setMessage(R.string.app_close_prompt);

        builder.setNegativeButton(R.string.logout,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AppPrefs.clearLoginCredentials(MaidActivity.this);
                        startActivity(new Intent(MaidActivity.this,Login.class));
                        finish();
                    }
                });
        builder.setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                    System.exit(0);
                }else
                {
                    finish();
                }

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;}
    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.email:{
                // startActivity(new Intent(this,com.agrovc.viola1.bill.email.MainActivity.class));

                break;
            }
            case R.id.sms:{
                // startActivity(new Intent(this,com.agrovc.viola1.bill.sms.MainActivity.class));

                break;
            }

            case R.id.watsapp:{
                PackageManager pm=getPackageManager();
                try {

                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                    waIntent.setType("text/plain");
                    String text = "YOUR TEXT HERE";

                    PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    //Check if package exists or not. If not then code
                    //in catch block will be called
                    waIntent.setPackage("com.whatsapp");

                    waIntent.putExtra(Intent.EXTRA_TEXT, text);
                    startActivity(Intent.createChooser(waIntent, "Share with"));

                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                            .show();
                }

                break;
            }
            case R.id.call:{
                //startActivity(new Intent(this,com.agrovc.viola1.bill.call.MainActivity.class));

                break;
            }



            default:break;
        }

    }
    NumberPicker.Formatter formatter = new NumberPicker.Formatter(){
        @Override
        public String format(int i) {
            return NumberFormat.getCurrencyInstance(Locale.CANADA).format((long)i).toString();
        }
    };


    NumberPicker.OnValueChangeListener onValueChangeListener =
            new 	NumberPicker.OnValueChangeListener(){
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    Toast.makeText(MaidActivity.this,
                            "selected number "+numberPicker.getValue(), Toast.LENGTH_SHORT);
                }
            };



    public void handleButtonClicks(View view){
        int id = view.getId();

        switch (id){



            case R.id.txtWPickDate:{
                showDatePickerDialog();
                break;
            }
            default:break;
        }
    }

    private void showDatePickerDialog() {

        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setListener(this);
        newFragment.show(getFragmentManager(), "datePicker");

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        String dateSelected = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
        EditText editTextDate = (EditText) findViewById(R.id.edtWDate);
        if (editTextDate != null) {
            editTextDate.setText(dateSelected);
        }

    }















    public class DataLoaderMaid {
        Activity activity;
        RetrofitService service = new LocalRetrofitApi().getRetrofitService();
        // ProgressDialog progressDialog;



        GeneralMethods generalMethods = new GeneralMethods();

        public DataLoaderMaid(Activity activity) {
            this.activity = activity;
        }

        /*
         * Send registration details to the server
         * */
        public void userSignUp(ReqBeanMaid reqBean) {
            progressBar.setVisibility(View.VISIBLE);

            startProgressDialog();
            Call<ResBeanMaid> call = service.registerMaid(reqBean);
            call.enqueue(new Callback<ResBeanMaid>() {
                @Override
                public void onResponse(Call<ResBeanMaid> call, Response<ResBeanMaid> response) {
                    progressBar.setVisibility(View.GONE);
                    // stopProgressDialog();

                    if (response.code() == AppNums.STATUS_COD_SUCCESS) {

                        ResBeanMaid resBean = response.body();
                        String response_status = Encryption.decrypt(resBean.getResponse_status());

                        if(response_status.equalsIgnoreCase(EnumAppMessages.RESPONSE_STATUS_SUCCESS.getValue())){


                            edtEmail.getText().clear();


                            MDToast mdToast = MDToast.makeText(activity, EnumAppMessages.REGISTER_SUCCESS_TITLE.getValue(), Toast.LENGTH_LONG,MDToast.TYPE_SUCCESS);

                            mdToast.show();
                            startActivity(new Intent(MaidActivity.this,Login.class));
                            finish();
                            generalMethods.showLocationDialog(activity,EnumAppMessages.REGISTER_SUCCESS_TITLE.getValue(),Encryption.decrypt(resBean.getRegistration_status()));



                        }else{
                            MDToast mdToast = MDToast.makeText(activity, EnumAppMessages.REGISTER_ERROR_TITLE.getValue(), Toast.LENGTH_LONG,MDToast.TYPE_ERROR);

                            mdToast.show();
                            generalMethods.showLocationDialog(activity,EnumAppMessages.REGISTER_ERROR_TITLE.getValue(),Encryption.decrypt(resBean.getError()));

                        }
                    }else if(response.code() == AppNums.STATUS_COD_FILE_NOT_FOUND){
                        MDToast mdToast = MDToast.makeText(activity, EnumAppMessages.REGISTER_ERROR_TITLE.getValue(), Toast.LENGTH_LONG,MDToast.TYPE_ERROR);

                        mdToast.show();
                        generalMethods.showLocationDialog(activity,EnumAppMessages.REGISTER_ERROR_TITLE.getValue(),EnumAppMessages.ERROR_RESOURCE_NOT_FOUND.getValue());
                    }else{
                        MDToast mdToast = MDToast.makeText(activity, EnumAppMessages.REGISTER_ERROR_TITLE.getValue(), Toast.LENGTH_LONG,MDToast.TYPE_ERROR);

                        mdToast.show();
                        generalMethods.showLocationDialog(activity,EnumAppMessages.REGISTER_ERROR_TITLE.getValue(),EnumAppMessages.ERROR_INTERNAL_ERROR.getValue());
                    }
                }

                @Override
                public void onFailure(Call<ResBeanMaid> call, Throwable t) {
                    stopProgressDialog();
                    if(t instanceof IOException){
                        MDToast mdToast = MDToast.makeText(activity, EnumAppMessages.REGISTER_ERROR_TITLE.getValue(), Toast.LENGTH_LONG,MDToast.TYPE_ERROR);

                        mdToast.show();
                        generalMethods.showLocationDialog(activity,EnumAppMessages.REGISTER_ERROR_TITLE.getValue(), EnumAppMessages.ERROR_INTERNET_CONNECTION.getValue());

                    }else {
                        MDToast mdToast = MDToast.makeText(activity, EnumAppMessages.REGISTER_ERROR_TITLE.getValue(), Toast.LENGTH_LONG,MDToast.TYPE_ERROR);

                        mdToast.show();
                        generalMethods.showLocationDialog(activity,EnumAppMessages.REGISTER_ERROR_TITLE.getValue(),EnumAppMessages.ERROR_UNKNOWN_ERROR.getValue());
                    }
                }
            });
        }


        private void startProgressDialog(){
            // progressDialog = new GeneralMethods().progressDialog(activity, null,EnumAppMessages.DIALOG_PROCESSING.getValue());
            // progressDialog.show();
            progressBar.setVisibility(View.VISIBLE);
            setProgressBarIndeterminateVisibility(true);
        };

        private void stopProgressDialog(){
            progressBar.setVisibility(View.GONE);
            //progressDialog.dismiss();
        };

    }














}


