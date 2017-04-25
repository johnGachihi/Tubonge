package com.originals.johnevans.tubonge;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatRoom extends AppCompatActivity{

    Button sendBt;
    ImageView mateImage;
    TextView mateName;
    EditText message;
    ListView chatList;
    SharedPreferences preferences;
    ArrayList<Message> messageArrayList = new ArrayList<>();
    static String gravity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();

        preferences = getSharedPreferences("user_pref", MODE_PRIVATE);
        final String userid = preferences.getString("userid", null);

        sendBt = (Button) findViewById(R.id.sendBt);
        mateImage = (ImageView) findViewById(R.id.mate_image);
        mateName = (TextView) findViewById(R.id.mate_name);
        message = (EditText) findViewById(R.id.messageTxt);

        String username = getIntent().getStringExtra("mate username");
        final String userid_R = getIntent().getStringExtra("mate id");
        final String iconPath = getIntent().getStringExtra("mate iconPath");

        chatList = (ListView) findViewById(R.id.chatlist);
        final MessageAdapter messageAdapter = new MessageAdapter(messageArrayList);
        chatList.setAdapter(messageAdapter);

        Toast.makeText(getApplicationContext(), userid_R, Toast.LENGTH_LONG).show();

        mateName.setText(username);
        Picasso.with(getApplicationContext())
                .load(iconPath)
                .resize(100,100)
                .centerCrop()
                .into(mateImage);

        final DatabaseReference ref2push = databaseReference.push();
        DatabaseReference ref3push = firebaseDatabase.getReference(ref2push.getKey());

        sendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences = getSharedPreferences("user_pref", MODE_PRIVATE);
                String userid = preferences.getString("userid", null);
                String messageToSend = message.getText().toString();
                Calendar calendar = Calendar.getInstance();
                Message message = new Message(userid, iconPath, messageToSend, null, userid_R, calendar.getTime());
                ref2push.push().setValue(message);
            }
        });


        ref3push.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                Log.e("database message", message.getMessage());
                if (message.getReceiverId().equals(userid) || message.getSenderId().equals(userid)) {
                    messageArrayList.add(message);
                    messageAdapter.notifyDataSetChanged();
                    if (message.getReceiverId().equals(userid)) {
                        gravity = "left";
                    } else {
                        gravity = "right";
                    }
                }

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

    class MessageAdapter extends BaseAdapter {

        ArrayList<Message> messageArrayList = new ArrayList<>();

        public MessageAdapter(ArrayList<Message> arrayList) {
            this.messageArrayList = arrayList;
        }

        @Override
        public int getCount() {
            return messageArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                convertView = inflater.inflate(R.layout.message_item_format, parent, false);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.the_message);
            textView.setText(messageArrayList.get(position).getMessage());
            if (gravity.equals("right")) {
                textView.setGravity(Gravity.RIGHT);
            } else {
                textView.setGravity(Gravity.LEFT);
            }

                return  convertView;
        }
    }
}
