package viola1.agrovc.com.tonguefinal.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.beardedhen.androidbootstrap.font.FontAwesome;

import viola1.agrovc.com.tonguefinal.R;


public class StudentHome extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{
    private BootstrapThumbnail complete_registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //initialize();
        TypefaceProvider.registerDefaultIconSets();
       // FontAwesome.applyToAllViews(this, findViewById(R.id.activity_main));
        //Typeface font=Typeface.createFromAsset(getAssets(),"fontawesome-webfont.ttf");
        // Button awesomeButton=(Button)findViewById(R.id.awesome_button);
        //awesomeButton.setTypeface(font);
        // awesomeButton.setText(getString(R.string.icon_heart));
        complete_registration = (BootstrapThumbnail)findViewById(R.id.complete_registration);
        complete_registration.setOnClickListener(this);

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.complete_registration:{
                startActivity(new Intent(this,MaidActivity.class));

                break;
            }



            default:break;
        }

    }
}
