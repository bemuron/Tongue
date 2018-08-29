package viola1.agrovc.com.tonguefinal.Others;

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
import android.widget.Toast;


import com.beardedhen.androidbootstrap.BootstrapThumbnail;



import com.beardedhen.androidbootstrap.BootstrapThumbnail;

import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.constants.AppPrefs;
import viola1.agrovc.com.tonguefinal.view.Login;

public class AdminContact extends AppCompatActivity implements View.OnClickListener {
    /**   private Button email;
     private Button sms;
     private Button watsapp;
     private Button call;**/

    private BootstrapThumbnail email;
    private BootstrapThumbnail sms;
    private BootstrapThumbnail watsapp;
    private BootstrapThumbnail call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);

        initialize();

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Contact Us");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_navigation_arrow_back));

        toolbar.setTitle("Contact Us");


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    private void initialize() {

        email = (BootstrapThumbnail)findViewById(R.id.email);
        email.setOnClickListener(this);

        sms = (BootstrapThumbnail)findViewById(R.id.sms);
        sms.setOnClickListener(this);

        watsapp = (BootstrapThumbnail)findViewById(R.id.watsapp);
        watsapp.setOnClickListener(this);


        call = (BootstrapThumbnail)findViewById(R.id.call);
        call.setOnClickListener(this);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminContact.this);
        builder.setMessage(R.string.app_close_prompt);

        builder.setNegativeButton(R.string.logout,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AppPrefs.clearLoginCredentials(AdminContact.this);
                        startActivity(new Intent(AdminContact.this,Login.class));
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
}

