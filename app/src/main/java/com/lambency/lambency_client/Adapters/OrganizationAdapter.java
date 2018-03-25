package com.lambency.lambency_client.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lambency.lambency_client.Activities.EventDetailsActivity;
import com.lambency.lambency_client.Activities.OrganizationDetailsActivity;
import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;
import com.lambency.lambency_client.Utils.ImageHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lshan on 2/20/2018.
 */

public class OrganizationAdapter extends RecyclerView.Adapter<OrganizationAdapter.AreaViewHolder>{

    List<OrganizationModel> orgs;
    private Context context;

    public OrganizationAdapter(Context context, List<OrganizationModel> orgs){
        this.context = context;
        this.orgs = orgs;
    }

    @Override
    public OrganizationAdapter.AreaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_organization, parent, false);
        return new OrganizationAdapter.AreaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final OrganizationAdapter.AreaViewHolder holder, int position) {
        OrganizationModel orgModel = orgs.get(position);

        if(orgModel.name != null) {
            holder.titleView.setText(orgModel.name);
        }

        if(orgModel.description != null) {
            holder.descriptionView.setText(orgModel.description);
        }

        if(orgModel.email != null) {
            holder.emailView.setText(orgModel.email);
        }

        if(orgModel.getImage() != null){
            //holder.profileImage.setImageBitmap(ImageHelper.stringToBitmap(orgModel.getImage()));

            ImageHelper.loadWithGlide(context,
                    ImageHelper.saveImage(context, orgModel.getImage(), "orgImage" + orgModel.getOrgID()),
                    holder.profileImage);
        }

        LambencyAPIHelper.getInstance().getRequestsToJoin(UserModel.myUserModel.getOauthToken(), orgModel.getOrgID()).enqueue(new Callback<ArrayList<UserModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserModel>> call, Response<ArrayList<UserModel>> response) {
                if(response.body() == null){
                    Log.e("Retrofit", "Org get user request is null");
                }else{
                    int numRequests = response.body().size();
                    if(numRequests > 0){
                        holder.notificationNumText.setText(numRequests + "");
                        holder.notificationNumText.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserModel>> call, Throwable t) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return orgs.size();
    }

    public class AreaViewHolder extends RecyclerView.ViewHolder {

        //Get references to layout and define onClick methods here

        @BindView(R.id.title)
        TextView titleView;

        @BindView(R.id.description)
        TextView descriptionView;

        @BindView(R.id.email)
        TextView emailView;

        @BindView(R.id.profileImage)
        ImageView profileImage;

        @BindView(R.id.card_organization)
        CardView cardView;

        @BindView(R.id.notificationNumText)
        TextView notificationNumText;

        public AreaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.card_organization)
        public void onClickCard() {
            Intent intent = new Intent(context, OrganizationDetailsActivity.class);
            Bundle bundle = new Bundle();

            Integer taggedPosition = this.getLayoutPosition();

            System.out.println("the position is : " + taggedPosition);
            OrganizationModel org = orgs.get(taggedPosition);
            bundle.putInt("org_id", orgs.get(taggedPosition).getOrgID());
            intent.putExtras(bundle);
            context.startActivity(intent);
        }


    }

    public void updateOrgs(ArrayList<OrganizationModel> orgsList){
        orgs = orgsList;
        notifyDataSetChanged();
    }

}
