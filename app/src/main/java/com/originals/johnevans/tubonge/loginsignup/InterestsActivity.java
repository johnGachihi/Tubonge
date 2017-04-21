package com.originals.johnevans.tubonge.loginsignup;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.originals.johnevans.tubonge.InterestObject;
import com.originals.johnevans.tubonge.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterestsActivity extends AppCompatActivity{

    ImageView imageView;
    TextView textView;
    ListView listView;
    ArrayList<InterestObject> interestObjects = new ArrayList<>();
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        interestObjects = getInterestsArrays();

        listView = (ListView) findViewById(R.id.interest_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                if (!checkBox.isChecked()) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
                int interestId = interestObjects.get(position).getInterestId();
                String name = interestObjects.get(position).getInterest();
                Log.e("interest", name + " " +interestId);
            }
        });
    }

    public ArrayList<InterestObject> getInterestsArrays() {
        final ArrayList<Integer> interestIds = new ArrayList<>();
        final ArrayList<String> interestNames = new ArrayList<>();
        final ArrayList<String> interestPaths = new ArrayList<>();
        final ArrayList<InterestObject> interestObjects = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST,
                "http://192.168.0.13/tubonge_app/interests_get.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONObject jsonInterests = jsonResponse.getJSONObject("interests");

                            JSONArray idArrayJson = jsonInterests.getJSONArray("interest_id");
                            for (int i = 0; i<idArrayJson.length(); i++) {
                                interestIds.add(idArrayJson.getInt(i));
                            }

                            JSONArray namesArrayJson = jsonInterests.getJSONArray("name");
                            for (int i = 0; i < namesArrayJson.length(); i++) {
                                interestNames.add(namesArrayJson.getString(i));
                            }

                            JSONArray pathsArrayJson = jsonInterests.getJSONArray("path");
                            for (int i = 0; i < pathsArrayJson.length(); i++) {
                                interestPaths.add(pathsArrayJson.getString(i));
                            }

                            for (int i=0; i<interestNames.size(); i++) {
                                interestObjects.add(
                                    new InterestObject(interestIds.get(i), interestPaths.get(i), interestNames.get(i)));
                            }

                            listView.setAdapter(
                                    new InterestsAdapter(InterestsActivity.this, R.layout.interest_list_format, interestObjects)
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);
        Toast.makeText(getApplicationContext(), "bleeee  "+interestObjects.size(), Toast.LENGTH_LONG).show();
        return  interestObjects;
    }

    public ArrayList<Integer> addInterestsToArray(InterestObject interestObject) {
        int interestId = interestObject.getInterestId();
        ArrayList<Integer> interestIds = new ArrayList<>();
        interestIds.add(interestId);
        return interestIds;
    }

    public void postInterests(final ArrayList<Integer> interestIds) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE);
        final String userid = sharedPreferences.getString("userid", null);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, " ", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("userid", userid);
                for (int i = 0; i<interestIds.size(); i++) {
                    params.put("interestid[" + i + "]", "" + interestIds.get(i));
                }
                return super.getParams();
            }
        };
    }

    class InterestsAdapter extends ArrayAdapter{

        List<InterestObject> list = new ArrayList<>();

        InterestsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
            super(context, resource, objects);
            this.list = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.interest_list_format, parent, false);
            }
            imageView = (ImageView) convertView.findViewById(R.id.interest_icon);
            textView = (TextView) convertView.findViewById(R.id.interest_name);
            textView.setText(list.get(position).getInterest());
            Picasso.with(getApplicationContext())
                   .load(list.get(position).getIconPath())
                   .error(R.drawable.ble)
                   .into(imageView);

            return convertView;
        }
    }
}
