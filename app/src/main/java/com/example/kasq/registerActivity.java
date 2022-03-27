package com.example.kasq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kasq.Controller.UserController;
import com.example.kasq.Model.User;


public class registerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        onClick();
    }

    EditText etUserName, etUserPassword;
    Button btnRegister,btnCancel;
    DBHelper dataHelper;
    private void init(){
        etUserName = findViewById(R.id.etUserName);
        etUserPassword = findViewById(R.id.etUserPassword);
        dataHelper = new DBHelper(this);
        btnRegister = findViewById(R.id.btnSignUp);
        btnCancel = findViewById(R.id.btnCancelSet);
        getSupportActionBar().hide();
    }

    private void onClick(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = etUserName.getText().toString();
                String userPassword = etUserPassword.getText().toString();

                if(etUserName.getText().toString().equals("")){
                    Toast.makeText(registerActivity.this, "please insert username", Toast.LENGTH_SHORT).show();
                    etUserName.requestFocus();
                }else if (etUserPassword.getText().toString().equals("")){
                    Toast.makeText(registerActivity.this, "please insert your password", Toast.LENGTH_SHORT).show();
                    etUserPassword.requestFocus();
                }else{
                    User user = new UserController().createUser(userName,userPassword);

                    boolean checkregisUser = dataHelper.registerUser(user);
                    if(checkregisUser==true){
                        Toast.makeText(registerActivity.this, "New User Inserted", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(registerActivity.this, "New User not Inserted", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(registerActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}