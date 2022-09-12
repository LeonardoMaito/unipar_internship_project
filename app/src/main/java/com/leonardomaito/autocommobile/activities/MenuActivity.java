package com.leonardomaito.autocommobile.activities;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.leonardomaito.autocommobile.models.ServiceOrder;

import autocommobile.R;

public class MenuActivity extends AppCompatActivity {

    private TextView tvMenu;
    private Button btOpenOs;

    private ServiceOrder serviceOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tvMenu = findViewById(R.id.tvMainMenu);
        btOpenOs = findViewById(R.id.btNewOs);

    }

    public void openOsActivity(View view) {
        Intent osIntent = new Intent(this, OsActivity.class);
        startActivity(osIntent);

    }
}