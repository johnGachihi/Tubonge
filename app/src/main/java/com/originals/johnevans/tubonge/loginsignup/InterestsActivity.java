package com.originals.johnevans.tubonge.loginsignup;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.originals.johnevans.tubonge.InterestObject;
import com.originals.johnevans.tubonge.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InterestsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        getInterestsArrays();

    }

    public ArrayList<InterestObject> getInterestsArrays() {
        final ArrayList<String> interestsNames = new ArrayList<>();
        final ArrayList<String> interestPaths = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST,
                "http://192.168.0.13/tubonge_app/interests_get.php)",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONObject jsonInterests = jsonResponse.getJSONObject("interests");
                            JSONArray namesArrayJson = jsonInterests.getJSONArray("name");
                            for (int i = 0; i < namesArrayJson.length(); i++) {
                                interestsNames.add(namesArrayJson.getString(i));
                            }
                            StringBuilder names = new StringBuilder();
                            for (int i = 0; i < interestsNames.size(); i++) {
                                names.append(interestsNames.get(i));
                            }
                            Log.e("names", names.toString());


                            JSONArray pathsArrayJson = jsonResponse.getJSONArray("path");
                            for (int i = 0; i < pathsArrayJson.length(); i++) {
                                interestPaths.add(pathsArrayJson.getString(i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(request);
        return null;
    }

    class InterestsAdapter extends ArrayAdapter{

        public InterestsAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


            return super.getView(position, convertView, parent);
        }
    }
}
