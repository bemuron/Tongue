package viola1.agrovc.com.tonguefinal.presentation.ui.activities;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.helper.SquareImageView;

public class AboutTongueActivity extends AppCompatActivity {
    private SquareImageView backgroundImage;
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView aboutTongue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_tongue);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        backgroundImage = findViewById(R.id.bkImage);
        collapsingToolbar = findViewById(R.id.toolbar_layout);
        aboutTongue = findViewById(R.id.about_tongue_tv);

        setAboutTongue();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setAboutTongue(){
        aboutTongue.setText(R.string.about_tongue);
        aboutTongue.setTextSize(18);

        collapsingToolbar.setTitle("About Tongue");
        int imageId = getResources().getIdentifier("study_together",
                "drawable", getPackageName());
        backgroundImage.setImageResource(imageId);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
    }
}
