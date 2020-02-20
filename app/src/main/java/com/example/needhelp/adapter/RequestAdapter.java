package com.example.needhelp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.needhelp.R;
import com.example.needhelp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.Viewholder> {


    private Context context;
    private List<User> users;
    DatabaseReference databaseReference;
    FirebaseUser userr;

    public RequestAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_item, parent, false);
        return new RequestAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        userr = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Friend_Request");
        final User user = users.get(position);
        final String s = user.getId();
        holder.name.setText(user.getUsername_item());
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("friend", "false");
                databaseReference.child(s).child(userr.getUid()).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @SuppressLint("ShowToast")
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(view.getContext(), " Now you can chat ", Toast.LENGTH_SHORT);
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {

        TextView name;
        Button accept;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.NAME);
            accept = itemView.findViewById(R.id.buttonacc);
        }
    }
}
