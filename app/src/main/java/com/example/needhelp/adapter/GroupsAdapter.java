package com.example.needhelp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.needhelp.Group;
import com.example.needhelp.R;

import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> {
    private Context mContext;
    private List<Group> lst;

    public GroupsAdapter(Context mContext, List<Group> lst) {
        this.mContext = mContext;
        this.lst = lst;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.group_item, parent, false);
        return new GroupsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Group group = lst.get(position);
        holder.groupNameTextView.setText(group.getGroupName());
    }

    @Override
    public int getItemCount() {
        return lst.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView groupNameTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            groupNameTextView = itemView.findViewById(R.id.groupName);
        }
    }
}
