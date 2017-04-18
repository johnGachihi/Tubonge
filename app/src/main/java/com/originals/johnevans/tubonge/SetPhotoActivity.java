package com.originals.johnevans.tubonge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class SetPhotoActivity extends AppCompatActivity {

    ImageView setPhoto;
    Button setPhoto_bt;
    Context context;
    static Uri uri1;
    static Bitmap photo;
    static Bitmap photophoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_photo);
        final Context context = this;

        setPhoto = (ImageView) findViewById(R.id.imageView);

        setPhoto_bt = (Button) findViewById(R.id.setPhoto_bt);
        setPhoto_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePhoto.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePhoto, 1);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            Bundle photo_b = data.getExtras();
            Bitmap photo;
            photo = (Bitmap) photo_b.get("data");
            setPhoto = (ImageView) findViewById(R.id.imageView);
            setPhoto.setImageBitmap(photo);
            new SetIconAsync().execute(photo);
        }
    }



    public class SetIconAsync extends AsyncTask<Bitmap, Void, Bitmap> {

       // Bitmap photo;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Bitmap[] params) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            params[0].compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] imageByte = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageByte, Base64.DEFAULT);
            Bitmap imagephoto = postIcon(encodedImage);
            return imagephoto;
        }

        @Override
        protected void onProgressUpdate(Void[] values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Bitmap b) {
            ImageView imageView = (ImageView) findViewById(R.id.imageView2);
            imageView.setImageBitmap(b);

        }

        public Bitmap postIcon(final String encodedImage) {
            Log.e("postIcon", "post iconning");
            final Bitmap[] photo = new Bitmap[1];
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            String url = "http://192.168.0.13/tubonge_app/IconSet.php";
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean error = jsonObject.getBoolean("error");
                        if (!error) {
                            JSONObject userJson = jsonObject.getJSONObject("user");
                            String image = userJson.getString("icon");

                            byte[] imageByte = image.getBytes();
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inPurgeable = true;
                            photo[0] = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length, options);
                            if (photo[0] == null) {
                                Log.e("photo is null", "true");
                            } else {
                                Log.e("photo is null", "false");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.toString()+"---"+e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString()+"---"+error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("icon", encodedImage);
                    SharedPreferences preferences = getSharedPreferences("user_pref", Context.MODE_PRIVATE);
                    String userid = preferences.getString("userid", null);
                    params.put("userid", userid);
                    return params;
                }
            };

            requestQueue.add(request);
            String bool;
            if (photo != null) {
                bool = "false";
            } else {bool = "true";}
            Log.e("is photo null?", bool);
            return photo[0];
        }
    }

}
