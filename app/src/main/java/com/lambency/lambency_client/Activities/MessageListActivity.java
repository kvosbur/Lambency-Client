package com.lambency.lambency_client.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lambency.lambency_client.Adapters.MessageListAdapter;
import com.lambency.lambency_client.R;
import com.lambency.lambency_client.Utils.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 3/31/2018.
 */

public class MessageListActivity extends AppCompatActivity {

    private RecyclerView mMessageRecycler;
    private MessageListAdapter myMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        List<Message> messageList = new ArrayList<Message>();

        Message m1 = new Message("Hello!", "Evan");
        Message m2 = new Message("How are you?", "Jim");
        Message m3 = new Message("I am good! Thanks!", "Evan");
        Message m4 = new Message("This is such a cool messaging system!", "Jim");
        Message m5 = new Message("Yeah, I know!", "Evan");
        Message m6 = new Message("I hope this is enough", "Jim");
        Message m7 = new Message("Should be!", "Evan");

        messageList.add(m1); messageList.add(m2); messageList.add(m3);
        messageList.add(m4); messageList.add(m5); messageList.add(m6);
        messageList.add(m7);

        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        myMessageAdapter = new MessageListAdapter(this, messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(myMessageAdapter);
        myMessageAdapter.notifyDataSetChanged();
    }
}