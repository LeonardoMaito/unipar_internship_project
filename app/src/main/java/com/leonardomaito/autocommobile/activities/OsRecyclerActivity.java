package com.leonardomaito.autocommobile.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.leonardomaito.autocommobile.adapters.FirestoreAdapter;
import com.leonardomaito.autocommobile.models.ServiceDocument;

import autocommobile.R;

public class OsRecyclerActivity extends AppCompatActivity {
    private FirestoreAdapter listAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public static Activity self_intent;
    private int updateOption = 0;

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os);

        CollectionReference osRef = db.collection("userData").document(user.getUid())
                .collection("ServiceOrder");

        self_intent = this;

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,

                DividerItemDecoration.VERTICAL);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewOs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(dividerItemDecoration);

        Query query = osRef.orderBy("serviceOrder",
                Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ServiceDocument> options =
                new FirestoreRecyclerOptions.Builder<ServiceDocument>()
                        .setQuery(query, ServiceDocument.class)
                        .build();

        listAdapter = new FirestoreAdapter(options);
        recyclerView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onStart() {
        super.onStart();
        listAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        listAdapter.stopListening();
    }

    public void createNewOs(View view) {
        Intent newOsIntent = new Intent(this, ClientActivity.class);
        newOsIntent.putExtra("updateOption",updateOption);
        startActivity(newOsIntent);
        finish();

    }

    public void logOut(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle("Autocom Mobile");
        alert.setMessage("Você tem certeza que deseja sair do aplicativo?");
        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                Intent newOsIntent = new Intent(OsRecyclerActivity.this, LoginActivity.class);
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