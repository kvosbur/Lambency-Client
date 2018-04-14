package com.lambency.lambency_client.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.lambency.lambency_client.Activities.MessageListActivity;
import com.lambency.lambency_client.Activities.StartChatActivity;
import com.lambency.lambency_client.Adapters.ChatRoomAdapter;
import com.lambency.lambency_client.Models.ChatModel;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPI;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Brian on 4/2/18.
 */

public class ChatListFragment extends Fragment {

    private final ArrayList<ChatModel> chatModels = new ArrayList<>();

    @BindView(R.id.chatFab)
    FloatingActionButton chatFab;

    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;


    protected RecyclerView mRecyclerView;
    protected ChatRoomAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ArrayList<ChatModel> mDataset = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        LambencyAPIHelper.getInstance().getAllChats(UserModel.myUserModel.getOauthToken()).enqueue(new Callback<ArrayList<ChatModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ChatModel>> call, Response<ArrayList<ChatModel>> response) {
                if(response == null || response.code() != 200 || !response.isSuccessful()){
                    Toast.makeText(getContext(), "Sorry We cant load chat", Toast.LENGTH_LONG).show();
                }
                else{
                   initDataset(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ChatModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Sorry We cant load chat because shit hit the fan fam", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_list, container, false);
        ButterKnife.bind(this, rootView);
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.chatListRecyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new ChatRoomAdapter(this.getContext(),mDataset);
        // Set CustomAdapter as the adapter for RecyclerView.


        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("adapter onclick");
                Toast.makeText(view.getContext(), mAdapter.getRooms().get((Integer)view.getTag()).getName(), Toast.LENGTH_LONG).show();
            }
        });

        if(mDataset == null){
            mRecyclerView.setVisibility(View.INVISIBLE);
        }

        return rootView;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset( ArrayList<ChatModel> c) {
        mDataset = c;
        mAdapter.updateEvents(mDataset);
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.chatFab)
    public void handleOrgFabClick(){
        Intent intent = new Intent(getActivity(), StartChatActivity.class);
        ArrayList<ChatModel> chats = mAdapter.getRooms();
        ArrayList<Integer> ids = new ArrayList<>();
        for(ChatModel c: chats){
            ids.add(c.getUserID());
        }
        intent.putExtra("chats", ids);
        startActivityForResult(intent,1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("on activity result");
        if (requestCode == 1) {
            ChatModel chatModel = (ChatModel) data.getExtras().getSerializable("chatModel");
            Intent intent = new Intent(getActivity(), MessageListActivity.class);
            intent.putExtra("chatModel",chatModel);
            startActivity(intent);

        }
    }
}