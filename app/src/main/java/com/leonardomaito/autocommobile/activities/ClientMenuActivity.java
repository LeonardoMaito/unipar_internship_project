package com.leonardomaito.autocommobile.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.leonardomaito.autocommobile.adapters.ClientAdapter;
import com.leonardomaito.autocommobile.models.Client;
import com.leonardomaito.autocommobile.models.ClientDocument;

import autocommobile.R;

public class ClientMenuActivity extends AppCompatActivity {

    private ClientAdapter clientAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_menu);

        CollectionReference clientRef = db.collection("userData").document(user.getUid())
                .collection("Client");

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,

                DividerItemDecoration.VERTICAL);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewClient);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(dividerItemDecoration);

        Query query = clientRef.orderBy("client.id",
                Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ClientDocument> options =
                new FirestoreRecyclerOptions.Builder<ClientDocument>()
                        .setQuery(query, ClientDocument.class)
                        .build();

        clientAdapter = new ClientAdapter(options);
        recyclerView.setAdapter(clientAdapter);
        clientAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onStart() {
        super.onStart();
        clientAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        clientAdapter.stopListening();
    }

    public void createNewClient(View view) {
        Intent newClientIntent = new Intent(this, ClientActivity.class);
        startActivity(newClientIntent);
        finish();
    }
}