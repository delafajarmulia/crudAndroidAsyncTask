package com.example.atry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class UpdateActivity extends AppCompatActivity {
    TextView txtName, txtJob;
    Button btnEdit;
    String name, job;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Bundle extras = getIntent().getExtras();
        id = extras.getInt("id");

        txtName = findViewById(R.id.edit_name);
        txtJob = findViewById(R.id.edit_job);
        btnEdit = findViewById(R.id.btn_edit_user);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = txtName.getText().toString();
                job = txtJob.getText().toString();

                new UpdateUser().execute();
            }
        });
    }

    private class UpdateUser extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;

            try {
                JSONObject updateData = new JSONObject();
                updateData.put("name", name);
                updateData.put("job", job);

                URL url =  new URL("https://reqres.in/api/users/" + id);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setChunkedStreamingMode(0);

                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(updateData.toString());
                writer.flush();

                int code = urlConnection.getResponseCode();
                if (code != 200){
                    throw new IOException("invalid response from server "+ code);
                }

                System.out.println(code);
                BufferedReader bf = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line = bf.readLine()) != null){
                    Log.i("data", line);
                }

                Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                startActivity(intent);
            } catch (Exception e){
                e.printStackTrace();
            }finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }
}