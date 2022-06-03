package com.example.eduquizzes;

import static com.google.firebase.database.FirebaseDatabase.*;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.charset.StandardCharsets;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private TextView loginback, registerbtn;
    private EditText name, Phonenum, EmailAddress, Passwordinput;
    FirebaseDatabase rootNode;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();

        loginback = findViewById(R.id.loginback);
        loginback.setOnClickListener(this);

        registerbtn = findViewById(R.id.registerbtn);
        registerbtn.setOnClickListener(this);

        name = findViewById(R.id.name);
        Phonenum = findViewById(R.id.Phonenum);
        EmailAddress = findViewById(R.id.EmailAddress);
        Passwordinput = findViewById(R.id.Passwordinput);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.loginback:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.registerbtn:
                registerUser();
        }

    }

    private void registerUser() {
        String email = EmailAddress.getText().toString().trim();
        String fullname = name.getText().toString().trim();
        String phonenumber = Phonenum.getText().toString().trim();
        String password = Passwordinput.getText().toString().trim();

        if (fullname.isEmpty()) {
            name.setError("Full name is required");
            name.requestFocus();
            return;
        }
        if (phonenumber.isEmpty()) {
            Phonenum.setError("Phonenumber is required");
            Phonenum.requestFocus();
            return;
        }
        if(phonenumber.length()<10){
            Phonenum.setError("Please provide valid phone number");
            Phonenum.requestFocus();
            return;

        }
        if (email.isEmpty()) {
            EmailAddress.setError("Email address is required");
            EmailAddress.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            EmailAddress.setError("Please provide valid email");
            EmailAddress.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            Passwordinput.setError("Password is required");
            Passwordinput.requestFocus();
            return;
        }
        if (password.length() < 6) {
            Passwordinput.setError("Minimum password length should be 6 characters");
            Passwordinput.requestFocus();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if(task.isSuccessful()){
                    User user = new User(fullname,phonenumber,email);
                    FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(Register.this, "User has been registered Successfully", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(Register.this, "Failed to register", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(Register.this, "Failed to register", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}