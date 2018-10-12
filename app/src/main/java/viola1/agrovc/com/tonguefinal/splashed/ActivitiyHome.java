package viola1.agrovc.com.tonguefinal.splashed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.BootstrapButton;

import viola1.agrovc.com.tonguefinal.R;


public class ActivitiyHome extends AppCompatActivity {
    BootstrapButton tutor;
    BootstrapButton student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }




    /*public void checkOption(View view){
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
    }*/




}
