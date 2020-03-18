package com.example.needhelp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.needhelp.Group;
import com.example.needhelp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateGroup extends AppCompatActivity {

    DatabaseReference databaseReference;
    Button createGpBtn;
    EditText gpNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        databaseReference = FirebaseDatabase.getInstance().getReference("groups");
        databaseReference.keepSynced(true);
        ImageView close = findViewById(R.id.close);
        gpNameText = findViewById(R.id.group_name_edit_text);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateGroup.this, MyGroups.class));
                finish();
            }
        });
        createGpBtn = findViewById(R.id.createGpBtn);
        createGpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGroup();
            }
        });
    }

    private void addGroup() {
        String id = databaseReference.push().getKey();
        String gpName = gpNameText.getText().toString();
        Group group = new Group(id, gpName, "abc", "def");
        assert id != null;
        databaseReference.child(id).setValue(group);
        Toast.makeText(this, "Group Added", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(CreateGroup.this, MyGroups.class));
    }
}
