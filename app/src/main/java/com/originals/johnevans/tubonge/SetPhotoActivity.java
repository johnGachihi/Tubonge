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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class SetPhotoActivity extends AppCompatActivity {

    ImageView setPhoto;
    ImageView imageView2;
    Button setPhoto_bt;
   // Context context;
    public static Uri uri1;
    static Bitmap photo;
    static Bitmap photophoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_photo);
        final Context context = this;

        setPhoto = (ImageView) findViewById(R.id.imageView);
        imageView2 = (ImageView) findViewById(R.id.imageView2);

        setPhoto_bt = (Button) findViewById(R.id.setPhoto_bt);
        setPhoto_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePhoto.resolveActivity(getPackageManager()) != null) {
                    File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                   /* if (new File(file, "my_icon_tubongee.jpg").exists()) {
                        if (new File(file, "my_icon_tubongee.jpg").delete()) {
                            Toast.makeText(getApplicationContext(), "deleted", Toast.LENGTH_LONG).show();
                        }

                    }
                    File imageFile = new File(file, "my_icon_tubongee.jpg");*/
                   File imageFile = null;
                    try {
                        imageFile = File.createTempFile("icon", ".jpg", file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (imageFile != null) {
                        uri1 = Uri.fromFile(imageFile);
                        takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, uri1);
                        startActivityForResult(takePhoto, 1);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Picasso.with(getApplicationContext()).load(uri1).resize(50, 50).centerCrop().into(setPhoto);
        }
        File imageFile = new File(uri1.getPath());
        Bitmap image = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        imageView2.setImageBitmap(image);

        String imageString = getStringImage(image);
        uploadImage(imageString);
    }

    public String getStringImage(Bitmap b) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageString;
    }

    public void uploadImage(final String imageString) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.0.13/tubonge_app/IconSet.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SharedPreferences pref = getSharedPreferences("user_pref", MODE_PRIVATE);
                String userid = pref.getString("userid", null);
                HashMap<String, String> params = new HashMap<>();
                params.put("icon", imageString);
                params.put("userid", userid);
                return params;
            }
        };

        requestQueue.add(request);
    }
}
