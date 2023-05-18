package com.example.atry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.atry.adapter.MainAdapter;
import com.example.atry.model.UserModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    JSONArray data;
    ArrayList<UserModel> users = new ArrayList<UserModel>();
    private RecyclerView recyclerView;
    private MainAdapter adapter;
    private FloatingActionButton btnFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFab = findViewById(R.id.fab);
        new callApi().execute();

        btnFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });
    }

    private class callApi extends AsyncTask<String, String,String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("https://reqres.in/api/users?page=1");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");
                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuffer sb = new StringBuffer();
                while (scanner.hasNextLine()){
                    sb.append(scanner.nextLine() + "\n");
                }
                String responseBody = sb.toString();
                scanner.close();
                System.out.println("Get response code :" +conn.getResponseCode());
                System.out.println("Get response body :" +responseBody);
                JSONObject jsonObject = new JSONObject(responseBody);
                System.out.println(jsonObject.getString("page"));
                String page = jsonObject.getString("page");
                data = jsonObject.getJSONArray("data");

                for(int i = 0; i < data.length(); i++) {
                    users.add(new UserModel(
                            data.getJSONObject(i).getInt("id"),
                            data.getJSONObject(i).getString("first_name"),
                            data.getJSONObject(i).getString("email")
                    ));
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);

            recyclerView = (RecyclerView) findViewById(R.id.rv_main);
            adapter = new MainAdapter( users);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}