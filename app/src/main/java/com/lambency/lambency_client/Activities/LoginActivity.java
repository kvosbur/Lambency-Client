package com.lambency.lambency_client.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;


import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.lambency.lambency_client.Models.UserAuthenticatorModel;
import com.lambency.lambency_client.Models.UserModel;
import com.lambency.lambency_client.Networking.LambencyAPI;
import com.lambency.lambency_client.Networking.LambencyAPIHelper;
import com.lambency.lambency_client.R;
import com.lambency.lambency_client.Utils.SharedPrefsHelper;


import org.json.JSONObject;

import java.util.Arrays;

import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private TextView mStatusTextView;

    final private Context context = this;


    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(context);

        if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }else {

            //skip login activity if there is already an oAuthToken saved to shared preferences
            SharedPreferences sharedPref = SharedPrefsHelper.getSharedPrefs(context);
            final String myauth = sharedPref.getString("myauth", "");

            if (myauth.length() > 0) {
                UserAuthenticatorModel.myAuth = myauth;
                System.out.println("My auth is : " + myauth);
                LambencyAPIHelper.getInstance().userSearch(UserAuthenticatorModel.myAuth, null).enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.body() == null || response.code() != 200) {
                            System.out.println("ERROR!!!!!");
                            return;
                        }
                        //when response is back
                        UserModel.myUserModel = response.body();
                        if (response.body() == null) {
                            System.out.println("ERROR NULLED!!!!");
                            Toast.makeText(getApplicationContext(), "USER NULL", Toast.LENGTH_LONG).show();
                            return;
                        }
                        System.out.println("got the user object");

                        //System.out.println("SUCCESS");
                        Intent myIntent = new Intent(LoginActivity.this, BottomBarActivity.class);
                        startActivity(myIntent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable throwable) {
                        //when failure
                        System.out.println("FAILED CALL");
                        Toast.makeText(getApplicationContext(), "Login error... Please try again", Toast.LENGTH_LONG).show();

                    }
                });
            }


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            TextView forSignup = findViewById(R.id.link_signup);
            forSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    startActivity(intent);
                }
            });

            Intent intent = getIntent();
            Uri data = intent.getData();

            //Bundle for deeplink
            Bundle bundle = getIntent().getExtras();
            if (bundle != null || data != null) {
                //TODO error check
                if (data != null) {
                    String userId = data.getQueryParameter("uid");
                    String verificationCode = data.getQueryParameter("code");
                    LambencyAPIHelper.getInstance().verifyEmail(Integer.parseInt(userId),verificationCode).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if (response.body() == null || response.code() != 200) {
                                Toast.makeText(getApplicationContext(), "Server error!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            //when response is back
                            Integer ret = response.body();
                            if (ret != 0 ) {
                                System.out.println("Error has occurred here inside verification");
                            } else {
                                System.out.println("Successfully verified the user");
                                Toast.makeText(getApplicationContext(), "User verification successfull", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            System.out.println("Server error");
                        }
                    });
                }
            }

            //Login with email and password
            final EditText emailAddr = findViewById(R.id.input_email);
            final EditText passWord = findViewById(R.id.input_password);
            Button logInButton = findViewById(R.id.btn_login);

            logInButton.setOnClickListener(new View.OnClickListener() {
                boolean check = true;
                @Override
                public void onClick(View v) {
                    if (emailAddr.getText().toString().matches("")){
                        Toast.makeText(getApplicationContext(), "Email address empty", Toast.LENGTH_LONG).show();
                        check = false;
                    }
                    if (passWord.getText().toString().matches("")){
                        Toast.makeText(getApplicationContext(), "Password is empty", Toast.LENGTH_LONG).show();
                        check = false;
                    }
                    if (check){
                        LambencyAPIHelper.getInstance().loginUser(emailAddr.getText().toString(),passWord.getText().toString()).enqueue(new Callback<UserAuthenticatorModel>() {
                            @Override
                            public void onResponse(Call<UserAuthenticatorModel> call, Response<UserAuthenticatorModel> response) {
                                if (response.body() == null || response.code() != 200) {
                                    Toast.makeText(getApplicationContext(), "Server error!", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                UserAuthenticatorModel userAuthenticatorModel = response.body();
                                if (userAuthenticatorModel.getStatus() == UserAuthenticatorModel.Status.SUCCESS){
                                    Toast.makeText(getApplicationContext(), "Got User Object", Toast.LENGTH_LONG).show();
                                    System.out.println("got the user object");
                                    UserAuthenticatorModel.myAuth = userAuthenticatorModel.getoAuthCode();

                                    SharedPreferences sharedPref = SharedPrefsHelper.getSharedPrefs(context);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("myauth", UserAuthenticatorModel.myAuth);
                                    editor.apply();

                                    LambencyAPIHelper.getInstance().userSearch(UserAuthenticatorModel.myAuth, null).enqueue(new Callback<UserModel>() {
                                        @Override
                                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                            if (response.body() == null || response.code() != 200) {
                                                System.out.println("ERROR!!!!!");
                                                return;
                                            }
                                            //when response is back
                                            UserModel.myUserModel = response.body();
                                            if (response.body() == null) {
                                                System.out.println("ERROR NULLED!!!!");
                                                Toast.makeText(getApplicationContext(), "USER NULL", Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                            Toast.makeText(getApplicationContext(), "Got User Object", Toast.LENGTH_LONG).show();
                                            System.out.println("got the user object");

                                            //System.out.println("SUCCESS");
                                            Intent myIntent = new Intent(LoginActivity.this, BottomBarActivity.class);
                                            startActivity(myIntent);
                                            finish();
                                        }

                                        @Override
                                        public void onFailure(Call<UserModel> call, Throwable throwable) {
                                            //when failure
                                            System.out.println("FAILED CALL");
                                            Toast.makeText(getApplicationContext(), "Something went wrong please try again", Toast.LENGTH_LONG).show();

                                        }
                                    });
                                }
                                else if (userAuthenticatorModel.getStatus() == UserAuthenticatorModel.Status.NON_UNIQUE_EMAIL){
                                    Toast.makeText(getApplicationContext(), "Email has yet to be verified", Toast.LENGTH_LONG).show();
                                }
                                else if (userAuthenticatorModel.getStatus() == UserAuthenticatorModel.Status.INVALID_LOGIN){
                                    Toast.makeText(getApplicationContext(), "Email or password is incorrect", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    System.out.println("non deterministic error occurred in login");
                                    Toast.makeText(getApplicationContext(), "An error occured", Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<UserAuthenticatorModel> call, Throwable t) {
                                System.out.println("Server Error occurred in login");
                            }
                        });
                    }

                }
            });


            callbackManager = CallbackManager.Factory.create();
            //add permissions for Facebook to get email
            LoginButton loginButton = findViewById(R.id.login_button);
            loginButton.clearPermissions();
            loginButton.setReadPermissions(Arrays.asList("email"));


            //String googleWebID = "406595282653-87c0rdih5bqi4nrei8catgh3pq1usith.apps.googleusercontent.com";
            String googleWebID = "801710608826-06vpf384rl9nfcbumav56niql251419n.apps.googleusercontent.com";


            // Configure sign-in to request the user's ID, email address, and basic
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(googleWebID)
                    .requestEmail()
                    .build();

            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            findViewById(R.id.sign_in_button).setOnClickListener(this);
            findViewById(R.id.disconnect_button).setOnClickListener(this);

            //LoginManager.getInstance()

            //Forogot password on click
            TextView forgotPassword = findViewById(R.id.link_forgotPassword);
            forgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Enter email to send");

                    // Set up the input
                    final EditText input = new EditText(LoginActivity.this);
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    builder.setView(input);
                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LambencyAPIHelper.getInstance().beginPasswordRecovery(input.getText().toString()).enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                    if (response.body() == null || response.code() != 200) {
                                        System.out.println("ERROR!!!!!");
                                        return;
                                    }

                                    int ret = response.body();
                                    if (ret == 0){
                                        System.out.println("success in forgot password");
                                        Toast.makeText(LoginActivity.this, "email sent was " + input.getText().toString(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else if (ret < 0){
                                        System.out.println("email was not assoicated with an account");
                                        Toast.makeText(LoginActivity.this, "email not associated with an account ",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else if (ret > 0){
                                        System.out.println("general server error");
                                        Toast.makeText(LoginActivity.this, "general server error occurred",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {
                                    System.out.println("Error occured in forgot my password");
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });


            loginButton.registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            // App code

                            AccessToken accessToken = loginResult.getAccessToken();

                            GraphRequest request = GraphRequest.newMeRequest(
                                    accessToken,
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(
                                                JSONObject object,
                                                GraphResponse response) {
                                            // Application code

                                            try {
                                                String id = (String) object.get("id");
                                                String firstName = (String) object.get("first_name");
                                                String lastName = (String) object.get("last_name");
                                                String email = (String) object.get("email");

                                                System.out.println("Hello " + firstName + " " + lastName + " with email " + email + " id: " + id);

                                                LambencyAPIHelper.getInstance().getFacebookLogin(id, firstName, lastName, email).enqueue(new Callback<UserAuthenticatorModel>() {
                                                    @Override
                                                    public void onResponse(Call<UserAuthenticatorModel> call, Response<UserAuthenticatorModel> response) {
                                                        if (response.body() == null || response.code() != 200) {
                                                            System.out.println("ERROR!!!!!");
                                                            return;
                                                        }
                                                        //when response is back


                                                        UserAuthenticatorModel ua = response.body();

                                                        if (ua != null && ua.getStatus() == UserAuthenticatorModel.Status.SUCCESS) {
                                                            //System.out.println("SUCCESS");
                                                            UserAuthenticatorModel.myAuth = ua.getoAuthCode();

                                                            //save myauth into shared preferences
                                                            SharedPreferences sharedPref = SharedPrefsHelper.getSharedPrefs(context);
                                                            SharedPreferences.Editor editor = sharedPref.edit();
                                                            editor.putString("myauth", ua.getoAuthCode());
                                                            editor.apply();

                                                            LambencyAPIHelper.getInstance().userSearch(UserAuthenticatorModel.myAuth, null).enqueue(new Callback<UserModel>() {
                                                                @Override
                                                                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                                                    if (response.body() == null || response.code() != 200) {
                                                                        System.out.println("ERROR!!!!!");
                                                                        return;
                                                                    }
                                                                    //when response is back
                                                                    UserModel.myUserModel = response.body();
                                                                    if (response.body() == null) {
                                                                        System.out.println("ERROR NULLED!!!!");
                                                                        Toast.makeText(getApplicationContext(), "USER NULL", Toast.LENGTH_LONG).show();
                                                                        return;
                                                                    }
                                                                    Toast.makeText(getApplicationContext(), "Got User Object", Toast.LENGTH_LONG).show();
                                                                    System.out.println("got the user object");

                                                                    Intent myIntent = new Intent(LoginActivity.this, BottomBarActivity.class);
                                                                    startActivity(myIntent);
                                                                    finish();
                                                                    //System.out.println("SUCCESS");

                                                                    //Intent myIntent = new Intent(LoginActivity.this, BottomBarActivity.class);
                                                                    //startActivity(myIntent);
                                                                    //finish();
                                                                }

                                                                @Override
                                                                public void onFailure(Call<UserModel> call, Throwable throwable) {
                                                                    //when failure
                                                                    System.out.println("FAILED CALL");
                                                                    Toast.makeText(getApplicationContext(), "Something went wrong please try again", Toast.LENGTH_LONG).show();

                                                                }
                                                            });
                                                        } else if (ua.getStatus() == UserAuthenticatorModel.Status.NON_DETERMINANT_ERROR) {
                                                            //System.out.println("NON_DETERMINANT_ERROR");
                                                            Toast.makeText(getApplicationContext(), "NON_DETERMINANT_ERROR", Toast.LENGTH_LONG).show();
                                                            Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_LONG).show();

                                                        } else if (ua.getStatus() == UserAuthenticatorModel.Status.NON_UNIQUE_EMAIL) {
                                                            //System.out.println("NON_UNIQUE_EMAIL");
                                                            Toast.makeText(getApplicationContext(), "NON_UNIQUE_EMAIL", Toast.LENGTH_LONG).show();
                                                            Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_LONG).show();

                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<UserAuthenticatorModel> call, Throwable throwable) {
                                                        //when failure
                                                        System.out.println("FAILED CALL");
                                                        System.out.println(throwable.getMessage());

                                                        Toast.makeText(getApplicationContext(), "Failed to Communicate with Server please try again.", Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Toast.makeText(getApplicationContext(), "Exception Occured (probs email)", Toast.LENGTH_LONG).show();
                                                System.out.println("Messed up");
                                            }
                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,first_name,last_name, email");
                            request.setParameters(parameters);
                            request.executeAsync();
                        }

                        @Override
                        public void onCancel() {
                            // App code
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                        }
                    });
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
        // [END on_start_sign_in]
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.disconnect_button:
                signOut();
                break;
        }
    }

    // [START signIn]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]


    // [START signOut]
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.disconnect_button).setVisibility(View.VISIBLE);

        } else {
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.disconnect_button).setVisibility(View.GONE);
        }
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            String idToken = account.getIdToken();
            // Signed in successfully, show authenticated UI.
            LambencyAPIHelper.getInstance().getGoogleLogin(idToken).enqueue(new Callback<UserAuthenticatorModel>() {
                @Override
                public void onResponse(Call<UserAuthenticatorModel> call, Response<UserAuthenticatorModel> response) {
                    if (response.body() == null || response.code() != 200) {
                        System.out.println("ERROR!!!!!");
                        return;
                    }
                    //when response is back
                    UserAuthenticatorModel ua = response.body();
                    String authCode = ua.getoAuthCode();
                    //System.out.println(ua.getoAuthCode());
                    //System.out.println(ua.getStatus());
                    if(ua.getStatus() == UserAuthenticatorModel.Status.SUCCESS){
                        //updateUI(account);

                        UserAuthenticatorModel.myAuth = ua.getoAuthCode();

                        //save myauth into shared preferences
                        SharedPreferences sharedPref = SharedPrefsHelper.getSharedPrefs(context);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("myauth", ua.getoAuthCode());
                        editor.apply();

                        LambencyAPIHelper.getInstance().userSearch(UserAuthenticatorModel.myAuth, null).enqueue(new Callback<UserModel>() {
                            @Override
                            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                if (response.body() == null || response.code() != 200) {
                                    System.out.println("ERROR!!!!!");
                                }

                                //when response is back
                                UserModel.myUserModel = response.body();
                                System.out.println("got the user object");

                                //System.out.println("SUCCESS");
                                Intent myIntent = new Intent(LoginActivity.this, BottomBarActivity.class);
                                startActivity(myIntent);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<UserModel> call, Throwable throwable) {
                                //when failure
                                System.out.println("FAILED CALL");
                                Toast.makeText(getApplicationContext(), "Something went wrong please try again", Toast.LENGTH_LONG).show();

                            }
                        });

                    }
                    else if(ua.getStatus() == UserAuthenticatorModel.Status.NON_DETERMINANT_ERROR){
                        //System.out.println("NON_DETERMINANT_ERROR");
                        Toast.makeText(getApplicationContext(), "NON_DETERMENANT ERROR.", Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_LONG).show();


                    }
                    else if(ua.getStatus() == UserAuthenticatorModel.Status.NON_UNIQUE_EMAIL){
                        //System.out.println("NON_UNIQUE_EMAIL");
                        Toast.makeText(getApplicationContext(), "non unique email.", Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<UserAuthenticatorModel> call, Throwable throwable) {
                    //when failure
                    System.out.println("FAILED CALL");
                    Toast.makeText(getApplicationContext(), "Something went wrong please try again", Toast.LENGTH_LONG).show();

                }
            });

            /*updateUI(account);

            Intent myIntent = new Intent(LoginActivity.this, BottomBarActivity.class);
            startActivity(myIntent);
            finish();*/
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
    // [END handleSignInResult]




}
