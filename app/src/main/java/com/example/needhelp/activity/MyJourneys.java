package com.example.needhelp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.needhelp.R;
import com.example.needhelp.adapter.MyJourneysAdapter;
import com.example.needhelp.model.Upload;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyJourneys extends AppCompatActivity {
    private ImageView close;
    private RecyclerView recyclerView;
    private FirebaseUser user;
    private MyJourneysAdapter adapter;
    private DatabaseReference reference;
    private List<Upload> mUploads;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_journeys);

        close = findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyJourneys.this, MainActivity.class));
                finish();
            }
        });

        recyclerView = findViewById(R.id.Recyclee);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        user = FirebaseAuth.getInstance().getCurrentUser();

        mUploads = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("item_details");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUploads.clear();
                for (DataSnapshot mdataSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = mdataSnapshot.getValue(Upload.class);
                    assert upload != null;
                    if (user.getUid().equals(upload.getId())) {
                        mUploads.add(upload);
                    }

                    adapter = new MyJourneysAdapter(getApplicationContext(), mUploads);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
