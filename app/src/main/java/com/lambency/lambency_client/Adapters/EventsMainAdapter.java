package com.lambency.lambency_client.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lshan on 2/17/2018.
 */

public class EventsMainAdapter extends RecyclerView.Adapter<EventsMainAdapter.AreaViewHolder>{

    @Override
    public AreaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(AreaViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class AreaViewHolder extends RecyclerView.ViewHolder {

        public AreaViewHolder(View itemView) {
            super(itemView);
        }
    }

}
