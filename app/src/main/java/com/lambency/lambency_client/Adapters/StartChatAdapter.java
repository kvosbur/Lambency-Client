package com.lambency.lambency_client.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.SortedList;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.lambency.lambency_client.Activities.OrgUsersActivity;
import com.lambency.lambency_client.Activities.StartChatActivity;
import com.lambency.lambency_client.Models.ChatModel;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPI;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Evan on 2/12/2018.
 */

public class StartChatAdapter extends RecyclerView.Adapter<StartChatAdapter.AreaViewHolder> {

    private Context context;

    private static ArrayList<ChatModel> emptyChats;

    private final SortedList<UserModel> users = new SortedList<UserModel>(UserModel.class, new SortedList.Callback<UserModel>() {
        @Override
        public int compare(UserModel o1, UserModel o2) {
            return o1.getFirstName().compareTo(o2.getFirstName());
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(UserModel oldItem, UserModel newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(UserModel item1, UserModel item2) {
            return item1.getUserId() == item2.getUserId();
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }
    });

    public StartChatAdapter(Context context, List<UserModel> users, ArrayList<ChatModel> emptyChats)
    {
        this.context = context;
        StartChatAdapter.emptyChats = emptyChats;
        add(users);
    }



    @Override
    public StartChatAdapter.AreaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_user, parent, false);
        return new AreaViewHolder(v, users);
    }

    @Override
    public void onBindViewHolder(StartChatAdapter.AreaViewHolder holder, int position) {

        final UserModel userModel = users.get(position);

        String name = userModel.getFirstName() + " " + userModel.getLastName();
        holder.nameView.setText(name);


        if(userModel.getOrgStatus() == UserModel.MEMBER){
            holder.permissionButton.setVisibility(View.VISIBLE);
            holder.permissionButton.setText("MEMBER");
            holder.permissionButton.setTextColor(context.getResources().getColor(R.color.androidGreen));
        }

        if(userModel.getOrgStatus() == UserModel.ORGANIZER){
            holder.permissionButton.setVisibility(View.VISIBLE);
            holder.permissionButton.setText("ORGANIZER");
            holder.permissionButton.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }

        holder.offlineCircle.setVisibility(View.GONE);
        holder.onlineCircle.setVisibility(View.GONE);

        holder.emailView.setVisibility(View.GONE);
        holder.emailLayout.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class AreaViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView nameView;

        @BindView(R.id.emailLayout)
        LinearLayout emailLayout;

        @BindView(R.id.email)
        TextView emailView;

        @BindView(R.id.permissionButton)
        Button permissionButton;

        @BindView(R.id.editButton)
        ImageButton editButton;

        @BindView(R.id.onlineStatusOffline)
        LinearLayout offlineCircle;

        @BindView(R.id.onlineStatusOnline)
        LinearLayout onlineCircle;

        @BindView(R.id.user_card)
        CardView user_card;

        SortedList<UserModel> users;


        public AreaViewHolder(View itemView, SortedList<UserModel> users) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.users = users;
        }


        @OnClick(R.id.user_card)
        public void CreateChat(){
            System.out.println("CLICKED " + nameView.getText());
            //get current user token
            String token = FirebaseInstanceId.getInstance().getToken();

            //((Activity)user_card.getContext()).finish();




            //FirebaseDatabase.getInstance().getReference().child("messages").child("1").setValue("EMPTY");
            ChatModel chatModel = StartChatActivity.containsEmptyChat(emptyChats, users.get(getAdapterPosition()).getUserId());
            if(chatModel == null){
                LambencyAPIHelper.getInstance().createChat(UserModel.myUserModel.getOauthToken(),users.get( getAdapterPosition()).getUserId(),false).enqueue(new Callback<ChatModel>() {
                    @Override
                    public void onResponse(Call<ChatModel> call, Response<ChatModel> response) {
                        if (response == null || response.code() != 200 || response.body() == null) {
                            Toast.makeText(user_card.getContext(), "Sorry we cant create it", Toast.LENGTH_LONG).show();
                            System.out.println("It FAILED3");
                        }else{
                            ChatModel chatModel = response.body();
                            Intent intent = new Intent();
                            intent.putExtra("chatModel", chatModel);
                            StartChatActivity c = (StartChatActivity)user_card.getContext();
                            c.setResult(RESULT_OK, intent);
                            c.finish();
                            System.out.println("It FAILED4");
                        }
                        System.out.println("It FAILED5");

                    }

                    @Override
                    public void onFailure(Call<ChatModel> call, Throwable t) {
                        System.out.println("It FAILED2");
                        Toast.makeText(user_card.getContext(), "Sorry it failed ;/", Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                Intent intent = new Intent();
                intent.putExtra("chatModel", chatModel);
                StartChatActivity c = (StartChatActivity)user_card.getContext();
                c.setResult(RESULT_OK, intent);
                c.finish();
            }
        }



    }

    public void updateUserList(ArrayList<UserModel> users){
        add(users);
        notifyDataSetChanged();
    }


    public void add(UserModel user){
        this.users.add(user);
    }

    public void add(List<UserModel> users){
        this.users.addAll(users);
    }

    public void remove(UserModel user){
        this.users.remove(user);
    }

    public void remove(List<UserModel> users){
        this.users.beginBatchedUpdates();
        for(UserModel user: users){
            this.users.remove(user);
        }
        this.users.endBatchedUpdates();
    }


    public void replaceAll(List<UserModel> filteredUsers){

        this.users.beginBatchedUpdates();

        if(filteredUsers == null || filteredUsers.size() == 0){
            for (int i = this.users.size() - 1; i >= 0 ; i--) {
                remove(this.users.get(i));
            }

            this.users.endBatchedUpdates();

            return;
        }

        for (int i = this.users.size() - 1; i >= 0; i--) {
            UserModel user = this.users.get(i);
            if(!filteredUsers.contains(user)) {
                this.users.remove(user);
            }
        }

        this.users.addAll(filteredUsers);
        this.users.endBatchedUpdates();
    }

    public SortedList<UserModel> getUsers() {
        return users;
    }
}

