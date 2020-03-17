package com.example.needhelp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.needhelp.R;
import com.example.needhelp.activity.Message;
import com.example.needhelp.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> lst;

    public UserAdapter(Context mContext, List<User> lst) {
        this.mContext = mContext;
        this.lst = lst;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final User user = lst.get(position);
        holder.username.setText(user.getUsername_item());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Message.class);
                intent.putExtra("ID", user.getId());
                intent.putExtra("imgUrl", user.getImageURL());
                mContext.startActivity(intent);
            }
        });

        Picasso.get()
                .load(user.getImageURL())
                .resize(120, 120)
                .into(holder.imageUrl);
    }

    @Override
    public int getItemCount() {
        return lst.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        LinearLayout linearLayout;
        CircleImageView imageUrl;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_item);
            linearLayout = itemView.findViewById(R.id.linearlayout);
            imageUrl = itemView.findViewById(R.id.profile_image_chat);
        }
    }
}
