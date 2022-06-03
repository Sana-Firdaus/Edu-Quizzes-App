package com.example.eduquizzes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView register,loginbtn,forgotpassword;
    private EditText EmailId,Password;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register = findViewById(R.id.register);
        register.setOnClickListener(this);

        loginbtn = findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(this);

        EmailId =(EditText) findViewById(R.id.EmailId);
        Password=(EditText) findViewById(R.id.Password);

        firebaseAuth=FirebaseAuth.getInstance();

        forgotpassword=(TextView) findViewById(R.id.forgotpassword);
        forgotpassword.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this,Register.class));
                break;
            case R.id.loginbtn:
                userLogin();
                break;
            case R.id.forgotpassword:
                startActivity(new Intent(this,forgotPassword.class));
                break;
        }
    }

    private void userLogin() {
        String email=EmailId.getText().toString().trim();
        String password=Password.getText().toString().trim();

        if (email.isEmpty()) {
            EmailId.setError("Email address is required");
            EmailId.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            EmailId.setError("Please provide valid email");
            EmailId.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            Password.setError("Password is required");
            Password.requestFocus();
            return;
        }
        if (password.length() < 6) {
            Password.setError("Minimum password length should be 6 characters");
            Password.requestFocus();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                 if(task.isSuccessful()){
                     FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();

                     if(user.isEmailVerified()){
                     //redirect to categories
                     startActivity(new Intent(LoginActivity.this,Categories.class));
                 }else{
                         user.sendEmailVerification();
                         Toast.makeText(LoginActivity.this, "Check your email to verify your account", Toast.LENGTH_LONG).show();
                     }

                 }else{
                     Toast.makeText(LoginActivity.this, "Failed to login Please check credentials", Toast.LENGTH_LONG).show();
                 }
            }
        });

    }
}