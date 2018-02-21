package com.lambency.lambency_client.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.lambency.lambency_client.Models.OrganizationModel;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;
import com.squareup.picasso.Callback;
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
                if(imagePath == ""){
                    //Use default profile image
                    bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_default_avatar);
                }else {
                    bm = BitmapFactory.decodeFile(imagePath);
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                byte[] b = baos.toByteArray();
                String encodedProfile = Base64.encodeToString(b, Base64.DEFAULT);

                String name = nameEdit.getText().toString();
                String email = emailEdit.getText().toString();
                String description = descriptionEdit.getText().toString();

                String address = addressEdit.getText().toString();
                String city = cityEdit.getText().toString();
                String state = stateAutocomplete.getText().toString();
                String zip = zipEdit.getText().toString();
                String location = address + " " + city + " " + state + " " + zip;

                orgModel = new OrganizationModel(UserModel.myUserModel, name, location, 0, description, email, UserModel.myUserModel, encodedProfile);
                int a = 6;
                LambencyAPIHelper.getInstance().postCreateOrganization(orgModel).enqueue(new retrofit2.Callback<OrganizationModel>() {
                    @Override
                    public void onResponse(Call<OrganizationModel> call, Response<OrganizationModel> response) {
                        if (response.body() == null || response.code() != 200) {
                            System.out.println("ERROR!!!!!");
                        }
                        //when response is back
                        OrganizationModel org = response.body();
                        System.out.println(org.name);
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
        EasyImage.openChooserWithGallery(this, "Select Profile Image", 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
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
}
