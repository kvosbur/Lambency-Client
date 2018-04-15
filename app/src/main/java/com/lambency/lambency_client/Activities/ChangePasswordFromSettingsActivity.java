package com.lambency.lambency_client.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;
import com.ybs.passwordstrengthmeter.PasswordStrength;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFromSettingsActivity extends AppCompatActivity implements TextWatcher {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_from_settings);

        final EditText oldPasswordEntry = findViewById(R.id.oldPassEntry);

        final EditText newPasswordEntry1 = findViewById(R.id.newPass1);

        newPasswordEntry1.addTextChangedListener(this);

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
                if (newPasswordEntry1.getText().toString().matches("") || newPasswordEntry1.getText().toString().length() < 8) {
                    check = false;
                    newPasswordEntry1.setError("Password must be atleast 8 characters");
                    Toast.makeText(ChangePasswordFromSettingsActivity.this, "Password must be atleast 8 characters",
                            Toast.LENGTH_LONG).show();
                }
                if (newPasswordEntry2.getText().toString().matches("") || newPasswordEntry1.getText().toString().length() < 8) {
                    check = false;
                    newPasswordEntry2.setError("Password must be atleast 8 characters");
                    Toast.makeText(ChangePasswordFromSettingsActivity.this, "Password must be atleast 8 characters",
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updatePasswordStrengthView(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    private void updatePasswordStrengthView(String password) {

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView strengthView = (TextView) findViewById(R.id.password_strength);
        if (TextView.VISIBLE != strengthView.getVisibility())
            return;

        if (password.isEmpty()) {
            strengthView.setText("");
            progressBar.setProgress(0);
            return;
        }

        PasswordStrength str = PasswordStrength.calculateStrength(password);
        strengthView.setText(str.getText(this));
        strengthView.setTextColor(str.getColor());

        progressBar.getProgressDrawable().setColorFilter(str.getColor(), android.graphics.PorterDuff.Mode.SRC_IN);
        if (str.getText(this).equals("Weak")) {
            progressBar.setProgress(25);
        } else if (str.getText(this).equals("Medium")) {
            progressBar.setProgress(50);
        } else if (str.getText(this).equals("Strong")) {
            progressBar.setProgress(75);
        } else {
            progressBar.setProgress(100);
        }
    }

}
