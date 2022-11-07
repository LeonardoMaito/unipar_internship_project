package com.leonardomaito.autocommobile.activities;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.leonardomaito.autocommobile.models.ServiceOrder;

import autocommobile.R;

public class MenuActivity extends AppCompatActivity {

    private TextView tvMenu;
    private Button btOpenOs;
    private int updateOption = 0;
    private AlertDialog alertDialog;

    private ServiceOrder serviceOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tvMenu = findViewById(R.id.tvMainMenu);
        btOpenOs = findViewById(R.id.btMenuOs);

    }

    public void openOsActivity(View view) {
        Intent osIntent = new Intent(this, OsRecyclerActivity.class);
        osIntent.putExtra("updateOption", updateOption);
        startActivity(osIntent);

    }


    public void logOut(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle("Autocom Mobile");
        alert.setMessage("Você tem certeza que deseja sair do aplicativo?");
        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                Intent newOsIntent = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(newOsIntent);
                finish();
            }
        });
        alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog = alert.create();
        alertDialog.show();
    }
}