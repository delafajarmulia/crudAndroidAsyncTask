package com.example.atry;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        Bundle extras = getIntent().getExtras();
        idFromIntent = extras.getInt("id");
        new callApi().execute();
        ////new DownloadImageTask(avatar).execute();
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
//                id = jsonObject.getInt("id");
//                firstName = jsonObject.getString("first_name");
//                lastName = jsonObject.getString("last_name");
//                email = jsonObject.getString("email");
//                avatar = jsonObject.getString("avatar");
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
            TextView fName = (TextView) findViewById(R.id.user_f_name);
            TextView lName = (TextView) findViewById(R.id.user_l_name);
            TextView txtEmail = (TextView) findViewById(R.id.user_email);

            id.setText("User id :" + s.getId());
            fName.setText("First name: " + s.getFirstName());
            lName.setText("Last name : " + s.getLastName());
            txtEmail.setText("Email : " + s.getEmail());

//            try {
//                URL urlImage =  new URL(s.getAvatar());
//                Bitmap bmp = BitmapFactory.decodeStream(urlImage.openConnection().getInputStream());
//                avatar.setImageBitmap(bmp);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }

            //new DownloadImageTask((ImageView) findViewById(R.id.avatar_user)).execute(s.getAvatar());
//            UrlImage
//            Bitmap imgBitmap = BitmapFactory.decodeFile(s.getAvatar());
//            avatar.setImageBitmap(imgBitmap);
//            avatar.setImageURI(s.getAvatar());
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
        ImageView bmImage;
        @Override
        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            }catch (Exception e){
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected  void onPostExecute(Bitmap result){
            bmImage.setImageBitmap(result);
        }
    }

//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
//        ImageView bmImage;
//        public DownloadImageTask(ImageView bmImage){
//            this.bmImage = bmImage;
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... urls) {
//            String urlDisplay = urls[0];
//            Bitmap bmp = null;
//            try {
//                URL url = new URL(userDetail.getAvatar());
//
//                InputStream in = new URL(urlDisplay).openStream();
//                bmp = BitmapFactory.decodeStream(in);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            return bmp;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap result){
//            bmImage.setImageBitmap(result);
//        }
//    }
}