package com.lambency.lambency_client.Activities;

import android.content.Context;
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

import com.lambency.lambency_client.Models.EventModel;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;
import com.ybs.passwordstrengthmeter.PasswordStrength;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity implements TextWatcher {
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

        _passwordText.addTextChangedListener(this);

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
                    Toast.makeText(getApplicationContext(), "User registration successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                    startActivity(intent);
                } else if (ret == 1){
                    System.out.println("Bad email address");
                    Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_LONG).show();
                }
                else if (ret ==2){
                    System.out.println("Not unique email");
                    Toast.makeText(getApplicationContext(), "There is already an account with that email address", Toast.LENGTH_LONG).show();
                }
                else{
                    System.out.println("wrong verification code for user");
                    Toast.makeText(getApplicationContext(), "There was an error on Server", Toast.LENGTH_LONG).show();
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

        if (password.isEmpty() || password.length() < 8 || password.length() > 15) {
            _passwordText.setError("Password must be atleast 8 characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
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
