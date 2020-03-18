package com.example.needhelp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.needhelp.R;
import com.example.needhelp.adapter.RecyclerViewAdapter;
import com.example.needhelp.model.Upload;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Working extends AppCompatActivity {
    FloatingActionButton main;
    LinearLayoutManager linearLayoutManager;
    private FirebaseUser firebaseUser;
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private List<Upload> mUploads;
    private String message;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working);

        message = getIntent().getStringExtra("namee");
        Toolbar toolbar = findViewById(R.id.toolbar_work);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        Objects.requireNonNull(getSupportActionBar()).dispatchMenuVisibilityChanged(true);

        //fab
        main = findViewById(R.id.main);

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Working.this, HelpCall.class);
                intent.putExtra("nameee", message);
                startActivity(intent);
            }
        });


        recyclerView = findViewById(R.id.Recycle);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

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
                    if (!firebaseUser.getUid().equals(upload.getId())) {
                        mUploads.add(upload);
                    }
                    adapter = new RecyclerViewAdapter(getApplicationContext(), mUploads);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater menuInflater1 = getMenuInflater();
        menuInflater1.inflate(R.menu.menu1, menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.actio_search);

        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.uploadss:
                startActivity(new Intent(Working.this, Myuploads.class));
                finish();
                break;

            case R.id.profile:
                startActivity(new Intent(Working.this, Profile.class));
                finish();
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Working.this, MainActivity.class));
                finish();
                break;
            case R.id.chats:
                startActivity(new Intent(Working.this, Conversation.class));
                finish();
                break;
            case R.id.myrides:
                startActivity(new Intent(Working.this, MyRides.class));
                finish();
                break;
            case R.id.mygroups:
                startActivity(new Intent(Working.this, MyGroups.class));
                finish();
                break;
            case R.id.actio_emergncy:
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"lit2018068@iiitl.ac.in"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Emergency");
                email.putExtra(Intent.EXTRA_TEXT, "I am in a great trouble I need help please help me");

                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
