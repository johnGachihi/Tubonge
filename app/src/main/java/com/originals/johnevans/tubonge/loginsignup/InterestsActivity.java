package com.originals.johnevans.tubonge.loginsignup;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

public class InterestsActivity extends AppCompatActivity{

    static ArrayList<InterestObject> interestArray;
    ImageView imageView;
    TextView textView;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        interestArray = getInterestsArrays();

        /*for (int i = 0; i<interestArray.size(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(interestArray.get(i));
            Log.e("array", stringBuilder.toString());
        }*/

        ArrayList<String> strings = new ArrayList<>();
        strings.add("john");
        strings.add("Kamau");
        listView = (ListView) findViewById(R.id.interest_list);
        listView.setAdapter(new InterestsAdapter(getApplicationContext(), R.layout.interest_list_format, interestArray));

    }

    public ArrayList<InterestObject> getInterestsArrays() {
        final ArrayList<String> interestNames = new ArrayList<>();
        final ArrayList<String> interestPaths = new ArrayList<>();
        final ArrayList<InterestObject> interestObjects = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST,
                "http://192.168.0.13/tubonge_app/interests_get.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONObject jsonInterests = jsonResponse.getJSONObject("interests");

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
                                    new InterestObject(interestPaths.get(i), interestNames.get(i)));

                            }
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
        Toast.makeText(getApplicationContext(), ""+interestObjects.size(), Toast.LENGTH_LONG).show();
        return  interestObjects;
    }

    class InterestsAdapter extends ArrayAdapter{

        List<InterestObject> list = new ArrayList<>();

        InterestsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
            super(context, resource, objects);
            this.list = objects;
        }

        public InterestsAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
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
//            InterestObject interestObject = list.get(position);
            textView.setText(list.get(position).getInterest());
            Picasso.with(getApplicationContext())
                   .load("http://localhost/tubonge_app/interestIcons/eating/drawable-xxxhdpi/ic_local_dining_black_24dp.png")
                   .error(R.drawable.ble)
                   .into(imageView);

            return convertView;
        }
    }
}
