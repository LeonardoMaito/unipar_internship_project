package com.leonardomaito.autocommobile.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.leonardomaito.autocommobile.adapters.AutoCompleteAdapter;
import com.leonardomaito.autocommobile.controllers.VehicleController;
import com.leonardomaito.autocommobile.models.Client;
import com.leonardomaito.autocommobile.models.ClientDocument;
import com.santalu.maskara.widget.MaskEditText;

import java.util.ArrayList;
import java.util.List;

import autocommobile.R;

public class VehicleActivity extends AppCompatActivity {

    private Button btNext;
    private EditText etBrand;
    private EditText etModel;
    private MaskEditText etChassi;
    private EditText etYear;
    private EditText etColor;
    private EditText etKm;

    private AutoCompleteTextView acClient;
    private List<Client> acClientList = new ArrayList<>();
    private AutoCompleteAdapter autoCompleteAdapter;

    private VehicleController vehicleController = new VehicleController();

    private Client client;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference docRef =
            db.collection("userData")
                    .document(user.getUid())
                    .collection("Client");

    public static Activity self_intent;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os_vehicle);
        self_intent = this;

        getDataFromFire();

        btNext = findViewById(R.id.btNextOs2);
        etBrand = findViewById(R.id.etBrandInput);
        etModel = findViewById(R.id.etModelNumber);
        etChassi = findViewById(R.id.etChassiInput);
        etYear = findViewById(R.id.etYearInput);
        etColor = findViewById(R.id.etColorInput);
        etKm = findViewById(R.id.etKmInput);
        acClient = findViewById(R.id.acClient);

        autoCompleteAdapter = new AutoCompleteAdapter(this, acClientList);
        acClient.setAdapter(autoCompleteAdapter);

        acClient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                 client = new Client.ClientBuilder(acClientList.get(i).getName(),
                        acClientList.get(i).getCpf(), acClientList.get(i).getId())
                        .address(acClientList.get(i).getAddress())
                        .telephone(acClientList.get(i).getTelephone())
                        .build();

                Log.e("NewClient", " " +  client.toString());
            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                Intent createServiceOrderActivity = new Intent(getApplicationContext(), ServiceOrderActivity.class);

                createServiceOrderActivity.putExtra("novoCarro", vehicleController.returnNewVehicle(etBrand, etModel,
                        etChassi, etYear, etColor, etKm));
                createServiceOrderActivity.putExtra("novoCliente", client);
                startActivity(createServiceOrderActivity);
            }
        });
    }


    private void getDataFromFire() {
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (isFinishing() || isDestroyed()) return;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Client clientDocument = document.toObject(ClientDocument.class).client;

                                Log.d("DB success", document.getId() + " => " + document.getData());
                                String name = clientDocument.getName();
                                String cpf =clientDocument.getCpf();
                                String address = clientDocument.getAddress();
                                String phone = clientDocument.getTelephone();
                                //Integer id = Integer.parseInt(document.getString("id"));

                                Client newClient = new Client.ClientBuilder(name, cpf, 0).telephone(phone).address(address).build();
                                acClientList.add(newClient);
                                autoCompleteAdapter.updateList(acClientList);

                            }
                        } else {
                            Log.d("DB Error", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /*private void setViewForUpdate(String documentId, int updateOption, Client newClient) {

        DocumentReference idRef =
                db.collection("userData")
                        .document(user.getUid())
                        .collection("ServiceOrder")
                        .document(documentId);

        idRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Result", "DocumentSnapshot data: " + document.getData());
                        ServiceOrder serviceDocument = document.toObject(ServiceDocument.class).serviceOrder;
                        etBrand.setText(serviceDocument.getVehicle().getBrand());
                        etModel.setText(serviceDocument.getVehicle().getModel());
                        etChassi.setText(String.valueOf(serviceDocument.getVehicle().getChassi()));
                        etYear.setText(String.valueOf(serviceDocument.getVehicle().getYear()));
                        etColor.setText(serviceDocument.getVehicle().getColor());
                        etKm.setText(String.valueOf(serviceDocument.getVehicle().getKm()));
                    }
                }
            }
        });

        startOsSecond.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                Intent updateOs  = new Intent(getApplicationContext(), ServiceOrderActivity.class);

                updateOs.putExtra("novoCliente", newClient);
                updateOs.putExtra("novoCarro", vehicleController.returnNewVehicle(etBrand,etModel,
                        etChassi, etYear, etColor, etKm));
                updateOs.putExtra("documentId", documentId);
                updateOs.putExtra("updateOption", updateOption);

                startActivity(updateOs);
            }
        });
    }*/
}