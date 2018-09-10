package viola1.agrovc.com.tonguefinal.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.BootstrapProgressBar;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.app.localserver.request.ReqBeanTutor;
import viola1.agrovc.com.tonguefinal.app.localserver.request.response.ResBeanTutor;
import viola1.agrovc.com.tonguefinal.constants.AppErrors;
import viola1.agrovc.com.tonguefinal.constants.AppNums;
import viola1.agrovc.com.tonguefinal.constants.EnumAppMessages;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.LocalRetrofitApi;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.RetrofitService;
import viola1.agrovc.com.tonguefinal.presentation.ui.fragments.DatePickerFragment;
import viola1.agrovc.com.tonguefinal.helper.Encryption;
import viola1.agrovc.com.tonguefinal.helper.GeneralMethods;
import viola1.agrovc.com.tonguefinal.helper.InputValidator;

public class Tutor extends AppCompatActivity implements View.OnClickListener,DatePickerDialog.OnDateSetListener {


    /**   private Button email;
     private Button sms;
     private Button watsapp;
     private Button call;**/
    private EditText wage;

    private EditText phone;
    private EditText subjects;
    private TextView desc;
    private TextView rating_title;
    private TextView rating_count;
    RatingBar rating_bar;
    private ImageView pro_pic;
    Button upload_image;
    private static final int RESULT_LOAD_IMAGE = 1;
    Button saveChangesButton;
    AutoCompleteTextView spinner_country;



    Spinner spinner_user_type;



    BootstrapEditText description;
    Spinner spinner_sex;
    Spinner spinner_level;
    BootstrapEditText edtFirstname;
    BootstrapEditText edtLastname;
    BootstrapEditText edtContact;
    BootstrapEditText edtEmail;
    BootstrapEditText nin_number;
    AutoCompleteTextView district;
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
    NumberPicker npsalary;
    NumberPicker np;
    private BootstrapProgressBar progressBar;


    String[] countries = new String[]{"Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra",
            "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba",
            "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados",
            "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina",
            "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria",
            "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands",
            "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands",
            "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica",
            "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti",
            "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea",
            "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji",
            "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia",
            "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar",
            "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau",
            "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras",
            "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq",
            "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati",
            "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, " +
            "People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya",
            "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of",
            "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique",
            "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of",
            "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal",
            "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria",
            "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama",
            "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico",
            "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia",
            "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia",
            "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia",
            "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe"};


    String[] districts = new String[]{"Abim", "Adjumani"," Alebtong","Amolatar", "Amuria",
            "Amuru","Apac","Arua","Bombo","Budaka","Bugembe", " Bugiri", "Buikwe ", "Bukedea", "Bukomansimbi", "Bukungu", "Buliisa", "Bundibugyo", "Busembatya", "Bushenyi", "Busia", "Busolwe", "Butaleja", "Buwenge", "Buyende", "Dokolo", "Elegu", "Entebbe", "Fort Portal", "Gombe, Butambala", "Gulu", "Hima", "Hoima ", " Ibanda ", "Iganga", "Isingiro", "Jinja ", "Kaabong",
            "Kabale ", "Kaberamaido", "Kabuyanda", "Kabwohe", "Kagadi", "Kakinga", "Kakira", "Kakiri ", "Kalangala", "Kaliro", "Kalisizo", "Kalongo ", "Kalungu", "Kampala", "Kamuli ", "Kamwenge", "Kanoni", "Kanungu", "Kapchorwa", "Kasese ", "Katakwi", "Kayunga", "Kibaale", "Kibingo", "Kiboga", "Kihiihi", "Kira ", "Kiruhura", "Kiryandongo", "Kisoro", "Kitgum ", "Koboko", "Kotido", "Kumi", "Kyazanga", "Kyegegwa ", "Kyenjojo", "Kyotera", "Lira", "Lugazi", "Lukaya","Luweero", "Lwakhakha", "Lwengo", "Lyantonde", "Malaba", "Manafwa" ,"Masaka", "Masindi" ,"Masindi" ,"Masulita", "Matugga", "Mayuge", "Mbale", "Mbarara", "Mitooma" ,"Mityana", "Moroto", "Moyo", "Mpigi", "Mpondwe", "Mubende", "Mukono","Mutukula ","Nagongera","Nakaseke","Nakapiripirit","Nakasongola","Namayingo", "Namayumba" ,"Namutumba","Nansana","Nebbi","Ngora","Njeru","Nkokonjeru",
            "Ntungamo", "Oyam","Pader"," Paidha","Pakwach","Pallisa","Rakai","Rukungiri","Rwimi"
            ,"Sanga", "Sembabule","Sironko","Soroti","Ssabagabo","Tororo","Wakiso","Wobulenzi","Yumbe","Dar es Salaam","Mwanza",
            "Arusha","Dodoma","Mbeya","Morogoro","Tanga","Kahama","Tabora","Zanzibar","Kigoma","Sumbawanga",
            "Kasulu","Songea","Moshi","Musoma","Shinyanga","Iringa","Singida","Njombe","Bukoba","Kibaha","Mtwara",
            "Mpanda","Tunduma","Makambako","Babati","Handeni","Lindi","Korogwe","Mahinga",
            "Abekr","Abyei", "Al Fashir","Al Managil","Al Qadarif","Al-Ubayyid","Atbara","Babanusa","Berber",
            "Buwaidhaa","Delgo","Dongola","Ad-Damazin","Ed Dueim","El Ait","El Gebir","En Nahud",
            "Er Rahad","Geneina","Hala","Iyāl Bakhīt","Kaduqli","Kassala","Khartoum North or Bahri",
            "Kusti or Kosti","Merowe","Muglad","Kigali","Butare","Muhanga","Ruhengeri","Gisenyi","Byumba",
            "Cyangugu","Nyanza","Kabuga","Ruhango","Rwamagana","Kibuye","Kibungo","Gikongoro","Umutara",
            "Baragoi","Bungoma","Busia","Butere","Dadaab","Diani","Beach","Eldoret","Emali","Embu","Garissa","Gede",
            "Hola","Homa Bay","Isiolo","Kitui","Kibwezi","Makindu","Wote","Mutomo","Kajiado","Kakamega","Kakuma",
            "Kapenguria", "Kericho","Keroka","Kiambu","Kilifi","Kisii","Kisumu","Kitale","Lamu","Langata","Litein",
            "Lodwar","Lokichoggio","Londiani","Loyangalani","Machakos","Malindi","Mandera","Maralal","Marsabit","Meru",
            "Mombasa","Moyale","Mumias","Muranga","Nairobi","Naivasha","Nakuru","Namanga","Nanyuki","Naro","Moru",
            "Narok","Nyahururu","Nyeri","Ruiru","Shimoni","Takaungu","Thika","Vihiga"
            ,"Voi","Wajir","Watamu","Webuye","Wundanyi"
    };



    public String[] gender = {
            "",
            "Male",
            "Female",

    };






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutor_registration);

        initialize();

        upload_image.setOnClickListener(this);
        pro_pic.setOnClickListener(this);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Tutor");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_navigation_arrow_back));

        toolbar.setTitle("Tutor");


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    private void initialize() {

        wage = (EditText) findViewById(R.id.enter_hourly_rate);

        subjects = (EditText) findViewById(R.id.enter_subjects);
        desc = (TextView) findViewById(R.id.descTextView);


        pro_pic = (ImageView) findViewById(R.id.pro_pic);
        upload_image = (Button) findViewById(R.id.upload_image);
        wage = (EditText) findViewById(R.id.enter_hourly_rate);
        rating_bar = (RatingBar) findViewById(R.id.ratingBar);

        // Get rating from rating table
        double rating = 0;
        int rateCount = 0;
        Log.w("rating", String.valueOf(rating));
        rating_title = (TextView) findViewById(R.id.rating_title);

        edtFirstname= (BootstrapEditText) findViewById(R.id.edtFirstname);
        edtLastname= (BootstrapEditText) findViewById(R.id.edtLastname);
        edtContact= (BootstrapEditText) findViewById(R.id.edtContact);

        edtEmail= (BootstrapEditText) findViewById(R.id.edtEmail);
        nextAddress= (BootstrapEditText) findViewById(R.id.nextaddress);
        nextRelationship= (BootstrapEditText) findViewById(R.id.nextrelationship);
        nextContact= (BootstrapEditText) findViewById(R.id.nextContact);

        next= (BootstrapEditText) findViewById(R.id.next);
        language= (BootstrapEditText) findViewById(R.id.language);

        ArrayAdapter<String> adapter_district = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, districts);
        district= (AutoCompleteTextView) findViewById(R.id.district);
        district.setThreshold(1);//will start working from first character
        district.setAdapter(adapter_district);//setting the adapter data into the AutoCompleteTextView
        district.setTextColor(Color.BLACK);

        address= (BootstrapEditText) findViewById(R.id.address);
        nin_number= (BootstrapEditText) findViewById(R.id.nin_number);

        description= (BootstrapEditText) findViewById(R.id.description);
        edtWDate= (BootstrapEditText) findViewById(R.id.edtWDate);
        txtWPickDate= (TextView) findViewById(R.id.txtWPickDate);
        progressBar = (BootstrapProgressBar) findViewById(R.id.progressBar1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, countries);
        spinner_country= (AutoCompleteTextView) findViewById(R.id.spinner_country);
        spinner_country.setThreshold(1);//will start working from first character
        spinner_country.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        spinner_country.setTextColor(Color.BLACK);

        spinner_sex= (Spinner) findViewById(R.id.spinner_sex);
        spinner_sex.setPrompt("Select Gender");


        ArrayAdapter<String> adaptergender = new ArrayAdapter<String>(this,
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
        adaptergender.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner_sex.setAdapter(adaptergender);




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
            int mnpsalary = npsalary.getValue();
            String mspinner_country = spinner_country.getText().toString();
            String mspinner_sex = spinner_sex.getSelectedItem().toString();
            String mspinner_level = spinner_level.getSelectedItem().toString();


            ReqBeanTutor reqBean = new ReqBeanTutor();
            reqBean.setFirstname(Encryption.encrypt(Firstname));
            reqBean.setLastname(Encryption.encrypt(Lastname));
            reqBean.setNext(Encryption.encrypt(Next));
            reqBean.setLanguage_spoken(Encryption.encrypt(Language));
            reqBean.setNextcontact(Encryption.encrypt(NextContact));
            reqBean.setNextaddress(Encryption.encrypt(NextAddress));
            reqBean.setDescription(Encryption.encrypt(Description));
            reqBean.setNextrelationship(Encryption.encrypt(NextRelationship));
            reqBean.setAddress(Encryption.encrypt(Address));
            reqBean.setDistrict(Encryption.encrypt(District));
            reqBean.setyears(Encryption.encrypt2(mnp));
            reqBean.setsalary(Encryption.encrypt2(mnpsalary));
            reqBean.setNIN(Encryption.encrypt( Enin_number));
            reqBean.setEmail(Encryption.encrypt(Email));

            reqBean.setContact(Encryption.encrypt(Contact));
            reqBean.setCountry(Encryption.encrypt(mspinner_country));
            reqBean.setDate(Encryption.encrypt(Date));
            reqBean.setGender(Encryption.encrypt(mspinner_sex));
            reqBean.setLevel(Encryption.encrypt(mspinner_level));

            new DataLoaderTutor(Tutor.this).userSignUp(reqBean);

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
            MDToast mdToast = MDToast.makeText(Tutor.this, AppErrors.INVALID_EMAIL,Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            return false;
        }
        if(edtFirstname.getText().toString().isEmpty()){
            focusView = edtFirstname;
            MDToast mdToast = MDToast.makeText(Tutor.this, "First Name is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }
        if(edtLastname.getText().toString().isEmpty()){
            focusView = edtLastname;
            MDToast mdToast = MDToast.makeText(Tutor.this, "Last Name is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }


        if( edtContact.getText().toString().isEmpty()){
            focusView =  edtContact;
            MDToast mdToast = MDToast.makeText(Tutor.this, "Contact is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }

        if( edtContact.getText().toString().isEmpty()){
            focusView =  edtContact;
            MDToast mdToast = MDToast.makeText(Tutor.this, "Contact is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }


        if( edtEmail.getText().toString().isEmpty()){
            focusView = edtEmail;
            MDToast mdToast = MDToast.makeText(Tutor.this, "Email is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }

        if( nextAddress.getText().toString().isEmpty()){
            focusView = nextAddress;
            MDToast mdToast = MDToast.makeText(Tutor.this, "Next of kin's address is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }

        if(nextRelationship.getText().toString().isEmpty()){
            focusView = nextRelationship;
            MDToast mdToast = MDToast.makeText(Tutor.this, "Next of kin's relationship is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }

        if(nextContact.getText().toString().isEmpty()){
            focusView = nextContact;
            MDToast mdToast = MDToast.makeText(Tutor.this, "Next of kin's relationship is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }

        if(next.getText().toString().isEmpty()){
            focusView = next;
            MDToast mdToast = MDToast.makeText(Tutor.this, "Next of kin is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }
        if(language.getText().toString().isEmpty()){
            focusView = language;
            MDToast mdToast = MDToast.makeText(Tutor.this, "language is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }

        if(district.getText().toString().isEmpty()){
            focusView = district;
            MDToast mdToast = MDToast.makeText(Tutor.this, "District is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }


        if(address.getText().toString().isEmpty()){
            focusView = address;
            MDToast mdToast = MDToast.makeText(Tutor.this, "Address is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }
        if(description.getText().toString().isEmpty()){
            focusView = description;
            MDToast mdToast = MDToast.makeText(Tutor.this, "Add your description",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }
        if(nin_number.getText().toString().isEmpty()){
            focusView = nin_number;
            MDToast mdToast = MDToast.makeText(Tutor.this, "NIN number is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }

        if(edtWDate.getText().toString().isEmpty()){
            focusView = edtWDate;
            MDToast mdToast = MDToast.makeText(Tutor.this, "This date is required",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR);

            mdToast.show();
            //  Firstname.setError(AppErrors.INVALID_FIRST_NAME);
            return false;
        }

        if( spinner_country.getText().toString()==""){
            focusView = spinner_country;
            MDToast mdToast = MDToast.makeText(Tutor.this,"Select country",MDToast.TYPE_ERROR);

            mdToast.show();
            // edtContact.setError(AppErrors.INVALID_CONTACT);
            return false;
        }
        if( spinner_sex.getSelectedItem().toString()==""){
            focusView = spinner_sex;
            MDToast mdToast = MDToast.makeText(Tutor.this,"Select country",MDToast.TYPE_ERROR);

            mdToast.show();
            // edtContact.setError(AppErrors.INVALID_CONTACT);
            return false;
        }
        if(  spinner_level.getSelectedItem().toString()==""){
            focusView =  spinner_level;
            MDToast mdToast = MDToast.makeText(Tutor.this,"Select country",MDToast.TYPE_ERROR);

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















    public class DataLoaderTutor {
        Activity activity;
        RetrofitService service = new LocalRetrofitApi().getRetrofitService();
        // ProgressDialog progressDialog;



        GeneralMethods generalMethods = new GeneralMethods();

        public DataLoaderTutor(Activity activity) {
            this.activity = activity;
        }

        /*
         * Send registration details to the server
         * */
        public void userSignUp(ReqBeanTutor reqBean) {
            progressBar.setVisibility(View.VISIBLE);

            startProgressDialog();
            Call<ResBeanTutor> call = service.registerTutor(reqBean);
            call.enqueue(new Callback<ResBeanTutor>() {
                @Override
                public void onResponse(Call<ResBeanTutor> call, Response<ResBeanTutor> response) {
                    progressBar.setVisibility(View.GONE);
                    // stopProgressDialog();

                    if (response.code() == AppNums.STATUS_COD_SUCCESS) {

                        ResBeanTutor resBean = response.body();
                        String response_status = Encryption.decrypt(resBean.getResponse_status());

                        if(response_status.equalsIgnoreCase(EnumAppMessages.RESPONSE_STATUS_SUCCESS.getValue())){


                            edtEmail.getText().clear();


                            MDToast mdToast = MDToast.makeText(activity, EnumAppMessages.REGISTER_SUCCESS_TITLE.getValue(), Toast.LENGTH_LONG,MDToast.TYPE_SUCCESS);

                            mdToast.show();
                            startActivity(new Intent(Tutor.this,Login.class));
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
                public void onFailure(Call<ResBeanTutor> call, Throwable t) {
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
        }

        private void stopProgressDialog(){
            progressBar.setVisibility(View.GONE);
            //progressDialog.dismiss();
        }

    }














}


