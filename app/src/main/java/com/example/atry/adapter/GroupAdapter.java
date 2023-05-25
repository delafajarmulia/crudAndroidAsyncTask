package com.example.atry.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atry.R;
import com.example.atry.model.UserModel;

import java.util.List;
import java.util.zip.Inflater;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.HolderGroup> {
    List<UserModel> userGroup;
    public GroupAdapter(List<UserModel> userGroup){
        this.userGroup = userGroup;
    }
    @NonNull
    @Override
    public HolderGroup onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_data_group, parent, false);
        return new HolderGroup(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.HolderGroup holder, int position) {
        UserModel user = userGroup.get(position);
        holder.txtEmail.setText(userGroup.get(position).getEmail());
        holder.txtFirstName.setText(userGroup.get(position).getFirstName());
    }

    @Override
    public int getItemCount() {
        return userGroup.size();
    }

    public class HolderGroup extends RecyclerView.ViewHolder {
        TextView txtEmail, txtFirstName;
        LinearLayout parentLayout;
        public HolderGroup(@NonNull View itemView) {
            super(itemView);

            parentLayout = itemView.findViewById(R.id.parent_layout_group);
            txtEmail = itemView.findViewById(R.id.email_group);
            txtFirstName = itemView.findViewById(R.id.first_name_group);
        }
    }
}
