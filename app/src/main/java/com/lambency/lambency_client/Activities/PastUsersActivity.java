package com.lambency.lambency_client.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lambency.lambency_client.Adapters.UserListAdapter;
import com.lambency.lambency_client.Models.EventAttendanceModel;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPI;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastUsersActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Context context;
    private int event_id;
    private UserListAdapter userListAdapter;

    List<UserModel> users;

    @BindView(R.id.usersRecyclerView)
    RecyclerView usersRecyclerView;

    @BindView(R.id.noUsersText)
    TextView noUsersText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_users);

        ButterKnife.bind(this);

        context = this;

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        event_id = getIntent().getIntExtra("event_id", -1);
        if(event_id == -1){
            Log.e("PastUsersActivity", "Error passing event id");
        }else {
            getUsers();
        }

    }


    private void getUsers(){
        LambencyAPIHelper.getInstance().getPastEventAttendence(UserModel.myUserModel.getOauthToken(), event_id).enqueue(new Callback<ArrayList<EventAttendanceModel>>() {
            @Override
            public void onResponse(Call<ArrayList<EventAttendanceModel>> call, Response<ArrayList<EventAttendanceModel>> response) {
                if(response.body() == null){
                    Log.e("Retrofit", "Get past event attendance returned null");
                    return;
                }

                ArrayList<EventAttendanceModel> attendanceModels = response.body();
                if(attendanceModels.size() == 0){
                    System.out.println("No users attended this event.");
                    noUsersText.setVisibility(View.VISIBLE);
                    usersRecyclerView.setVisibility(View.GONE);
                }

                ArrayList<UserModel> users = new ArrayList<>();
                for(EventAttendanceModel model : attendanceModels){
                    users.add(model.getUserModel());
                    long milliseconds = model.getEndTime().getTime() - model.getStartTime().getTime();
                    double hours = milliseconds/3600000d;
                    hours = Math.round(hours * 10)/10d;
                    model.getUserModel().setPastEventHours(hours);
                    users.add(model.getUserModel());
                }

                setUsers(users);

                startAdapter(users);

            }

            @Override
            public void onFailure(Call<ArrayList<EventAttendanceModel>> call, Throwable t) {
                Log.e("Retrofit", "Get past event attendance error out");
            }
        });
    }

    private void startAdapter(ArrayList<UserModel> users){
        userListAdapter = new UserListAdapter(context, users);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        usersRecyclerView.setAdapter(userListAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_search_users, menu);

        final MenuItem searchAction = menu.findItem( R.id.search );
        SearchView searchView = (SearchView) searchAction.getActionView();
        searchView.setQueryHint("Search users...");
        searchView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<UserModel> filteredUsers = filter(users, newText);
        userListAdapter.replaceAll(new ArrayList<UserModel>(filteredUsers));

        return true;
    }

    private static List<UserModel> filter(List<UserModel> users, String query){
        final String lowercaseQuery = query.toLowerCase();

        final  List<UserModel> filteredUserList = new ArrayList<>();
        for(UserModel user: users){
            final String firstName = user.getFirstName().toLowerCase();
            final String lastName = user.getLastName().toLowerCase();
            final String fullName = firstName + " " + lastName;
            if(firstName.contains(lowercaseQuery) || lastName.contains(lowercaseQuery) || fullName.contains(lowercaseQuery)){
                filteredUserList.add(user);
            }
        }

        return filteredUserList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return true;
        }
    }

    public void setUsers(List<UserModel> users) {
        this.users = users;
    }
}
