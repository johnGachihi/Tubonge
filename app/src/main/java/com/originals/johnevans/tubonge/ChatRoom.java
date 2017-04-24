package com.originals.johnevans.tubonge;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom extends AppCompatActivity{

    Button sendBt;
    ImageView mateImage;
    TextView mateName;
    EditText message;
    SharedPreferences preferences;
    ArrayList<Message> messageArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();

        sendBt = (Button) findViewById(R.id.sendBt);
        mateImage = (ImageView) findViewById(R.id.mate_image);
        mateName = (TextView) findViewById(R.id.mate_name);
        message = (EditText) findViewById(R.id.messageTxt);

        String username = getIntent().getStringExtra("mate username");
        final String userid_R = getIntent().getStringExtra("mate id");
        final String iconPath = getIntent().getStringExtra("mate iconPath");

        Toast.makeText(getApplicationContext(), userid_R, Toast.LENGTH_LONG).show();

        mateName.setText(username);
        Picasso.with(getApplicationContext())
                .load(iconPath)
                .resize(100,100)
                .centerCrop()
                .into(mateImage);

        final DatabaseReference ref2push = databaseReference.push();

        sendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences = getSharedPreferences("user_pref", MODE_PRIVATE);
                String userid = preferences.getString("userid", null);
                String messageToSend = message.getText().toString();
                Message message = new Message(userid, iconPath, messageToSend, null, userid_R);
                ref2push.push().setValue(message);
            }
        });


        ref2push.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                message.setText("");
                Message message = dataSnapshot.getValue(Message.class);
                Log.e("database message", message.getMessage());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    class MessageAdapter extends ArrayAdapter {

        ArrayList<Message> messageArrayList = new ArrayList<>();

        public MessageAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
            super(context, resource, objects);
        }
    }
}
