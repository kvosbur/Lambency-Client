package com.lambency.lambency_client.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

                Message m1 = new Message(message, UserModel.myUserModel.getFirstName());
                SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
                Date now = new Date();
                String strDate = sdf.format(now);
                m1.createdAt = strDate;
                messageList.add(m1);
                myMessageAdapter.notifyDataSetChanged();
                messageContent.setText("");
                mMessageRecycler.scrollToPosition(messageList.size() - 1);
            }
        });
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