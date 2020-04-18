package com.example.needhelp.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import com.example.needhelp.R;

public class About extends AppCompatActivity {
    ImageView close;
    LinearLayout github_profile;
    LinearLayout rohit_email;
    LinearLayout star;
    LinearLayout submit_article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        star = findViewById(R.id.Star_on_github);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCrometab("https://github.com/refactor-droidyy/Share-Ride");
            }
        });
        github_profile = findViewById(R.id.rohit_github);
        github_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCrometab("https://github.com/refactor-droidyy");
            }
        });

        rohit_email = findViewById(R.id.rohit_email);
        rohit_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail("mailto:lit2018068@iiitl.ac.in?subject=Feedback & Bugs for Share Ride App");
            }
        });

        close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(About.this, MainActivity.class));
                finish();
            }
        });

        submit_article = findViewById(R.id.Submit_article);
        submit_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail("mailto:lit2018068@iiitl.ac.in?subject=Article submission for Share Ride App");
            }
        });
    }

    private void openCrometab(String s) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(Color.parseColor("#003366"));
        CustomTabsIntent intent = builder.build();
        intent.launchUrl(this, Uri.parse(s));
    }

    private void sendMail(String mailto) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(mailto));
        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            //TODO: Handle case where no email app is available
        }
    }
}
