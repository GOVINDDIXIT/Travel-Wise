package com.example.needhelp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.needhelp.R;
import com.example.needhelp.activity.HelpCall;
import com.example.needhelp.model.Upload;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyUploadAdapterr extends RecyclerView.Adapter<MyUploadAdapterr.ViewHolder> {

    private Context mContext;
    private List<Upload> mUploads;

    public MyUploadAdapterr(Context mContext, List<Upload> mUploads) {
        this.mContext = mContext;
        this.mUploads = mUploads;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.past_rides_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final Upload upload = mUploads.get(position);
        holder.ffrom.setText(upload.getFrom());
        holder.tto.setText(upload.getTo());
        holder.description.setText(upload.getDescription());
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat dateformat = new SimpleDateFormat("dd MMM,yyy hh:mm aa");
        final long time = Long.parseLong(upload.getTime());
        holder.time.setText(dateformat.format(time));

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.keepSynced(true);
                Query applesQuery = ref.child("item_details").orderByChild("time").equalTo(upload.getTime());
                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            snapshot.getRef().removeValue();
                            Toast.makeText(mContext, "Delete Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(mContext, databaseError.toException().toString(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        holder.edit_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.keepSynced(true);
                Query query = reference.child("item_details").orderByChild("time").equalTo(upload.getTime());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Intent intent = new Intent(view.getContext(), HelpCall.class);
                            intent.putExtra("value", snapshot.getRef().getKey());
                            intent.putExtra("time", upload.getTime());
                            intent.putExtra("from_intent", upload.getFrom());
                            intent.putExtra("to_intent", upload.getTo());
                            intent.putExtra("desc_intent", upload.getDescription());
                            intent.putExtra("mode_select_intent", upload.getRide_type());
                            intent.putExtra("companions_intetn", upload.getCompanions());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }


    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView ffrom, tto, description, username_item, time, email;
        RelativeLayout relativeLayout;
        CircleImageView delete, edit_data;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ffrom = itemView.findViewById(R.id.from);
            tto = itemView.findViewById(R.id.to);
            description = itemView.findViewById(R.id.description);
            // username_item = itemView.findViewById(R.id.username_item);
            time = itemView.findViewById(R.id.time);
            relativeLayout = itemView.findViewById(R.id.relative);
            // email = itemView.findViewById(R.id.emailwa);
            edit_data = itemView.findViewById(R.id.edit_data);
            delete = itemView.findViewById(R.id.delete_data);
        }
    }
}
