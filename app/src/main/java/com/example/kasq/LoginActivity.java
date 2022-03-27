package com.example.kasq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kasq.Model.User;

import java.io.Serializable;

public class LoginActivity extends AppCompatActivity {

    DBHelper dataHelper = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        init();
        run();
    }

    TextView usernameText;
    EditText passwordText;
    Button btnLogin,btnRegister;
    private void init(){
        if(dataHelper == null){
            dataHelper = new DBHelper(this);
        }
        usernameText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordtext);
        btnLogin = findViewById(R.id.Login);
        btnRegister = findViewById(R.id.Register);
    }

    String userName,userPassword;
    private void run(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = usernameText.getText().toString();
                userPassword = passwordText.getText().toString();

                if(userName.equals("")){
                    Toast.makeText(LoginActivity.this, "Please check your Username", Toast.LENGTH_SHORT).show();
                    usernameText.requestFocus();
                }else if(userPassword.equals("")){
                    Toast.makeText(LoginActivity.this, "Please check your Password", Toast.LENGTH_SHORT).show();
                    usernameText.requestFocus();
                }else{
                    User user = dataHelper.loginUser(userName,userPassword);
                    if(user != null){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, registerActivity.class);
                startActivity(intent);
                finish();
            }
        });
        usernameText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    passwordText.requestFocus();
                    return true;
                }
                return false;
            }
        });
    }
}