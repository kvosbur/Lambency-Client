package com.lambency.lambency_client.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lambency.lambency_client.Adapters.MessageListAdapter;
import com.lambency.lambency_client.Models.ChatModel;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;
import com.lambency.lambency_client.Models.MessageModel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *  Base code taken from tutorial here: https://blog.sendbird.com/android-chat-tutorial-building-a-messaging-ui
 *  Added onto and modified by Evan Honeysett
 */

public class MessageListActivity extends BaseActivity {

    private RecyclerView mMessageRecycler;
    private MessageListAdapter myMessageAdapter;

    @BindView(R.id.button_chatbox_send)
    Button sendButton;

    @BindView(R.id.edittext_chatbox)
    EditText messageContent;

    public List<MessageModel> messageModelList;

    private ChatModel chatModel;

    private FirebaseListAdapter<MessageModel> adapter;

    static int msg = 0;

    public static boolean isInMessaging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatModel = (ChatModel) getIntent().getSerializableExtra("chatModel");

        setContentView(R.layout.activity_message_list);
        ButterKnife.bind(this);

        messageModelList = new ArrayList<MessageModel>();

        isInMessaging = true;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Messaging");
        actionBar.setDisplayHomeAsUpEnabled(true);

        /*
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        /*



        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                    }
                });

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Toast.makeText(MessageListActivity.this, document.getId() + " => " + document.getData(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        */
        /*
        MessageModel m1 = new MessageModel("Hello!", "Evan");
        MessageModel m2 = new MessageModel("How are you?", "Jim");
        MessageModel m3 = new MessageModel("I am good! Thanks!", "Evan");
        MessageModel m4 = new MessageModel("This is such an amazing messaging system!", "Jim");
        MessageModel m5 = new MessageModel("Yeah, I know!", "Evan");
        MessageModel m6 = new MessageModel("I hope this is enough", "Jim");
        MessageModel m7 = new MessageModel("Should be!", "Evan");
*/
        //messageModelList.add(m1); messageModelList.add(m2); messageModelList.add(m3);
        //messageModelList.add(m4); messageModelList.add(m5); messageModelList.add(m6);
        //messageModelList.add(m7);
        MessageModel m1 = new MessageModel("Hello!", "Evan", (new Timestamp(System.currentTimeMillis())).toString());

        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        myMessageAdapter = new MessageListAdapter(this, messageModelList);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        mMessageRecycler.setLayoutManager(llm);
        mMessageRecycler.setAdapter(myMessageAdapter);
        myMessageAdapter.notifyDataSetChanged();



        //final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //final DatabaseReference myRef = database.getReference("chats/7");
        //myRef.child("1").setValue(m1);
        //System.out.println("added to database");
/*
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MessageModel value = dataSnapshot.getValue(MessageModel.class);
                messageModelList.add(value);
                myMessageAdapter.notifyDataSetChanged();
                mMessageRecycler.scrollToPosition(messageModelList.size() - 1);
                msg++;
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
        */

        /*
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                MessageModel value = dataSnapshot.getValue(MessageModel.class);
                messageModelList.add(value);
                myMessageAdapter.notifyDataSetChanged();
                mMessageRecycler.scrollToPosition(messageModelList.size() - 1);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        }); */


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("chats").document("" + chatModel.getChatID());
        final CollectionReference colRef = db.collection("chats").document("" + chatModel.getChatID()).collection("messages");

        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                for(DocumentSnapshot d : queryDocumentSnapshots) {
                    Map<String, Object> data = d.getData();
                    MessageModel m = new MessageModel((String) data.get("messageText"), (String)data.get("sender"), (String)data.get("createdAt"));
                    messageModelList.add(m);
                    myMessageAdapter.notifyDataSetChanged();
                    mMessageRecycler.scrollToPosition(messageModelList.size() - 1);
                }
            }
        });

        /*
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(MessageListActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Map<String, Object> data = snapshot.getData();
                    MessageModel m = new MessageModel((String) data.get("messageText"), (String)data.get("sender"), (String)data.get("createdAt"));
                    messageModelList.add(m);
                    myMessageAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MessageListActivity.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
        */

        /*
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("chats").document("" + chatModel.getChatID()).collection("messages")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> data = document.getData();
                                MessageModel m = new MessageModel((String) data.get("messageText"), (String)data.get("sender"), (String)data.get("createdAt"));
                                messageModelList.add(m);
                                myMessageAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(MessageListActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        */

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String message = messageContent.getText().toString();

                if(message.compareTo("") == 0)
                {
                    return;
                }
                else{
                    final MessageModel messageModel = new MessageModel(message, UserModel.myUserModel.getFirstName() + " " + UserModel.myUserModel.getLastName(),(new Timestamp(System.currentTimeMillis())).toString());
                    messageModelList.add(messageModel);
                    myMessageAdapter.notifyDataSetChanged();
                    mMessageRecycler.scrollToPosition(messageModelList.size() - 1);
                    messageContent.setText("");
                    LambencyAPIHelper.getInstance().sendMessage(UserModel.myUserModel.getOauthToken(),chatModel.getChatID(),messageModel).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if(response == null || !response.isSuccessful() || response.code() != 200 || response.body() == null){
                                Toast.makeText(view.getContext(),"I am sorry, the message failed to post. It failed in response", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Integer ret = response.body();
                                if (ret < 0) {
                                    Toast.makeText(view.getContext(), "Response from server said you cant send that message.", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(view.getContext(), "Message Sent with chat id " + chatModel.getChatID() + " and ret " + ret, Toast.LENGTH_LONG).show();
                                    //TODO
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("chats").document("" + chatModel.getChatID())
                                            .collection("messages")
                                            .document("" + ret)
                                            .set(messageModel);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Toast.makeText(view.getContext(),"I am sorry, the message failed to post. :(", Toast.LENGTH_LONG).show();
                        }
                    });
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
                        .setValue(new MessageModel("Hello World With Firebase", "Evan"));
                */
                /*

                MessageModel m1 = new MessageModel(message, UserModel.myUserModel.getFirstName());
                SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
                Date now = new Date();
                String strDate = sdf.format(now);
                m1.createdAt = strDate;
                //messageModelList.add(m1);
                //myMessageAdapter.notifyDataSetChanged();
                messageContent.setText("");
                */

                //DatabaseReference currRef = database.getReference("message/m" + msg);

                //currRef.setValue(m1);
            }
        });
    }

    /*
    public void populateMessage() {

        Query query = FirebaseDatabase.getInstance().getReference("/TestSpace").orderByKey();

        FirebaseListOptions<MessageModel> options = new FirebaseListOptions.Builder<MessageModel>()
                .setLayout(R.layout.activity_message_list)//Note: The guide doesn't mention this method, without it an exception is thrown that the layout has to be set.
                .setQuery(query, MessageModel.class)
                .build();

        /*
        adapter = new FirebaseListAdapter<MessageModel>(this, MessageModel.class,
                R.layout.activity_message_list, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, MessageModel model, int position) {
                // Get references to the views of message.xml
                messageModelList.add(model);
                myMessageAdapter.notifyDataSetChanged();
            }
        };
        */
        /*
        adapter = new FirebaseListAdapter<MessageModel>(options) {
            @Override
            protected void populateView(View v, MessageModel model, int position) {
                // Get references to the views of message.xml
                Toast.makeText(MessageListActivity.this, "Hello!", Toast.LENGTH_SHORT).show();
                messageModelList.add(model);
                myMessageAdapter.notifyDataSetChanged();
            }
        };


    }
    */


    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                isInMessaging = false;
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}