package com.example.atry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ImageView;

import com.example.atry.adapter.GroupAdapter;
import com.example.atry.model.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GroupActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ArrayList<UserModel> usersGroup = new ArrayList<UserModel>();
    JSONArray data;
    ImageView viewAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        new callApi().execute();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Activity selectedActivity = null;
                switch (item.getItemId()){
                    case R.id.home:
                        selectedActivity = new MainActivity(); break;
                    case R.id.group:
                        selectedActivity = new GroupActivity(); break;
                    case R.id.profile:
                        selectedActivity = new ProfileActivity(); break;
                }

                Intent intent = new Intent(GroupActivity.this, selectedActivity.getClass());
                startActivity(intent);
                return true;
            }
        });
    }

    private class callApi extends AsyncTask<Void, Void, List<UserModel>>{

        @Override
        protected List<UserModel> doInBackground(Void... voids) {
            try {
                URL url = new URL("https://reqres.in/api/users?page=2");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-type", "application/json");
                Scanner scanner = new Scanner(con.getInputStream());
                StringBuffer buffer = new StringBuffer();
                while (scanner.hasNextLine()){
                    buffer.append(scanner.nextLine());
                }

                String responseBody = buffer.toString();
                JSONObject jsonObject = new JSONObject(responseBody);
                System.out.println("hey");
                data = jsonObject.getJSONArray("data");

                for(int i = 0; i < data.length(); i++){
                    usersGroup.add(new UserModel(
                            data.getJSONObject(i).getInt("id"),
                            data.getJSONObject(i).getString("first_name"),
                            data.getJSONObject(i).getString("email"),
                            data.getJSONObject(i).getString("avatar")
                    ));
                }

                System.out.println(usersGroup);
            } catch (Exception e){
                e.printStackTrace();
            }
            return usersGroup;
        }

        @Override
        protected void onPostExecute(List<UserModel> userGroup){
            super.onPostExecute(userGroup);

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_group);
            RecyclerView.Adapter adapter = new GroupAdapter(usersGroup);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(GroupActivity.this, 2);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(adapter);

            viewAvatar = (ImageView) findViewById(R.id.avatar_user_in_group);
            for (int i = 0; i < userGroup.size(); i++){
                System.out.println(userGroup.get(i).getUrlAvatar());
                new loadImage().execute(userGroup.get(i).getUrlAvatar());
            }

        }
    }

    private class loadImage extends AsyncTask<String, Void, Bitmap>{
        Bitmap bitmap;
        @Override
        protected Bitmap doInBackground(String... url) {
            try {
                InputStream stream = new URL(url[0]).openStream();
                bitmap = BitmapFactory.decodeStream(stream);
            } catch (Exception e){
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result){
            for(int i = 0; i < usersGroup.size(); i++){
                viewAvatar.setImageBitmap(result);
            }
        }
    }
}