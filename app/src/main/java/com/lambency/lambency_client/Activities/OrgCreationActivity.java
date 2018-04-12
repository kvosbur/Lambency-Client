package com.lambency.lambency_client.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.AsyncOrgTask;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;
import com.lambency.lambency_client.Utils.ImageHelper;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrgCreationActivity extends AppCompatActivity {

    private Context context;
    private String imagePath = "";
    private OrganizationModel orgModel;
    private boolean editing;

    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;

    @BindView(R.id.progressLayout)
    RelativeLayout progressLayout;

    private static final int CAMERA = 0;

    @BindView(R.id.profileImage)
    ImageView profileImage;

    @BindView(R.id.nameEdit)
    TextInputEditText nameEdit;

    @BindView(R.id.emailEdit)
    TextInputEditText emailEdit;

    @BindView(R.id.descriptionEdit)
    TextInputEditText descriptionEdit;

    @BindView(R.id.addressEdit)
    TextInputEditText addressEdit;

    @BindView(R.id.cityEdit)
    TextInputEditText cityEdit;

    @BindView(R.id.stateAutocomplete)
    AutoCompleteTextView stateAutocomplete;

    @BindView(R.id.zipEdit)
    TextInputEditText zipEdit;

    @BindView(R.id.loadingBar)
    ProgressBar progressBar;

    @BindView(R.id.deleteButton)
    Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_creation);
        this.context = this;

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editing = false;
        int org_id = -1;
        if(getIntent().getExtras() != null){
            org_id = getIntent().getExtras().getInt("org_id", -1);
        }
        if(org_id != -1){
            editing = true;
            getOrgModel(org_id);
        }

        if(editing){
            deleteButton.setVisibility(View.VISIBLE);
            getSupportActionBar().setTitle("Edit Organization");
        }else{
            progressBar.setVisibility(View.GONE);
            progressLayout.setVisibility(View.GONE);
            getSupportActionBar().setTitle("New Organization");
        }


        //Set up the state selector
        AutoCompleteTextView stateAutocomplete = (AutoCompleteTextView) findViewById(R.id.stateAutocomplete);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.states, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateAutocomplete.setAdapter(adapter);

    }


    private void getOrgModel(final int orgId){

        mainLayout.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);

        LambencyAPIHelper.getInstance().getOrgSearchByID(orgId + "").enqueue(new Callback<OrganizationModel>() {
            @Override
            public void onResponse(Call<OrganizationModel> call, Response<OrganizationModel> response) {
                if(response.body() != null){

                    orgModel = response.body();
                    setOrgModel(orgModel);

                    nameEdit.setText(orgModel.getName());
                    emailEdit.setText(orgModel.getEmail());
                    descriptionEdit.setText(orgModel.getDescription());
                    addressEdit.setText(orgModel.getLocation());
                    mainLayout.setVisibility(View.VISIBLE);
                    progressLayout.setVisibility(View.GONE);
                    if(orgModel.getImagePath() != null && !orgModel.getImagePath().equals("")){
                        ImageHelper.loadWithGlide(context, orgModel.getImagePath(), profileImage);
                    }
                }
            }

            @Override
            public void onFailure(Call<OrganizationModel> call, Throwable t) {
                Log.e("Retrofit", "Unable to get org to edit");
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case(R.id.action_done):

                String name = nameEdit.getText().toString();
                String email = emailEdit.getText().toString();
                String description = descriptionEdit.getText().toString();

                String address = addressEdit.getText().toString();
                String city = cityEdit.getText().toString();
                String state = stateAutocomplete.getText().toString();
                String zip = zipEdit.getText().toString();
                String location = address + " " + city + " " + state + " " + zip;

                if (name.matches("") || email.matches("") || description.matches("") || address.matches("") || city.matches("") || state.matches("") || zip.matches("") || zip.matches("")) {
                    Toast.makeText(this, "You did not enter all information", Toast.LENGTH_SHORT).show();
                    return false;
                }

                mainLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                if(editing){
                    //Edit org
                    orgModel.setName(name);
                    orgModel.setEmail(email);
                    orgModel.setDescription(description);
                    orgModel.setLocation(location);
                    //editOrg(orgModel);
                    new AsyncOrgTask(orgModel, context, AsyncOrgTask.EDIT_MODE).execute();
                }else{
                    //Create new org
                    orgModel = new OrganizationModel(UserModel.myUserModel, name, location, 0, description, email, UserModel.myUserModel, ImageHelper.getByteArrayFromPath(context, imagePath));
                    //createNewOrg(orgModel);
                    new AsyncOrgTask(orgModel, context, AsyncOrgTask.CREATE_MODE).execute();
                }

                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @OnClick(R.id.deleteButton)
    public void deleteOrg(){

        LambencyAPIHelper.getInstance().getDeleteOrganization(UserModel.myUserModel.getOauthToken(), orgModel.getOrgID() + "").enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.body() == null || response.code() != 200) {
                    System.out.println("failed to update org or invalid permissions");
                }
                //when response is back
                Integer ret = response.body();
                if(ret == 0){
                    System.out.println("successfully deleted org");
                }
                else if(ret == -1){
                    System.out.println("failed to delete org: bad params or error occurred");
                }
                else if(ret == -2){
                    System.out.println("user is not an organizer");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable throwable) {
                //when failure
                System.out.println("FAILED CALL");
            }
        });

        UserModel.myUserModel.getMyOrgs().remove(Integer.valueOf(orgModel.getOrgID()));

        Intent intent = new Intent(context, BottomBarActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @OnClick(R.id.profileImage)
    public void setProfileImage(){

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(OrgCreationActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA);
        }else{
            EasyImage.openChooserWithGallery(this, "Select Profile Image", 0);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Log.e("Easy Image", e.getMessage());
                //Some error handling
            }

            @Override
            public void onImagesPicked(List<File> imagesFiles, EasyImage.ImageSource source, int type) {
                //Handle the images

                Picasso.Builder builder = new Picasso.Builder(context);
                builder.listener(new Picasso.Listener()
                {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                    {
                        exception.printStackTrace();
                    }
                });

                File imageFile = imagesFiles.get(0);
                imagePath = imageFile.getPath();
                ImageHelper.displayEasyImageResult(context, imageFile, profileImage);

                if(editing){
                    orgModel.setImageFile(ImageHelper.getByteArrayFromPath(context, imagePath));
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    EasyImage.openChooserWithGallery(this, "Select Profile Image", 0);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public void setOrgModel(OrganizationModel orgModel){
        this.orgModel = orgModel;
    }


}
