package com.leonardomaito.autocommobile.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.leonardomaito.autocommobile.adapters.FirestoreAdapter;
import com.leonardomaito.autocommobile.models.ServiceOrder;

import autocommobile.R;

public class OsActivity extends AppCompatActivity {

    private FirestoreAdapter listAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference osRef = db.collection("cliente");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os);

        setupRecyclerView();

        }

        private void setupRecyclerView() {

            RecyclerView recyclerView = findViewById(R.id.recyclerViewOs);

            Query query = osRef.orderBy("ServiceOrder",
                    Query.Direction.ASCENDING);

            FirestoreRecyclerOptions<ServiceOrder> options =
                    new FirestoreRecyclerOptions.Builder<ServiceOrder>()
                            .setQuery(query, ServiceOrder.class)
                            .build();

            listAdapter = new FirestoreAdapter(options);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(listAdapter);
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