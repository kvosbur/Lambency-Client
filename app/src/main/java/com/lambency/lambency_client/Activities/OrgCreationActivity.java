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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;
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
import retrofit2.Response;

public class OrgCreationActivity extends AppCompatActivity {

    private Context context;
    private String imagePath = "";
    private OrganizationModel orgModel;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_creation);
        this.context = this;

        ButterKnife.bind(this);

        //Set up the state selector
        AutoCompleteTextView stateAutocomplete = (AutoCompleteTextView) findViewById(R.id.stateAutocomplete);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.states, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateAutocomplete.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case(R.id.action_done):

                //Convert the image to a base64 string
                Bitmap bm;
                byte[] imageFile;
                if(imagePath.equals("")){
                    //Use default profile image
                    bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_default_avatar);
                    imageFile = null;
                }else {
                    bm = BitmapFactory.decodeFile(imagePath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                    imageFile = baos.toByteArray();
                }

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

                orgModel = new OrganizationModel(UserModel.myUserModel, name, location, 0, description, email, UserModel.myUserModel, imageFile);

                progressBar.setVisibility(View.VISIBLE);

                LambencyAPIHelper.getInstance().postCreateOrganization(orgModel).enqueue(new retrofit2.Callback<OrganizationModel>() {
                    @Override
                    public void onResponse(Call<OrganizationModel> call, Response<OrganizationModel> response) {
                        if (response.body() == null || response.code() != 200) {
                            System.out.println("ERROR!!!!!");
                            Toast.makeText(getApplicationContext(), "Error With Server", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //when response is back
                        OrganizationModel org = response.body();
                        int org_id = org.getOrgID();
                        UserModel.myUserModel.organizeGroup(org_id);

                        progressBar.setVisibility(View.GONE);

                        if(org.name == null)
                        {
                            Toast.makeText(getApplicationContext(), "That name is already taken", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        OrganizationModel.myOrgModel = orgModel;

                        //Go back to main page now
                        Intent myIntent = new Intent(context, BottomBarActivity.class);
                        startActivity(myIntent);
                        Toast.makeText(getApplicationContext(), "Organization made", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<OrganizationModel> call, Throwable throwable) {
                        //when failure
                        System.out.println("FAILED CALL");
                    }
                });
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return super.onCreateOptionsMenu(menu);
    }



    //https://github.com/jkwiecien/EasyImage

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

                imagePath = imagesFiles.get(0).getPath();

                builder.build()
                        .load(new File(imagesFiles.get(0).getPath()))
                        .error(R.drawable.ic_books)
                        .into(profileImage);

                /*
                Bitmap bitmap = BitmapFactory.decodeFile(imagesFiles.get(0).getPath(), null);
                profileImage.setImageBitmap(bitmap);
                */
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


}
