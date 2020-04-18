package com.example.needhelp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.needhelp.adapter.RequestAdapter;
import com.example.needhelp.model.Regain;
import com.example.needhelp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.needhelp.R.id;
import static com.example.needhelp.R.layout;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {
    FirebaseUser user;
    private String ss = "";
    private DatabaseReference ref;
    private Set<String> list;
    private TextView idv;
    private RequestAdapter adapter;
    private List<User> mUsers;

    private RecyclerView recyclerView;

    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(layout.fragment_request, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();

//        idv = v.findViewById(id.idview);
        recyclerView = v.findViewById(id.RecyclerVieww);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ref = FirebaseDatabase.getInstance().getReference().child("Friend_Request").child(user.getUid());
        ref.keepSynced(true);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new HashSet<>();
                for (DataSnapshot snapshots : dataSnapshot.getChildren()) {
                    Regain regg = snapshots.getValue(Regain.class);
                    assert regg != null;
                    String sdd = regg.getRequesttype();
                    String ff = regg.getFriend();
                    if (sdd.equals("recieved")) {
                        //  && ff.equals("false")

                        list.add(regg.getId());
                    }

                }
                showRequest();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }

    private void showRequest() {
        mUsers = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("USERS");
        ref.keepSynced(true);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUsers.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    User user = snapshot.getValue(User.class);

                    for (String id : list) {
                        assert user != null;
                        if (user.getId().equals(id)) {
                            mUsers.add(user);
                        }
                    }
                }

                adapter = new RequestAdapter(getContext(), mUsers);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
