package viola1.agrovc.com.tonguefinal.splashed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;

import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.view.Login;
import viola1.agrovc.com.tonguefinal.view.StudentLogin;


public class ActivitiyHome extends AppCompatActivity {
    BootstrapButton tutor;
    BootstrapButton student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tutor = (BootstrapButton) findViewById(R.id.tutor);
        student = (BootstrapButton) findViewById(R.id.student);

    }




    public void checkOption(View view){
        int id = view.getId();
        switch (id){
            case R.id.tutor:{
                startActivity(new Intent(ActivitiyHome.this,Login.class));
                finish();
                break;
            }
            case R.id.student:{
                startActivity(new Intent(ActivitiyHome.this,StudentLogin.class));
                finish();
                break;
            }
            default:{
                break;
            }
        }
    }




}
