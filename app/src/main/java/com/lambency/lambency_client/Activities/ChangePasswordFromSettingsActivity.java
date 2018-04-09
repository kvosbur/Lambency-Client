package com.lambency.lambency_client.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFromSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_from_settings);

        final EditText oldPasswordEntry = findViewById(R.id.oldPassEntry);

        final EditText newPasswordEntry1 = findViewById(R.id.newPass1);

        final EditText newPasswordEntry2 = findViewById(R.id.newPass2);

        Button confirmPass = findViewById(R.id.confirmChangeButton);

        confirmPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = true;
                if (!newPasswordEntry1.getText().toString().equals(newPasswordEntry2.getText().toString())) {
                    check = false;
                    Toast.makeText(ChangePasswordFromSettingsActivity.this, "The two passwords don't match",
                            Toast.LENGTH_LONG).show();
                }
                if (newPasswordEntry1.getText().toString().matches("")) {
                    check = false;
                    newPasswordEntry1.setError("Please enter a password");
                    Toast.makeText(ChangePasswordFromSettingsActivity.this, "Password entry field one empty",
                            Toast.LENGTH_LONG).show();
                }
                if (newPasswordEntry2.getText().toString().matches("")) {
                    check = false;
                    newPasswordEntry2.setError("Please enter same password");
                    Toast.makeText(ChangePasswordFromSettingsActivity.this, "Password entry field two empty",
                            Toast.LENGTH_LONG).show();
                }

                if (check) {
                    LambencyAPIHelper.getInstance().changePassword(newPasswordEntry1.getText().toString(),
                            newPasswordEntry2.getText().toString(), UserModel.myUserModel.getOauthToken(),oldPasswordEntry.getText().toString())
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
                                        Intent intent = new Intent(ChangePasswordFromSettingsActivity.this,ProfileSettingsActivity.class);
                                        startActivity(intent);
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
                }
            }
        });

    }
}
