package com.leonardomaito.autocommobile.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.leonardomaito.autocommobile.adapters.FirestoreAdapter;
import com.leonardomaito.autocommobile.models.ServiceDocument;
import com.leonardomaito.autocommobile.models.ServiceOrder;

import java.util.ArrayList;

import autocommobile.R;

public class OsActivity extends AppCompatActivity {
    private FirestoreAdapter listAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference osRef = db.collection("cliente");
    private DocumentReference docRef = osRef.document("clienteTeste");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewOs);

        /*docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.e("RESULTADO","result:" + document.toString());
                }
            }
        });*/

        Query query = osRef.orderBy("serviceOrder",
                Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ServiceDocument> options =
                new FirestoreRecyclerOptions.Builder<ServiceDocument>()
                        .setQuery(osRef, ServiceDocument.class)
                        .build();

         listAdapter = new FirestoreAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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
        Intent newOsIntent = new Intent(this, NewOsActivity.class);
        startActivity(newOsIntent);

    }
}