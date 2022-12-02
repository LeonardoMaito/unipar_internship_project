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

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.leonardomaito.autocommobile.adapters.ClientAutoCompleteAdapter;
import com.leonardomaito.autocommobile.controllers.VehicleController;
import com.leonardomaito.autocommobile.models.Client;
import com.leonardomaito.autocommobile.models.ClientDocument;
import com.leonardomaito.autocommobile.models.ClientOs;
import com.leonardomaito.autocommobile.models.ServiceDocument;
import com.leonardomaito.autocommobile.models.ServiceOrder;
import com.leonardomaito.autocommobile.models.Vehicle;
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
    private List<ClientOs> acClientList = new ArrayList<>();
    private ClientAutoCompleteAdapter clientAutoCompleteAdapter;

    private VehicleController vehicleController = new VehicleController();

    private ClientOs client;
    private Vehicle vehicle;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference docRef =
            db.collection("userData")
                    .document(user.getUid())
                    .collection("Client");

    public static Activity self_intent;

    private int updateOption;
    private String documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os_vehicle);
        self_intent = this;

        btNext = findViewById(R.id.btNextOs2);
        etBrand = findViewById(R.id.etBrandInput);
        etModel = findViewById(R.id.etModelNumber);
        etChassi = findViewById(R.id.etChassiInput);
        etYear = findViewById(R.id.etYearInput);
        etColor = findViewById(R.id.etColorInput);
        etKm = findViewById(R.id.etKmInput);
        acClient = findViewById(R.id.acClient);

        try {
            Bundle data = getIntent().getExtras();
            documentId = data.getString("documentId");
            updateOption = data.getInt("updateOption");

        } catch (Exception e) {
            Log.e("TAG", "Sem bundle");
        }
        Log.e("Updateoption", "" + updateOption);

        if (updateOption == 1) {
            setViewId(documentId);

        } else if (updateOption == 2) {
            setViewForUpdate(documentId);

        } else {

            getDataFromFire();
            clientAutoCompleteAdapter = new ClientAutoCompleteAdapter(this, acClientList);
            acClient.setAdapter(clientAutoCompleteAdapter);

            acClient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    client = new ClientOs(acClientList.get(i).getName(), acClientList.get(i)
                            .getAddress(), acClientList.get(i).getTelephone(),
                            acClientList.get(i).getCpf(), acClientList.get(i).getId());

                }
            });

            btNext.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {

                    Intent createServiceOrderActivity = new Intent(getApplicationContext(), ServiceOrderActivity.class);

                    createServiceOrderActivity.putExtra("newVehicle", vehicleController.returnNewVehicle(etBrand, etModel,
                            etChassi, etYear, etColor, etKm));
                    createServiceOrderActivity.putExtra("newClient", client);
                    startActivity(createServiceOrderActivity);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent osRecycler = new Intent(getApplicationContext(), OsRecyclerActivity.class);
        startActivity(osRecycler);
        finish();

    }

    private void setViewId(String documentId) {

        DocumentReference docRef =
                db.collection("userData")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("ServiceOrder")
                        .document(documentId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Result", "DocumentSnapshot data: " + document.getData());
                        ServiceOrder serviceDocument = document.toObject(ServiceDocument.class).serviceOrder;
                        acClient.setText(serviceDocument.getClient().getName());
                        etBrand.setText(serviceDocument.getVehicle().getBrand());
                        etModel.setText(serviceDocument.getVehicle().getModel());
                        etChassi.setText(serviceDocument.getVehicle().getChassi());
                        etYear.setText(String.valueOf(serviceDocument.getVehicle().getYear()));
                        etColor.setText(serviceDocument.getVehicle().getColor());
                        etKm.setText(String.valueOf(serviceDocument.getVehicle().getKm()));

                    }
                }
            }
        });

        acClient.setEnabled(false);
        etBrand.setEnabled(false);
        etModel.setEnabled(false);
        etChassi.setEnabled(false);
        etYear.setEnabled(false);
        etColor.setEnabled(false);
        etKm.setEnabled(false);

        btNext.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                updateOption = 1;
                Intent intent = new Intent(VehicleActivity.this, ServiceOrderActivity.class);
                intent.putExtra("documentId", documentId);
                intent.putExtra("updateOption", updateOption);
                startActivity(intent);
            }
        });
    }

    private void setViewForUpdate(String documentId) {

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
                        acClient.setText(serviceDocument.getClient().getName());
                        etBrand.setText(serviceDocument.getVehicle().getBrand());
                        etModel.setText(serviceDocument.getVehicle().getModel());
                        etChassi.setText(String.valueOf(serviceDocument.getVehicle().getChassi()));
                        etYear.setText(String.valueOf(serviceDocument.getVehicle().getYear()));
                        etColor.setText(serviceDocument.getVehicle().getColor());
                        etKm.setText(String.valueOf(serviceDocument.getVehicle().getKm()));
                        client = serviceDocument.getClient();
                        vehicle = serviceDocument.getVehicle();
                    }
                }
            }
        });

        acClient.setEnabled(false);

        btNext.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Vehicle newVehicle = vehicleController.returnNewVehicle(etBrand, etModel, etChassi, etYear, etColor, etKm);
                Intent updateOs  = new Intent(getApplicationContext(), ServiceOrderActivity.class);

                updateOs.putExtra("newClient", client);
                updateOs.putExtra("newVehicle", newVehicle);
                updateOs.putExtra("documentId", documentId);
                updateOs.putExtra("updateOption", updateOption);

                startActivity(updateOs);
            }
        });
    }



    //NAO MEXER NESSE MÉTODO
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
                                String id = document.getId();

                                ClientOs newClient = new ClientOs(name,address, phone, cpf, id);
                                acClientList.add(newClient);
                                clientAutoCompleteAdapter.updateList(acClientList);

                            }
                        } else {
                            Log.d("DB Error", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}