package com.mayuresh.annapurnata.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.mayuresh.annapurnata.R;

public class HomeActivity extends AppCompatActivity {

    CardView donate, receive, drivers, aboutus;
    FirebaseAuth auth;
    Button logout;
    private boolean doubleBackToExitPressedOnce = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logout=findViewById(R.id.logout_btn);
        auth=FirebaseAuth.getInstance();

        donate=findViewById(R.id.donate);
        receive=findViewById(R.id.receive);
        drivers=findViewById(R.id.drivers);
        aboutus=findViewById(R.id.aboutus);

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, DonationActivity.class));
            }
        });

        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ReceiverActivity.class));
            }
        });

        drivers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, DriversActivity.class));
            }
        });

        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, AboutActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
            }
        });

        if(auth.getCurrentUser()==null)
        {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        }
    }

    public void onBackPressed()
    {
        if(doubleBackToExitPressedOnce)
        {
            super.onBackPressed();
            return;
        }

        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();
        doubleBackToExitPressedOnce = true;
    }
}

//Color Palette:- https://www.canva.com/colors/color-palettes/mermaid-lagoon/