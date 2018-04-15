package com.lambency.lambency_client.Activities;

import android.content.Intent;
import android.net.Uri;
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

import com.lambency.lambency_client.Networking.LambencyAPI;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;
import com.ybs.passwordstrengthmeter.PasswordStrength;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity {
public class ChangePasswordActivity extends AppCompatActivity implements TextWatcher {
    String userId = "";
    String verificationCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Intent intent = getIntent();
        Uri data = intent.getData();


        //Bundle for deeplink
        Bundle bundle = getIntent().getExtras();
        if (bundle != null || data != null) {
            //TODO error check
            if (data != null) {
                userId = data.getQueryParameter("uid");
                verificationCode = data.getQueryParameter("code");
            }
        }

        final EditText passwordInput1 = findViewById(R.id.firstTime);
        passwordInput1.addTextChangedListener(this);
        final EditText passwordInput2 = findViewById(R.id.secondTime);
        Button confirmChanges = findViewById(R.id.confirmChangeBtn);

        confirmChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = true;
                if (!passwordInput1.getText().toString().equals(passwordInput2.getText().toString())){
                    check = false;
                    Toast.makeText(ChangePasswordActivity.this, "The two passwords don't match",
                            Toast.LENGTH_LONG).show();
                }
                if (passwordInput1.getText().toString().matches("") || passwordInput1.getText().toString().length() < 8){
                    check = false;
                    passwordInput1.setError("Password must be atleast 8 characters");
                    Toast.makeText(ChangePasswordActivity.this, "Password must be atleast 8 characters",
                            Toast.LENGTH_LONG).show();
                }
                if (passwordInput2.getText().toString().matches("") || passwordInput2.getText().toString().length() < 8){
                    check = false;
                    passwordInput2.setError("Password must be atleast 8 characters");
                    Toast.makeText(ChangePasswordActivity.this, "Password must be atleast 8 characters",
                            Toast.LENGTH_LONG).show();
                }

                if (check){
                    LambencyAPIHelper.getInstance().endPasswordRecovery(passwordInput1.getText().toString(),
                            passwordInput2.getText().toString(),verificationCode,Integer.parseInt(userId)).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if (response.body() == null || response.code() != 200) {
                                Toast.makeText(getApplicationContext(), "Server error!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            int ret = response.body();

                            if (ret == 0){
                                Intent changeActivity = new Intent(ChangePasswordActivity.this,LoginActivity.class);
                                startActivity(changeActivity);
                                System.out.println("Password was changed SUCCESS!!!");
                                Toast.makeText(getApplicationContext(), "Password change successfull", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                System.out.println("General Server Error");
                                Toast.makeText(getApplicationContext(), "General Server error!", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            System.out.println("error occurred while changing password");
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
