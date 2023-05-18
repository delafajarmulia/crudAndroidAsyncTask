package com.example.atry.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atry.DetailUserActivity;
import com.example.atry.R;
import com.example.atry.model.UserModel;

import org.json.JSONArray;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.HolderData> {
    Context context;
    private static final String TAG = "RecyclerViewAdapter";
    List<UserModel> users;
    LayoutInflater inflater;
    public MainAdapter(List<UserModel> userModels){
        this.users = userModels;
        //this.inflater = LayoutInflater.from(context);
    }

//    JSONArray users;
//    public MainAdapter(List<UserModel> users){
//        this.users = users;
//    }

    @NonNull
    @Override
    public MainAdapter.HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_data_main, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.HolderData holder, int position) {
        UserModel user = users.get(position);
        int id = user.getId();
        holder.txtFName.setText(users.get(position).getFirstName());
        holder.txtEmail.setText(users.get(position).getEmail());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: ");
                Context ctx = view.getContext();
//                Toast.makeText(ctx, "clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ctx, DetailUserActivity.class);
                intent.putExtra("id", id);
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView txtFName, txtEmail;
        LinearLayout parentLayout;
        public HolderData(@NonNull View itemView) {
            super(itemView);

            txtEmail = itemView.findViewById(R.id.email);
            txtFName = itemView.findViewById(R.id.first_name);
            parentLayout = itemView.findViewById(R.id.parent_layout_main);
        }
    }
}
