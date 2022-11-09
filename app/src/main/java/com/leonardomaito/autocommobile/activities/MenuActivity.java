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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.leonardomaito.autocommobile.models.ReservedID;
import com.leonardomaito.autocommobile.models.ServiceOrder;

import autocommobile.R;

public class MenuActivity extends AppCompatActivity {

    private TextView tvMenu;
    private Button btOpenOs;
    private int updateOption = 0;
    private AlertDialog alertDialog;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tvMenu = findViewById(R.id.tvMainMenu);
        btOpenOs = findViewById(R.id.btMenuOs);

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
                                    Log.d("Resultado", "Room already exists, start the chat");
                                }
                            } else {
                                DocumentReference idReference = db.collection("userData")
                                        .document(uID).collection("reservedID")
                                        .document("reservedProductId");

                                ReservedID reservedId = new ReservedID(0);
                                idReference.set(reservedId);
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
}