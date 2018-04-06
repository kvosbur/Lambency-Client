package com.lambency.lambency_client.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.lambency.lambency_client.Adapters.MessageListAdapter;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.R;
import com.lambency.lambency_client.Utils.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  Base code taken from tutorial here: https://blog.sendbird.com/android-chat-tutorial-building-a-messaging-ui
 *  Added onto and modified by Evan Honeysett
 */

public class MessageListActivity extends AppCompatActivity {

    private RecyclerView mMessageRecycler;
    private MessageListAdapter myMessageAdapter;

    @BindView(R.id.button_chatbox_send)
    Button sendButton;

    @BindView(R.id.edittext_chatbox)
    EditText messageContent;

    public List<Message> messageList;

    private FirebaseListAdapter<Message> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message_list);
        ButterKnife.bind(this);

        messageList = new ArrayList<Message>();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Messaging");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Message m1 = new Message("Hello!", "Evan");
        Message m2 = new Message("How are you?", "Jim");
        Message m3 = new Message("I am good! Thanks!", "Evan");
        Message m4 = new Message("This is such an amazing messaging system!", "Jim");
        Message m5 = new Message("Yeah, I know!", "Evan");
        Message m6 = new Message("I hope this is enough", "Jim");
        Message m7 = new Message("Should be!", "Evan");

        messageList.add(m1); messageList.add(m2); messageList.add(m3);
        messageList.add(m4); messageList.add(m5); messageList.add(m6);
        messageList.add(m7);

        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        myMessageAdapter = new MessageListAdapter(this, messageList);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        mMessageRecycler.setLayoutManager(llm);
        mMessageRecycler.setAdapter(myMessageAdapter);
        myMessageAdapter.notifyDataSetChanged();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("message");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                messageList.add(new Message(value, "Evan"));
                myMessageAdapter.notifyDataSetChanged();
                mMessageRecycler.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageContent.getText().toString();

                if(message.compareTo("") == 0)
                {
                    return;
                }

                if(messageList.size() > 0) {
                    messageList.get(messageList.size()-1).createdAt = "";
                }

                /*
                FirebaseMessaging fm = FirebaseMessaging.getInstance();
                fm.send(new RemoteMessage.Builder("525242144476@gcm.googleapis.com")
                        .setMessageId(Integer.toString(1))
                        .addData("my_message", "Hello World")
                        .addData("my_action","SAY_HELLO")
                        .build());
                        */

                /*
                FirebaseDatabase.getInstance()
                        .getReference()
                        .push()
                        .setValue(new Message("Hello World With Firebase", "Evan"));

                /*
                Message m1 = new Message(message, UserModel.myUserModel.getFirstName());
                SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
                Date now = new Date();
                String strDate = sdf.format(now);
                m1.createdAt = strDate;
                messageList.add(m1);
                myMessageAdapter.notifyDataSetChanged();
                messageContent.setText("");
                */
                myRef.setValue(message);

            }
        });
    }


    public void populateMessage() {

        Query query = FirebaseDatabase.getInstance().getReference("/TestSpace").orderByKey();

        FirebaseListOptions<Message> options = new FirebaseListOptions.Builder<Message>()
                .setLayout(R.layout.activity_message_list)//Note: The guide doesn't mention this method, without it an exception is thrown that the layout has to be set.
                .setQuery(query, Message.class)
                .build();

        /*
        adapter = new FirebaseListAdapter<Message>(this, Message.class,
                R.layout.activity_message_list, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, Message model, int position) {
                // Get references to the views of message.xml
                messageList.add(model);
                myMessageAdapter.notifyDataSetChanged();
            }
        };
        */
        adapter = new FirebaseListAdapter<Message>(options) {
            @Override
            protected void populateView(View v, Message model, int position) {
                // Get references to the views of message.xml
                Toast.makeText(MessageListActivity.this, "Hello!", Toast.LENGTH_SHORT).show();
                messageList.add(model);
                myMessageAdapter.notifyDataSetChanged();
            }
        };

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}