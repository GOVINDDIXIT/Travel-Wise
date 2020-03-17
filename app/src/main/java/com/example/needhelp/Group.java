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
public class Group  {

    private String id;
    private String groupName;
    private String from;
    private String to;

    public Group(String id, String groupName, String from, String to) {
        this.id = id;
        this.groupName = groupName;
        this.from = from;
        this.to = to;
    }

    public Group() {
        // Required empty public constructor
    }

    public String getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getGroupName() {
        return groupName;
    }
}
