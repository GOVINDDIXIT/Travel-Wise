package com.example.needhelp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.needhelp.R;
import com.example.needhelp.adapter.MessageAdapter;
import com.example.needhelp.model.Chat;
import com.example.needhelp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Message extends AppCompatActivity {

    private static final int REQUEST_CALL = 2342;
    private ImageView profile_image;
    private TextView username;
    private ImageView close;
    private TextView statuss;

    FirebaseUser fireBse;
    DatabaseReference reference;

    private EditText message;
    private ImageButton send;

    MessageAdapter messageAdapter;
    List<Chat> mchats;
    RecyclerView recyclerView;
    String userid;
    ImageView makeCall;
    String phone;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        profile_image = findViewById(R.id.img_toolbar_mess);
        username = findViewById(R.id.username_toolbar_mess);
        message = findViewById(R.id.txt_message);
        send = findViewById(R.id.send);
        recyclerView = findViewById(R.id.recycler_view_mess);
        makeCall = findViewById(R.id.make_call);
        phone = getIntent().getExtras().getString("phone");

        makeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCall(phone);
            }
        });

        close = findViewById(R.id.close);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        intent = getIntent();

        userid = intent.getStringExtra("ID");
        final String imageUrl = getIntent().getExtras().getString("imgUrl");


        Picasso.get()
                .load(imageUrl)
                .resize(100, 100)
                .into(profile_image);

        fireBse = FirebaseAuth.getInstance().getCurrentUser();
        assert userid != null;
        reference = FirebaseDatabase.getInstance().getReference("USERS").child(userid);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg = message.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(fireBse.getUid(), userid, msg);
                } else {
                    Toast.makeText(Message.this, "Please Type A Message", Toast.LENGTH_LONG).show();
                }
                message.setText("");
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                assert user != null;

                username.setText(user.getUsername_item());

                readMessages(fireBse.getUid(), userid, user.getImageURL());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void makeCall(String phone) {
        if (ContextCompat.checkSelfPermission(Message.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Message.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone)));
        }
    }

    private void readMessages(final String myid, final String userid, final String imageurl) {

        mchats = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mchats.clear();

                for (DataSnapshot snapshots : dataSnapshot.getChildren()) {

                    Chat chat = snapshots.getValue(Chat.class);
                    assert chat != null;
                    if (chat.getReciever().equals(myid) && chat.getSender().equals(userid) || chat.getReciever().equals(userid) && chat.getSender().equals(myid)) {
                        mchats.add(chat);
                    }

                    messageAdapter = new MessageAdapter(Message.this, mchats, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall(phone);
            } else {
                Toast.makeText(Message.this, "Denied Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendMessage(String sender, String reciever, String messag) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("sender", sender);
        hashMap.put("reciever", reciever);
        hashMap.put("message", messag);

        reference.child("Chats").push().setValue(hashMap);

    }
}
