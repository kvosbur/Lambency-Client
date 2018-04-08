package com.lambency.lambency_client.Adapters;

import android.content.Context;
import android.media.Image;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.R;
import com.lambency.lambency_client.Utils.ImageHelper;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lshan on 3/22/2018.
 */

public class OrgSpinnerAdapter extends BaseAdapter{

    Context context;
    List<OrganizationModel> orgs;

    public OrgSpinnerAdapter(Context context, List<OrganizationModel> orgs){
        this.context = context;
        this.orgs = orgs;
    }


    @Override
    public int getCount() {
        return orgs.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.spinner_item_org, null);

        OrganizationModel orgModel = orgs.get(i);

        TextView orgTitle = view.findViewById(R.id.orgTitle);
        orgTitle.setText(orgModel.getName());


        CircleImageView orgImage = view.findViewById(R.id.orgImage);
        ImageHelper.loadWithGlide(context,
                orgs.get(i).getImagePath(),
                orgImage);


        return view;
    }

    public List<OrganizationModel> getOrgs() {
        return orgs;
    }
}
