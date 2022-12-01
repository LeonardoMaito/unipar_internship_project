package com.leonardomaito.autocommobile.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;


import autocommobile.R;

public class MenuActivity extends AppCompatActivity {

    private TextView tvMenu, tvUser;
    private Button btOpenOs, btOpenClient;
    private int updateOption = 0;
    private AlertDialog alertDialog;
    private Boolean newUser;
    private String username;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        try {
            Bundle extra = getIntent().getExtras();
            newUser = extra.getBoolean("newUser");
            Log.e("Bundle", "" + newUser.booleanValue());
        }
        catch(Exception e){
            Log.e("Error", "sem bundle");
        }

        tvUser = findViewById(R.id.tvHeaderLoggedUser);
        tvMenu = findViewById(R.id.tvMainMenu);
        btOpenOs = findViewById(R.id.btMenuOs);
        btOpenClient = findViewById(R.id.btOpenClient);

        setUserName(newUser);

    }

    private void setUserName(Boolean newUser) {

        try {
            if (newUser) {
                final EditText input = new EditText(MenuActivity.this);
                AlertDialog.Builder alert = new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle("Autocom Mobile")
                        .setMessage("Insira um nome de usuário: ")
                        .setView(input);
                alert.setPositiveButton("Concluído", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        username = input.getText().toString();

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("TAG", "User profile updated.");
                                            tvUser.setText(user.getDisplayName());
                                        }
                                    }
                                });
                        alertDialog.dismiss();
                    }
                });
                alertDialog = alert.create();
                alertDialog.show();
            }
        }catch(Exception e){
                Log.e("TAG","Usuário antigo");
        }
    }

    public void openOsActivity(View view) {
        Intent osIntent = new Intent(this, OsRecyclerActivity.class);
        osIntent.putExtra("updateOption", updateOption);
        startActivity(osIntent);

    }


    public void openClientActivity(View view) {
        Intent clientMenuIntent = new Intent(this, ClientMenuActivity.class);
        startActivity(clientMenuIntent);
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