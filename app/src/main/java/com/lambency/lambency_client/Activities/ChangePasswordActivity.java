package com.lambency.lambency_client.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lambency.lambency_client.Networking.LambencyAPI;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity {
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
                if (passwordInput1.getText().toString().matches("")){
                    check = false;
                    passwordInput1.setError("Please enter a password");
                    Toast.makeText(ChangePasswordActivity.this, "Password entry field one empty",
                            Toast.LENGTH_LONG).show();
                }
                if (passwordInput2.getText().toString().matches("")){
                    check = false;
                    passwordInput2.setError("Please enter same password");
                    Toast.makeText(ChangePasswordActivity.this, "Password entry field two empty",
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

}
