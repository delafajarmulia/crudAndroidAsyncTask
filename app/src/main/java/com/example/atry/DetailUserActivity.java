package com.example.atry;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.atry.model.UserDetail;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class DetailUserActivity extends AppCompatActivity {

    UserDetail userDetail;
    int id, idFromIntent;
    ImageView avatar;
    String firstName, lastName, email;
    Button btnUpdate, btnDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        Bundle extras = getIntent().getExtras();
        idFromIntent = extras.getInt("id");
        new callApi().execute();

        btnUpdate = findViewById(R.id.btn_edit_user);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailUserActivity.this, UpdateActivity.class);
                intent.putExtra("id", idFromIntent);
                startActivity(intent);
            }
        });
        ////new DownloadImageTask(avatar).execute();

        btnDelete = findViewById(R.id.btn_delete_user);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new deleteUser().execute();
            }
        });
    }

    private class callApi extends AsyncTask<String, String, UserDetail>{

        @Override
        protected UserDetail doInBackground(String... strings) {
            try {
                URL url = new URL("https://reqres.in/api/users/" + idFromIntent);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");
                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuffer buffer = new StringBuffer();
                while (scanner.hasNextLine()){
                    buffer.append(scanner.nextLine());
                }

                String responseBody = buffer.toString();
                scanner.close();

                JSONObject jsonObject = new JSONObject(responseBody);
                JSONObject data = jsonObject.getJSONObject("data");
                return new UserDetail(
                        data.getInt("id"),
                        data.getString("first_name"),
                        data.getString("last_name"),
                        data.getString("email"),
                        data.getString("avatar")
                );
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(UserDetail s){
            super.onPostExecute(s);

            avatar = (ImageView) findViewById(R.id.avatar_user);
            TextView id = (TextView) findViewById(R.id.user_id);
            TextView name = (TextView) findViewById(R.id.user_fl_name);
            TextView txtEmail = (TextView) findViewById(R.id.user_email);

            id.setText("Id :" + s.getId());
            name.setText(s.getFirstName() + " " + s.getLastName());
            txtEmail.setText(s.getEmail());

            new DownloadImage().execute(s.getAvatar());
        }
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... url) {
            String imageURL = url[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e){
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result){
            avatar.setImageBitmap(result);
        }
    }

    private class deleteUser extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("https://reqres.in/api/users/" + idFromIntent);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("DELETE");
                con.setRequestProperty("Content-type", "application/json");

                int code = con.getResponseCode();
                System.out.println(code);

                Intent intent = new Intent(DetailUserActivity.this, MainActivity.class);
                startActivity(intent);
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}