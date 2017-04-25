package com.originals.johnevans.tubonge.loginsignup;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.originals.johnevans.tubonge.R;
import com.originals.johnevans.tubonge.SetPhotoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by John on 4/3/2017.
 */

public class LoginActivity extends AppCompatActivity {

    Button button, signin_bt;
    EditText email_et;
    EditText password_et;
    String user_firstname;
    String user_username;
    String user_email;
    String user_userid;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Window window = getWindow();
        window.setTitle("Sign up");

        email_et = (EditText) findViewById(R.id.email_loginEt);
        password_et = (EditText) findViewById(R.id.password_loginEt);

        button = (Button) findViewById(R.id.signup_bt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpSection();
            }
        });

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading");

        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Error")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        signin_bt = (Button) findViewById(R.id.signinBt_login);
        signin_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("signin_bt","clicked");
                final String email = email_et.getText().toString();
                final String password = password_et.getText().toString();

                AlertDialog dialog;
                if (!email.contains("@") && password.length()<4) {
                    builder.setMessage("Invalid email. Passord too short");
                    dialog = builder.create();
                    dialog.show();
                } else if (!email.contains("@")) {
                    builder.setMessage("Invalid email");
                    dialog = builder.create();
                    dialog.show();
                } else if (password.length()<4) {
                    builder.setMessage("Passord too short");
                    builder.setCancelable(false);
                    dialog = builder.create();
                    dialog.show();
                } else {
                    loginUser(email, password, progressDialog);
                    progressDialog.show();
                }

            }
        });
    }

    public void openSignUpSection() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    public void loginUser(final String email, final String password, final ProgressDialog progressDialog) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://192.168.0.13/tubonge_app/login.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        JSONObject userJson = jsonObject.getJSONObject("user");
                        user_firstname = userJson.getString("firstname");
                        user_userid = jsonObject.getString("userid");
                        preferences = getSharedPreferences("user_pref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("userid", user_userid);
                        editor.apply();
                        Log.e("userid", preferences.getString("userid", null));
                        Toast.makeText(getApplicationContext(), "Login by: " + user_firstname, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, InterestsActivity.class);
                        startActivity(intent);
                    } else {
                        String error_message = jsonObject.getString("error_message");
                        Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        requestQueue.add(request);
    }
}
