package com.lambency.lambency_client.Activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.lambency.lambency_client.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.myFAB)
    FloatingActionButton myButton;

    @BindView(R.id.firstName)
    TextView firstNameText;

    @BindView(R.id.editFirstName)
    EditText editFirstName;

    @BindView(R.id.lastName)
    TextView lastNameText;

    @BindView(R.id.editLastName)
    EditText editLastName;

    @BindView(R.id.phoneNum)
    TextView phoneNum;

    @BindView(R.id.editPhoneNum)
    EditText editPhoneNum;

    @BindView(R.id.emailOfUser)
    TextView emailOfUser;

    @BindView(R.id.editEmail)
    EditText editEmail;

    boolean edit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.myFAB)
    public void clickSend()
    {

        if(!edit)
        {
            firstNameText.setVisibility(View.INVISIBLE);
            editFirstName.setText(firstNameText.getText());
            editFirstName.setVisibility(View.VISIBLE);

            lastNameText.setVisibility(View.INVISIBLE);
            editLastName.setText(lastNameText.getText());
            editLastName.setVisibility(View.VISIBLE);

            phoneNum.setVisibility(View.INVISIBLE);
            editPhoneNum.setText(phoneNum.getText());
            editPhoneNum.setVisibility(View.VISIBLE);

            emailOfUser.setVisibility(View.INVISIBLE);
            editEmail.setText(emailOfUser.getText());
            editEmail.setVisibility(View.VISIBLE);
            edit = true;

            myButton.setImageResource(R.drawable.ic_check_black_24dp);
        }
        else
        {
            editFirstName.setVisibility(View.INVISIBLE);
            firstNameText.setText(editFirstName.getText());
            firstNameText.setVisibility(View.VISIBLE);

            editLastName.setVisibility(View.INVISIBLE);
            lastNameText.setText(editLastName.getText());
            lastNameText.setVisibility(View.VISIBLE);

            editPhoneNum.setVisibility(View.INVISIBLE);
            phoneNum.setText(editPhoneNum.getText());
            phoneNum.setVisibility(View.VISIBLE);

            editEmail.setVisibility(View.INVISIBLE);
            emailOfUser.setText(editEmail.getText());
            emailOfUser.setVisibility(View.VISIBLE);
            edit = false;

            myButton.setImageResource(R.drawable.ic_mode_edit_black_24dp);
        }


    }
}
