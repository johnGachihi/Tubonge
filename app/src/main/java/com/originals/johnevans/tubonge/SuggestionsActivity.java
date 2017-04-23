package com.originals.johnevans.tubonge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SuggestionsActivity extends AppCompatActivity {

    GridView gridView;
    ArrayList<Mate> mateArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);
        gridView = (GridView) findViewById(R.id.suggested_users_grid);

        Intent intent = getIntent();
        String response = intent.getStringExtra("jsonResponse");
//        Log.e("response " + getClass().getName(), response);

        try {
            mateArrayList = getArraylistFromArray(intent);
            MatesAdapter matesAdapter = new MatesAdapter(mateArrayList);
            gridView.setAdapter(matesAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public ArrayList<Mate> getArraylistFromArray(Intent intent) throws JSONException {
        ArrayList<Mate> mateArrayList = new ArrayList<>();
        String response = intent.getStringExtra("jsonResponse");
        JSONArray responseArray = new JSONArray(response);
        for (int i = 0; i < responseArray.length(); i++) {
            JSONObject jsonMate = responseArray.getJSONObject(i);
            String username = jsonMate.getString("username");
            String iconPath = jsonMate.getString("icon_path");
            int similarInterests = jsonMate.getInt("similar_interests");
            mateArrayList.add(new Mate(username, iconPath, similarInterests));
        }
        return mateArrayList;
    }

    class  MatesAdapter extends BaseAdapter {

        private ArrayList<Mate> mates = new ArrayList<>();

        public MatesAdapter(ArrayList<Mate> mates) {
            this.mates = mates;
        }

        @Override
        public int getCount() {
            return mates.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                convertView = inflater.inflate(R.layout.mates_format, parent, false);
            }

            ImageView mateIcon = (ImageView) convertView.findViewById(R.id.mate_icon);
            Picasso.with(getApplicationContext())
                   .load(mates.get(position).getIcon_path())
                   .placeholder(R.drawable.ble)
                   .into(mateIcon);

            TextView textView = (TextView) convertView.findViewById(R.id.mate_username);
            textView.setText(mates.get(position).getUsername());

            return convertView;
        }
    }
}
