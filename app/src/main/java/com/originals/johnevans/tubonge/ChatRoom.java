package com.originals.johnevans.tubonge;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ChatRoom extends AppCompatActivity{

    Button sendBt;
    ImageView mateImage;
    TextView mateName;
    EditText message;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("messages");

        sendBt = (Button) findViewById(R.id.sendBt);
        mateImage = (ImageView) findViewById(R.id.mate_image);
        mateName = (TextView) findViewById(R.id.mate_name);
        message = (EditText) findViewById(R.id.messageTxt);

        String username = getIntent().getStringExtra("mate username");
        String iconPath = getIntent().getStringExtra("mate iconPath");

        mateName.setText(username);
        Picasso.with(getApplicationContext())
                .load(iconPath)
                .resize(100,100)
                .centerCrop()
                .into(mateImage);

        sendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences = getSharedPreferences("user_pref", MODE_PRIVATE);
                String userid = preferences.getString("userid", null);
                String messageToSend = message.getText().toString();

            }
        });
    }
}
