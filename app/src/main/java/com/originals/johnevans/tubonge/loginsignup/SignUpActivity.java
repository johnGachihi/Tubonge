package com.originals.johnevans.tubonge.loginsignup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.originals.johnevans.tubonge.InformationHolder;
import com.originals.johnevans.tubonge.R;
import com.originals.johnevans.tubonge.SetPhotoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity
        implements SignUpNamesFragment.OnNextClicked, SignUpEmailFragment.OnNextClicked {

    EditText editText;
    View fragmentView;
    static String afirstname;
    static String ausername;
    static String aemail;
    static String apassword;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);
        progressDialog = new ProgressDialog(SignUpActivity.this);

        SignUpNamesFragment fragment = new SignUpNamesFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.signup_activity, fragment, "first")
                   .commit();
    }


    @Override
    public void onNextClicked(String firstname, String username) {
        afirstname = firstname;
        ausername = username;
        Log.e("firstname, username", afirstname + ausername);
    }

    @Override
    public void getDataFromFragment(String email, String password) {

        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        aemail = email;
        apassword = password;
        Log.e("email, password", aemail + apassword);
        registerUser(ausername, afirstname, aemail, apassword);
    }

    public void registerUser(final String username, final String firstname, final String email, final String password) {
        Log.e("registerUser", "let's see");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.e("registerUser1", "let's see");
        String ip = new InformationHolder().IP;
        final String url = "http://" + ip + "/tubonge_app/register.php";
        if (requestQueue.equals(null) || requestQueue==null) {
            Log.e("requestQueue", "let's see");
        } else {
            Log.e("requestQueue1", "let's see");
        }

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("json response", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean state = jsonObject.getBoolean("error");
                    if (!state) {
                        String userid = jsonObject.getString("userid");
                        SharedPreferences preferences = getSharedPreferences("user_pref",MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("userid", userid);
                        editor.apply();
                        startActivity(new Intent(SignUpActivity.this, SetPhotoActivity.class));
                    } else {
                        String error_message = jsonObject.getString("error_message");
                        Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("firstname", firstname);
                params.put("email", email);
                params.put("birthday", "");
                params.put("password", password);
                return params;
            }
        };
        requestQueue.add(request);
    }

}
