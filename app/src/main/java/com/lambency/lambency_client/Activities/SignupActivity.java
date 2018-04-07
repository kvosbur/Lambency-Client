package com.lambency.lambency_client.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lambency.lambency_client.Models.EventModel;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    @BindView(R.id.input_name)
    EditText _nameText;

    @BindView(R.id.input_email)
    EditText _emailText;

    @BindView(R.id.input_password)
    EditText _passwordText;

    @BindView(R.id.btn_signup)
    Button _signupButton;

    @BindView(R.id.link_login)
    TextView _loginLink;

    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        this.context = this;
        ButterKnife.bind(this);


        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do the sign up retrofit in this method
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });

    }

    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        String name = _nameText.getText().toString();
        String[] firstAndLast = name .split(" ");
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        LambencyAPIHelper.getInstance().registerUser(email,firstAndLast[0],firstAndLast[1],password).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.body() == null || response.code() != 200) {
                    Toast.makeText(getApplicationContext(), "Server error!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //when response is back
                Integer ret = response.body();
                if (ret == 0) {
                    System.out.println("Successfully registered the user");
                    Toast.makeText(getApplicationContext(), "User registration successfull", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                    startActivity(intent);
                } else if (ret == 1){
                    System.out.println("Bad email address");
                    Toast.makeText(getApplicationContext(), "Bad email address", Toast.LENGTH_LONG).show();
                }
                else if (ret ==2){
                    System.out.println("Not unique email");
                    Toast.makeText(getApplicationContext(), "Not unique email", Toast.LENGTH_LONG).show();
                }
                else{
                    System.out.println("wrong verification code for user");
                    Toast.makeText(getApplicationContext(), "wrong verification code for user", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                //when failure
                System.out.println("FAILED CALL");
            }
        });

    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if ((name.isEmpty() || name.length() < 3) || name.split(" ").length < 2) {
            _nameText.setError("please enter a valid first and last name");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

}
