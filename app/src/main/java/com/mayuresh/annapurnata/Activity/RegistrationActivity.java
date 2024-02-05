package com.mayuresh.annapurnata.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mayuresh.annapurnata.ModelClass.Users;
import com.mayuresh.annapurnata.R;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {

    EditText name1, phone1, aadhar1, email1, password1;
    Button register;
    TextView logintv;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressDialog pg;
    String email_pattern = "^[_A-Za-z0-9-+]+(\\\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\\\.[A-Za-z0-9]+)*(\\\\.[A-Za-z]{2,})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        pg = new ProgressDialog(this);
        pg.setMessage("Please Wait...");
        pg.setCancelable(false);

        name1 = findViewById(R.id.name);
        phone1 = findViewById(R.id.phone);
        aadhar1 = findViewById(R.id.aadhar);
        email1 = findViewById(R.id.email);
        password1 = findViewById(R.id.password);
        register = findViewById(R.id.register);
        logintv = findViewById(R.id.logintv);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        register.setOnClickListener(view -> {

            pg.show();

            String name=name1.getText().toString();
            String phone=phone1.getText().toString();
            String aadhar=aadhar1.getText().toString();
            String email=email1.getText().toString();
            String password=password1.getText().toString();

            if(TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(aadhar) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
            {
                pg.dismiss();
                Toast.makeText(RegistrationActivity.this, "Please Fill All Details",Toast.LENGTH_LONG).show();
            }
            else if(phone.length()!=10)
            {
                pg.dismiss();
                Toast.makeText(RegistrationActivity.this, "Please Enter Valid Phone Number",Toast.LENGTH_LONG).show();
            }
            else if(aadhar.length()!=12)
            {
                pg.dismiss();
                Toast.makeText(RegistrationActivity.this, "Please Enter Valid Aadhar",Toast.LENGTH_LONG).show();
            }
            else
            {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            reference=database.getReference().child("Users").child(aadhar);
                            Users user=new Users(name,phone,aadhar,email,password);

                            reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        pg.dismiss();
                                        Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegistrationActivity.this,HomeActivity.class));
                                    }
                                    else
                                    {
                                        pg.dismiss();
                                        Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else
                        {
                            pg.dismiss();
                            Toast.makeText(RegistrationActivity.this,"Code Error Again",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        logintv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                finish();
            }
        });

    }
}