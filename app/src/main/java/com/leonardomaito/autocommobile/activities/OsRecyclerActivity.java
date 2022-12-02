package com.leonardomaito.autocommobile.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
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
import com.leonardomaito.autocommobile.adapters.ServiceSearchAdapter;
import com.leonardomaito.autocommobile.models.Client;
import com.leonardomaito.autocommobile.models.ClientDocument;
import com.leonardomaito.autocommobile.models.ClientOs;
import com.leonardomaito.autocommobile.models.ServiceDocument;
import com.leonardomaito.autocommobile.models.ServiceOrder;
import com.leonardomaito.autocommobile.models.Vehicle;

import java.util.ArrayList;
import java.util.List;

import autocommobile.R;

public class OsRecyclerActivity extends AppCompatActivity {

    private ServiceSearchAdapter serviceSearchAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private CollectionReference osRef = db.collection("userData").document(user.getUid())
            .collection("ServiceOrder");

    private RecyclerView osRecyclerView;

    private List<ServiceOrder> serviceOrderList = new ArrayList<>();

    private EditText edSearch;

    private TextView tvUser;

    private AlertDialog alertDialog;

    public static Activity self_intent;
    private int updateOption = 0;

    private ClientOs client;
    private Vehicle vehicle;
    private String service;
    private String observation;
    private String paymentForm;
    private String date;
    private double totalValue;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os);
        self_intent = this;

        tvUser = findViewById(R.id.tvHeaderLoggedUser);
        edSearch = findViewById(R.id.etSearch);
        tvUser.setText(user.getDisplayName());
        osRecyclerView = findViewById(R.id.recyclerViewOs);

        try{
            VehicleActivity.self_intent.finish();
        }catch(Exception e){
            Log.e("Error","No intent");
        }

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
        serviceOrderList.clear();
        serviceSearchAdapter.notifyDataSetChanged();
        getDataFromFirestore();
        buildRecyclerView();
    }

    private void filter(String toString) {

        ArrayList<ServiceOrder> filteredList = new ArrayList<>();

        for (ServiceOrder item : serviceOrderList) {
            if (item.getClient().getName().toLowerCase().contains(toString.toLowerCase())) {
                filteredList.add(item);
            }
        }
        serviceSearchAdapter.filterList(filteredList);
    }

    private void buildRecyclerView() {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        osRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        osRecyclerView.setHasFixedSize(true);
        osRecyclerView.addItemDecoration(dividerItemDecoration);
        serviceSearchAdapter = new ServiceSearchAdapter(serviceOrderList, this);
        osRecyclerView.setAdapter(serviceSearchAdapter);
    }

    private void getDataFromFirestore() {

        CollectionReference docRef =
                db.collection("userData")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("ServiceOrder");

        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ServiceOrder serviceDocument = document.toObject(ServiceDocument.class).serviceOrder;
                                client = serviceDocument.getClient();
                                vehicle = serviceDocument.getVehicle();
                                service = serviceDocument.getService();
                                observation = serviceDocument.getObservation();
                                paymentForm = serviceDocument.getPaymentForm();
                                totalValue = serviceDocument.getTotalValue();
                                date = serviceDocument.getDate();
                                id = document.getId();

                                serviceOrderList.add(new ServiceOrder.ServiceOrderBuilder(client,vehicle,service,paymentForm,totalValue,date)
                                        .observation(observation)
                                        .id(id)
                                        .build());

                                serviceSearchAdapter.notifyDataSetChanged();

                            }
                        } else {
                            Log.d("DB Error", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    public void createNewOs(View view) {
        Intent newOsIntent = new Intent(this, VehicleActivity.class);
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