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

import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import com.leonardomaito.autocommobile.models.ReservedClientId;
import com.leonardomaito.autocommobile.models.ReservedID;



import autocommobile.R;

public class MenuActivity extends AppCompatActivity {

    private TextView tvMenu, tvUser;
    private Button btOpenOs, btOpenClient;
    private int updateOption = 0;
    private AlertDialog alertDialog;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tvUser = findViewById(R.id.tvHeaderLoggedUser);
        tvMenu = findViewById(R.id.tvMainMenu);
        btOpenOs = findViewById(R.id.btMenuOs);
        btOpenClient = findViewById(R.id.btOpenClient);

        verifyReservedId(user.getUid());

    }

    public void openOsActivity(View view) {
        Intent osIntent = new Intent(this, OsRecyclerActivity.class);
        osIntent.putExtra("updateOption", updateOption);
        startActivity(osIntent);

    }

    public void verifyReservedId(String uID){

        db.collection("userData").document(uID).collection("reservedID")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().size() > 0) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    Log.d("ReservedID", "Já Existe");
                                }
                            } else {
                                DocumentReference idServiceReference = db.collection("userData")
                                        .document(uID).collection("reservedID")
                                        .document("reservedServiceId");

                                ReservedID reservedServiceId = new ReservedID(0);
                                idServiceReference.set(reservedServiceId);

                                DocumentReference idClientReference = db.collection("userData")
                                        .document(uID).collection("reservedID")
                                        .document("reservedClientId");

                                ReservedClientId reservedClientId = new ReservedClientId(0);
                                idClientReference.set(reservedClientId);
                            }
                        } else {
                            Log.d("FTAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
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

    public void openClientActivity(View view) {
    }

}