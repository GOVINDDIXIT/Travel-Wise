package com.example.needhelp;


import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class Group {

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
