package com.lambency.lambency_client.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.SortedList;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.lambency.lambency_client.Activities.OrgUsersActivity;
import com.lambency.lambency_client.Fragments.UserListFragment;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Evan on 2/12/2018.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private Context context;
    private String org_id;

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

    public UserListAdapter(Context context, List<UserModel> users)
    {
        this.context = context;
        add(users);
    }

    public UserListAdapter(Context context, List<UserModel> users, String org_id){
        this.context = context;
        this.org_id = org_id;
        add(users);
    }



    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_user, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserListAdapter.ViewHolder holder, int position) {

        final UserModel userModel = users.get(position);

        String name = userModel.getFirstName() + " " + userModel.getLastName();
        holder.nameView.setText(name);

        holder.emailView.setText(userModel.getEmail());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPermissions(userModel);
            }
        });

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

        //TODO Add retrofit here for getting online status of other users, own call
        //TODO is done in BottomBarActivity (by default user is offline)
        /*
        if(userIsOnline) {
            holder.onlineCircle.setVisibility(View.VISIBLE);
            holder.offlineCircle.setVisibility(View.GONE);
        } else {
            holder.offlineCircle.setVisibility(View.VISIBLE);
            holder.onlineCirlce.setVisibility(View.GONE);
        }

         */

        if(userModel.getPastEventHours() > -1){
            String hoursStr = userModel.getPastEventHours() + " hrs";
            holder.hoursTextView.setText(hoursStr);
            holder.hoursTextView.setVisibility(View.VISIBLE);
        }

        holder.emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail(userModel);
            }
        });

        if(userModel.isEditable()){
            holder.editButton.setVisibility(View.VISIBLE);
        }


    }


    private void sendEmail(final UserModel userModel){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{userModel.getEmail()});
        i.putExtra(Intent.EXTRA_SUBJECT, "Lambency Volunteering");
        //i.putExtra(Intent.EXTRA_TEXT   , "body of email");
        try {
            context.startActivity(Intent.createChooser(i, "Email " + userModel.getFirstName() + "..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "No email clients installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void editPermissions(final UserModel userModel){
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View dialogView = layoutInflater.inflate(R.layout.dialog_permissions, null);

        alertDialog.setTitle("Change Permissions");
        alertDialog.setMessage("Change permissions for " + userModel.getFirstName() + " " + userModel.getLastName() + ":");
        alertDialog.setView(dialogView);

        dialogView.getRootView().findViewById(R.id.memberButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPermissionsRetrofit(userModel, 1 + "");


                Toast.makeText(context, userModel.getFirstName() + " " + userModel.getLastName() + " is now a member", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();

            }
        });

        dialogView.getRootView().findViewById(R.id.organizerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPermissionsRetrofit(userModel, 2 + "");

                Toast.makeText(context, userModel.getFirstName() + " " + userModel.getLastName() + " is now an organizer", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        dialogView.getRootView().findViewById(R.id.removeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPermissionsRetrofit(userModel, 0 + "");

                Toast.makeText(context, userModel.getFirstName() + " " + userModel.getLastName() + " has been removed from this organization", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();

            }
        });

        alertDialog.show();
    }


    private void callPermissionsRetrofit(UserModel userModel, String type){
        //type  0 is remove from group
        //      1 is set to member
        //      2 is set to organizer

        LambencyAPIHelper.getInstance().getChangeUserPermissions(UserModel.myUserModel.getOauthToken(), org_id, userModel.getUserId() + "", type).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.body() == null || response.code() != 200) {
                    System.out.println("ERROR!!!!!");
                    return;
                }
                //when response is back
                Integer ret = response.body();
                if(ret == 0){
                    System.out.println("Success");
                }
                else if(ret == -1){
                    System.out.println("an error has occurred");
                }
                else if(ret == -2){
                    System.out.println("insufficient permissions");
                }
                else if(ret == -3){
                    System.out.println("invalid arguments");
                }

                OrgUsersActivity.getCurInstance().getUsers(Integer.parseInt(org_id));
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable throwable) {
                //when failure
                System.out.println("FAILED CALL");
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

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

        @BindView(R.id.hoursText)
        TextView hoursTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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

}

