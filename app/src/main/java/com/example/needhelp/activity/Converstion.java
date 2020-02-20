package com.example.needhelp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.needhelp.R;
import com.example.needhelp.fragment.ChatFragment;

public class Converstion extends AppCompatActivity {
    private ImageView close;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converstion);
        close = findViewById(R.id.close);

        ChatFragment f1 = new ChatFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, f1);
        fragmentTransaction.commit();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Converstion.this, Working.class));
                finish();
            }
        });
    }
}
