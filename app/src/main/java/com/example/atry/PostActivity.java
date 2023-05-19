package com.example.atry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostActivity extends AppCompatActivity {
    private Button btnAdd;
    private EditText txtName, txtJob;
    private String name, job;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        txtName = findViewById(R.id.input_name);
        txtJob = findViewById(R.id.input_job);
        btnAdd = findViewById(R.id.btn_add_user);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = txtName.getText().toString();
                job = txtJob.getText().toString();

                new PostTaskUser().execute();
            }
        });
    }

    private class PostTaskUser extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;

            try {
                JSONObject postData = new JSONObject();
                postData.put("name", name);
                postData.put("job", job);
                System.out.println("ehy");

                URL url = new URL("https://reqres.in/api/users");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setChunkedStreamingMode(0);

                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(postData.toString());
                writer.flush();

                int code =  urlConnection.getResponseCode();
                if(code != 201){
                    throw new IOException("Invalid response from server :" +code);
                }

                BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null){
                    Log.i("data", line);
                }

                Log.d("data", "successfully add new user");
                Intent intent = new Intent(PostActivity.this, MainActivity.class);
                startActivity(intent);
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }
}