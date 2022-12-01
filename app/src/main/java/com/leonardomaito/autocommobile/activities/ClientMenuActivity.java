package com.leonardomaito.autocommobile.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.leonardomaito.autocommobile.adapters.ClientSearchAdapter;
import com.leonardomaito.autocommobile.models.Client;
import com.leonardomaito.autocommobile.models.ClientDocument;
import com.leonardomaito.autocommobile.models.ClientOs;

import java.util.ArrayList;
import java.util.List;

import autocommobile.R;

public class ClientMenuActivity extends AppCompatActivity {

    private ClientSearchAdapter clientSearchAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private CollectionReference clientRef = db.collection("userData").document(user.getUid())
            .collection("Client");

    private RecyclerView clientRecyclerView;

    private List<ClientOs> clientList = new ArrayList<>();

    private EditText edSearch;

    private TextView tvUser;

    public static Activity self_intent;

    private String name;
    private String cpf;
    private String id;
    private String address;
    private String telephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_menu);
        self_intent = this;

        clientRecyclerView = findViewById(R.id.recyclerViewClient);
        edSearch = findViewById(R.id.etSearch);
        tvUser = findViewById(R.id.tvHeaderLoggedUser);

        tvUser.setText(user.getDisplayName());

        getDataFromFirestore();
        buildRecyclerView();

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    public void updateRecycle(){
        clientList.clear();
        clientSearchAdapter.notifyDataSetChanged();
        getDataFromFirestore();
        buildRecyclerView();
    }

    private void filter(String toString) {
        ArrayList<ClientOs> filteredList = new ArrayList<>();

        for (ClientOs item : clientList) {
            if (item.getName().toLowerCase().contains(toString.toLowerCase())) {
                filteredList.add(item);
            }
        }
        clientSearchAdapter.filterList(filteredList);
    }

    private void buildRecyclerView() {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        clientRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        clientRecyclerView.setHasFixedSize(true);
        clientRecyclerView.addItemDecoration(dividerItemDecoration);
        clientSearchAdapter = new ClientSearchAdapter(clientList, this);
        clientRecyclerView.setAdapter(clientSearchAdapter);
    }

    private void getDataFromFirestore() {

        clientRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Client clientDocument = document.toObject(ClientDocument.class).client;
                                name = clientDocument.getName();
                                cpf  = clientDocument.getCpf();
                                id = document.getId();
                                address  = clientDocument.getAddress();
                                telephone = clientDocument.getTelephone();

                                clientList.add( new ClientOs(name, address, telephone,  cpf, id));
                          ;
                                clientSearchAdapter.notifyDataSetChanged();

                            }
                        } else {
                            Log.d("DB Error", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void createNewClient(View view) {
        Intent newClientIntent = new Intent(this, ClientActivity.class);
        startActivity(newClientIntent);
        finish();
    }
}