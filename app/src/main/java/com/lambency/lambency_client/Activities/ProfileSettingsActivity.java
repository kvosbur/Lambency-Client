package com.lambency.lambency_client.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPI;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        //the change password button
        final Button changePassword = findViewById(R.id.changePasswordBtn);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileSettingsActivity.this);
                alertDialog.setTitle("Password change");
                final EditText oldPass = new EditText(ProfileSettingsActivity.this);
                final EditText newPass = new EditText(ProfileSettingsActivity.this);
                final EditText confirmPass = new EditText(ProfileSettingsActivity.this);
                final Button confirmBtn = new Button(ProfileSettingsActivity.this);
                final Button cancelBtn = new Button(ProfileSettingsActivity.this);


                oldPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                newPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                confirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                oldPass.setHint("Old Password");
                newPass.setHint("New Password");
                confirmPass.setHint("Confirm Password");
                confirmBtn.setText("Confrim");
                cancelBtn.setText("Cancel");

                LinearLayout ll=new LinearLayout(ProfileSettingsActivity.this);
                ll.setOrientation(LinearLayout.VERTICAL);

                ll.addView(oldPass);
                ll.addView(newPass);
                ll.addView(confirmPass);


                LinearLayout lll =new LinearLayout(ProfileSettingsActivity.this);
                lll.setOrientation(LinearLayout.HORIZONTAL);
                lll.addView(cancelBtn);
                lll.addView(changePassword);

                ll.addView(lll);


                alertDialog.setView(ll);

                alertDialog.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                boolean check = true;


                                if (!newPass.getText().toString().equals(confirmPass.getText().toString())){
                                    check = false;
                                    Toast.makeText(ProfileSettingsActivity.this, "The two passwords don't match",
                                            Toast.LENGTH_LONG).show();
                                }
                                else if (newPass.getText().toString().matches("")){
                                    check = false;
                                    newPass.setError("Please enter a password");
                                    Toast.makeText(ProfileSettingsActivity.this, "Password entry field one empty",
                                            Toast.LENGTH_LONG).show();
                                }
                                else if (confirmPass.getText().toString().matches("")){
                                    check = false;
                                    confirmPass.setError("Please enter same password");
                                    Toast.makeText(ProfileSettingsActivity.this, "Password entry field two empty",
                                            Toast.LENGTH_LONG).show();
                                }
                                if (check == true) {
                                    LambencyAPIHelper.getInstance().changePassword(newPass.getText().toString(),
                                            confirmPass.getText().toString(), UserModel.myUserModel.getOauthToken(),oldPass.getText().toString())
                                            .enqueue(new Callback<Integer>() {
                                                @Override
                                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                    if (response.body() == null || response.code() != 200) {
                                                        Toast.makeText(getApplicationContext(), "Server error!", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                    int ret = response.body();

                                                    if (ret == 0){
                                                        Toast.makeText(getApplicationContext(), "Password change success", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else if (ret == 7){
                                                        Toast.makeText(getApplicationContext(), "You entered the old password wrong Pls try again", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else {
                                                        Toast.makeText(getApplicationContext(), "General Server error!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Integer> call, Throwable t) {
                                                    System.out.println("Failed in change password");
                                                }
                                            });
                                    dialog.cancel();
                                }
                            }
                        });


                alertDialog.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = alertDialog.create();
                alert11.show();
            }
        });
    }
}
