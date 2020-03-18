package com.example.needhelp.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.needhelp.R;
import com.example.needhelp.adapter.PlaceAutoSuggestAdapter;
import com.example.needhelp.api.GeoCodingApi;
import com.example.needhelp.geocodingModels.AddressComponent;
import com.example.needhelp.geocodingModels.Geometry;
import com.example.needhelp.geocodingModels.Location;
import com.example.needhelp.geocodingModels.Result;
import com.example.needhelp.geocodingModels.Results;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.android.rides.RideRequestDeeplink;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.core.client.SessionConfiguration;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HelpCall extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    ImageView close;
    TextView date_picker, txact_time;
    String value, intent_time = null;
    String from_intent, to_intent, desc_intent, ride_type_intent, companion_intent;
    RideRequestButton requestButton;
    SessionConfiguration config;
    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteTextView autoCompleteTextView1;
    String Key = "AIzaSyDN2zJbVCS_t-OMKn_g2jnbPB-xOwm2nzQ";
    Double fromLatitude = 25.4299;
    Double fromLongitude = 81.7712;
    String fromName = "Source";
    Double toLatitude = 26.8005;
    Double toLongitude = 81.0238;
    String toName = "Destination";
    private EditText from, to, description, companions;
    private DatabaseReference mDatabaseReference, uploadd;
    private Button upload;
    private FirebaseAuth auth;
    private String type_ride;
    private String username_data, id;
    private Button ola, uber, inDrive, train, plain, walk;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_call);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        autoCompleteTextView = findViewById(R.id.from_upload);
        autoCompleteTextView.setAdapter(new PlaceAutoSuggestAdapter(HelpCall.this, android.R.layout.simple_list_item_1));
        autoCompleteTextView1 = findViewById(R.id.to_upload);
        autoCompleteTextView1.setAdapter(new PlaceAutoSuggestAdapter(HelpCall.this, android.R.layout.simple_list_item_1));
        from = findViewById(R.id.from_upload);
        to = findViewById(R.id.to_upload);
        description = findViewById(R.id.description);
        upload = findViewById(R.id.upload);
        close = findViewById(R.id.close);
        companions = findViewById(R.id.number_of_companions);

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        } else {
            value = getIntent().getStringExtra("value");
            intent_time = getIntent().getStringExtra("time");
            from_intent = getIntent().getStringExtra("from_intent");
            to_intent = getIntent().getStringExtra("to_intent");
            desc_intent = getIntent().getStringExtra("desc_intent");
            ride_type_intent = getIntent().getStringExtra("mode_select_intent");
            companion_intent = getIntent().getStringExtra("companions_intetn");

            from.setText(from_intent);
            to.setText(to_intent);
            description.setText(desc_intent);
            companions.setText(companion_intent);
        }

        ola = findViewById(R.id.olaBtn);
        uber = findViewById(R.id.uberBtn);
        inDrive = findViewById(R.id.indriverBtn);
        train = findViewById(R.id.trainBtn);
        plain = findViewById(R.id.flightBtn);
        walk = findViewById(R.id.walkingBtn);
        requestButton = findViewById(R.id.requestButton);

        ola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type_ride = "ola";
                Toast.makeText(getApplicationContext(), "Ola Selected", Toast.LENGTH_SHORT).show();
            }
        });
        uber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type_ride = "uber";
                Toast.makeText(getApplicationContext(), "Uber Selected", Toast.LENGTH_SHORT).show();
            }
        });
        inDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type_ride = "inDrive";
                Toast.makeText(getApplicationContext(), "inDrive Selected", Toast.LENGTH_SHORT).show();
            }
        });
        train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type_ride = "train";
                Toast.makeText(getApplicationContext(), "Train Selected", Toast.LENGTH_SHORT).show();
            }
        });
        plain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type_ride = "flight";
                Toast.makeText(getApplicationContext(), "Flight Selected", Toast.LENGTH_SHORT).show();
            }
        });
        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type_ride = "walk";
                Toast.makeText(getApplicationContext(), "Walk Selected", Toast.LENGTH_SHORT).show();
            }
        });

        auth = FirebaseAuth.getInstance();
        id = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpCall.this, Working.class));
                finish();
            }
        });

        username_data = getIntent().getStringExtra("nameee");

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("item_details");
        mDatabaseReference.keepSynced(true);
        uploadd = FirebaseDatabase.getInstance().getReference().child("USERS");
        uploadd.keepSynced(true);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromText = autoCompleteTextView.getText().toString();
                String toText = autoCompleteTextView1.getText().toString();
                initialiseAndDeeplinkRideParams(fromText, toText);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String fromm = from.getText().toString();
                final String too = to.getText().toString();
                final String description = HelpCall.this.description.getText().toString();
                final String companionss = companions.getText().toString();
                Calendar c = Calendar.getInstance();
                // @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("dd MMM,yy hh:mm aa");
                time = System.currentTimeMillis();
                // datetime = dateformat.format(c.getTime());

                radioGroup = findViewById(R.id.postGroup);

                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);


                if (TextUtils.isEmpty(fromm) || TextUtils.isEmpty(too) || TextUtils.isEmpty(description)) {
                    Toast.makeText(getApplicationContext(), "All Fields Are Necessary", Toast.LENGTH_SHORT).show();
                } else {
                    if (radioButton.getText().toString().contains("organisation")) {
                        uploadd.child(id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    String Username = Objects.requireNonNull(dataSnapshot.child("username_item").getValue()).toString();
                                    String email = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();
                                    String imageURL = Objects.requireNonNull(dataSnapshot.child("imageURL").getValue()).toString();
                                    String phone = Objects.requireNonNull(dataSnapshot.child("phone").getValue()).toString();
                                    HashMap<String, String> hashMap = new HashMap<>();

                                    if (from_intent == null || to_intent == null) {
                                        hashMap.put("from", fromm);
                                        hashMap.put("to", too);
                                        hashMap.put("description", description);
                                        hashMap.put("username_item", Username);
                                        hashMap.put("ride_type", type_ride);
                                        hashMap.put("companions", companionss);
                                    } else {
                                        hashMap.put("from", from_intent);
                                        hashMap.put("to", to_intent);
                                        hashMap.put("description", desc_intent);
                                        hashMap.put("username_item", Username);
                                        hashMap.put("ride_type", ride_type_intent);
                                        hashMap.put("companions", companion_intent);
                                    }

                                    if (intent_time == null) {
                                        hashMap.put("time", String.valueOf(time));
                                    } else {
                                        hashMap.put("time", intent_time);
                                    }
                                    hashMap.put("email", email);
                                    hashMap.put("id", id);
                                    hashMap.put("imageUrl", imageURL);
                                    hashMap.put("phone", phone);


                                    if (value == null) {
                                        mDatabaseReference.child(id + time).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    Intent intent = new Intent(HelpCall.this, Working.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);

                                                    if (type_ride.equals("ola")) {
                                                        //openOla();
                                                    }
                                                    if (type_ride.equals("uber")) {
                                                        //openUber();
                                                    }
                                                }
                                            }
                                        });
                                    } else {
                                        mDatabaseReference.child(value).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    Toast.makeText(HelpCall.this, "Edited Successfully", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(HelpCall.this, Myuploads.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else {
                        uploadd.child(id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    String Username = "Anonymus User";
                                    String email = " ";
                                    String imageURL = "https://miro.medium.com/max/350/1*MccriYX-ciBniUzRKAUsAw.png";
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("from", fromm);
                                    hashMap.put("to", too);
                                    hashMap.put("description", description);
                                    hashMap.put("username_item", Username);
                                    hashMap.put("ride_type", type_ride);
                                    hashMap.put("companions", companionss);
                                    if (intent_time == null) {
                                        hashMap.put("time", String.valueOf(time));
                                    } else {
                                        hashMap.put("time", intent_time);
                                    }
                                    hashMap.put("email", email);
                                    hashMap.put("id", id);
                                    hashMap.put("imageUrl", imageURL);
                                    hashMap.put("phone", "N/A");

                                    if (value == null) {
                                        mDatabaseReference.child(id + time).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    Intent intent = new Intent(HelpCall.this, Working.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    } else {
                                        mDatabaseReference.child(value).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    Toast.makeText(HelpCall.this, "Edited Successfully", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(HelpCall.this, Myuploads.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        //post in normal
                    }
                }
            }
        });
    }

//    private void openOla() {
//        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.olacabs.customer");
//        if (launchIntent != null) {
//            startActivity(launchIntent);//null pointer check in case package name was not found
//        } else {
//            Uri uri = Uri.parse("market://details?id=com.olacabs.customer");
//            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//
//            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
//                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
//                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//            try {
//                startActivity(goToMarket);
//            } catch (ActivityNotFoundException e) {
//                startActivity(new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("http://play.google.com/store/apps/details?id=com.olacabs.customer")));
//            }
//        }
//    }
//
//    private void openUber() {
//        PackageManager pm = getPackageManager();
//        try {
//            pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
//            String uri = "uber://?action=setPickup&pickup=my_location";
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse(uri));
//            startActivity(intent);
//        } catch (PackageManager.NameNotFoundException e) {
//            try {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.ubercab")));
//            } catch (android.content.ActivityNotFoundException anfe) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.ubercab")));
//            }
//        }
//    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, i2);
        c.set(Calendar.MONTH, i1);
        c.set(Calendar.DATE, i);
        String currentDateString = DateFormat.getDateInstance().format(c.getTime());
        date_picker.setText(currentDateString);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        txact_time.setText(i + "hr" + "Minute : " + i1);
    }

    private void initialiseFromDetails(String from) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/geocode/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GeoCodingApi geoCodingApi = retrofit.create(GeoCodingApi.class);

        Call<Results> call = geoCodingApi.getLocation(from, Key);

        call.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(HelpCall.this, response.code(), Toast.LENGTH_SHORT).show();
                }
                Results results = response.body();
                List<Result> resultsList = results.getResults();
                Geometry geometry = resultsList.get(0).getGeometry();

                List<AddressComponent> addressComponentList = resultsList.get(0).getAddressComponents();
                fromName = addressComponentList.get(0).getLongName();

                Location location = geometry.getLocation();
                fromLatitude = location.getLat();
                fromLongitude = location.getLng();
            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                Toast.makeText(HelpCall.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialiseToDetails(String to) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/geocode/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GeoCodingApi geoCodingApi = retrofit.create(GeoCodingApi.class);

        Call<Results> call = geoCodingApi.getLocation(to, Key);

        call.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(HelpCall.this, response.code(), Toast.LENGTH_SHORT).show();
                }
                Results results = response.body();
                List<Result> resultsList = results.getResults();
                Geometry geometry = resultsList.get(0).getGeometry();

                List<AddressComponent> addressComponentList = resultsList.get(0).getAddressComponents();
                toName = addressComponentList.get(0).getLongName();

                Location location = geometry.getLocation();
                toLatitude = location.getLat();
                toLongitude = location.getLng();
            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                Toast.makeText(HelpCall.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialiseAndDeeplinkRideParams(String fromText, String toText) {
        initialiseFromDetails(fromText);
        initialiseToDetails(toText);

        config = new SessionConfiguration.Builder()
                .setClientId("mo7Mlwn9KYjoBB3QusGmDwGOxf722-b_") //This is necessary
                .setRedirectUri("com.example.app.uberauth://redirect")//This is necessary if you'll be using implicit grant
                .setEnvironment(SessionConfiguration.Environment.SANDBOX) //Useful for testing your app in the sandbox environment
                .setScopes(Arrays.asList(Scope.PROFILE, Scope.RIDE_WIDGETS)) //Your scopes for authentication here
                .build();
        RideParameters rideParams = new RideParameters.Builder()
                .setProductId("a1111c8c-c720-46c3-8534-2fcdd730040d")
                .setPickupLocation(fromLatitude, fromLongitude, fromName, fromName)
                .setDropoffLocation(toLatitude, toLongitude, toName, toName)
                .build();

        requestButton.setRideParameters(rideParams);

        RideRequestDeeplink deeplink = new RideRequestDeeplink.Builder(this)
                .setSessionConfiguration(config)
                .setRideParameters(rideParams)
                .build();
        deeplink.execute();
    }
}