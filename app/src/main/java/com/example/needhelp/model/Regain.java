package com.example.needhelp.model;

public class Regain {
    public String id;
    public String friend;
    public String requesttype;

    public Regain(String id, String requesttype, String friend) {
        this.id = id;
        this.requesttype = requesttype;
        this.friend = friend;
    }

    public Regain() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequesttype() {
        return requesttype;
    }

    public void setRequesttype(String requesttype) {
        this.requesttype = requesttype;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

}
