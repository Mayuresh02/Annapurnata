package com.mayuresh.annapurnata.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mayuresh.annapurnata.R;

public class LoginActivity extends AppCompatActivity {

    EditText email1,password1;
    TextView registrationtv;
    FirebaseAuth auth;
    Button login;
    ProgressDialog pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email1=findViewById(R.id.email);
        password1=findViewById(R.id.password);

        pg = new ProgressDialog(this);
        pg.setMessage("Please Wait...");
        pg.setCancelable(false);

        registrationtv = findViewById(R.id.registrationtv);
        login = findViewById(R.id.login);

        auth=FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pg.show();
                String email = email1.getText().toString();
                String password = password1.getText().toString();

                 if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                 {
                     pg.dismiss();
                     Toast.makeText(LoginActivity.this,"Please Enter Email OR Password",Toast.LENGTH_LONG).show();
                 }
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            pg.dismiss();
                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            finishAffinity();
                        }
                        else
                        {
                            pg.dismiss();
                            Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        registrationtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
    }
}