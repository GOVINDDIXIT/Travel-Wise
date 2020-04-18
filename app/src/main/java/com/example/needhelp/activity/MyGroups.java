package com.example.needhelp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.needhelp.model.Group;
import com.example.needhelp.R;
import com.example.needhelp.adapter.GroupsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyGroups extends AppCompatActivity {

    private RecyclerView recyclerView;
    //private FirebaseUser user;
    private GroupsAdapter adapter;
    private List<Group> mGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);

        ImageView close = findViewById(R.id.close);
        FloatingActionButton faBtn = findViewById(R.id.createGpFab);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyGroups.this, MainActivity.class));
                finish();
            }
        });

        faBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyGroups.this, CreateGroup.class));
            }
        });

        recyclerView = findViewById(R.id.Recyclee);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mGroups = new ArrayList<>();
        adapter = new GroupsAdapter(getApplicationContext(), mGroups);
        recyclerView.setAdapter(adapter);
        //user = FirebaseAuth.getInstance().getCurrentUser();

        readGroups();
    }

    private void readGroups() {

        mGroups = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("groups");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mGroups.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Group group = snapshot.getValue(Group.class);
                    mGroups.add(group);
                }
                adapter = new GroupsAdapter(getApplicationContext(), mGroups);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
