package viola1.agrovc.com.tonguefinal.view;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.beardedhen.androidbootstrap.AwesomeTextView;

import viola1.agrovc.com.tonguefinal.Others.AdminContact;
import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.constants.AppPrefs;

public class HelpCenter extends AppCompatActivity implements View.OnClickListener {
    /**   private Button email;
     private Button sms;
     private Button watsapp;
     private Button call;**/

    private AwesomeTextView faq;
    private AwesomeTextView contact_us;

    private AwesomeTextView licences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_center);

        initialize();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Agro-VCM");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_navigation_arrow_back));

        toolbar.setTitle("Agro-VCM");


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initialize() {

        faq = (AwesomeTextView) findViewById(R.id.faq);
        faq.setOnClickListener(this);
        contact_us = (AwesomeTextView) findViewById(R.id.contact_us);
        contact_us.setOnClickListener(this);
        licences = (AwesomeTextView) findViewById(R.id.licences);
        licences.setOnClickListener(this);



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
        AlertDialog.Builder builder = new AlertDialog.Builder(HelpCenter.this);
        builder.setMessage(R.string.app_close_prompt);

        builder.setNegativeButton(R.string.logout,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AppPrefs.clearLoginCredentials(HelpCenter.this);
                        startActivity(new Intent(HelpCenter.this, Login.class));
                        finish();
                    }
                });
        builder.setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                    System.exit(0);
                } else

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
        return true;
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.faq: {
               // startActivity(new Intent(this,faq.class));

                break;
            }
            case R.id.contact_us: {
                startActivity(new Intent(this,AdminContact.class));

            }
            case R.id.licences:{



                break;
            }





            default:break;
        }

    }


}

