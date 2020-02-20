package com.example.needhelp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Group extends Fragment {


    FirebaseUser user;
    private ListView listview;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list = new ArrayList<>();
    private View view;
    private String tt = "";
    private TextView txt;
    private List<String> lst;

    private ValueEventListener reference;

    public Group() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_group, container, false);
        FloatingActionButton fab = view.findViewById(R.id.floating);
        txt = view.findViewById(R.id.textt);
        user = FirebaseAuth.getInstance().getCurrentUser();


        return view;

    }


}
