package com.leonardomaito.autocommobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leonardomaito.autocommobile.controllers.VehicleController;
import com.leonardomaito.autocommobile.models.Client;
import com.leonardomaito.autocommobile.models.ServiceDocument;
import com.leonardomaito.autocommobile.models.ServiceOrder;

import autocommobile.R;

public class VehicleActivity extends AppCompatActivity {

    private Button startOsSecond;
    private EditText etBrand;
    private EditText etModel;
    private EditText etChassi;
    private EditText etYear;
    private EditText etColor;
    private EditText etKm;

    private VehicleController vehicleController = new VehicleController();

    private int updateOption = 0;
    private String documentId;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_os_second);

        Bundle data = getIntent().getExtras();
        Client newClient = data.getParcelable("novoCliente");
        updateOption = data.getInt("updateOption");
        documentId = data.getString("documentId");

        startOsSecond = findViewById(R.id.btNextOs2);
        etBrand = findViewById(R.id.etBrandInput);
        etModel = findViewById(R.id.etModelNumber);
        etChassi = findViewById(R.id.etChassiInput);
        etYear = findViewById(R.id.etYearInput);
        etColor = findViewById(R.id.etColorInput);
        etKm = findViewById(R.id.etKmInput);

        if(updateOption == 0){
            setView(newClient);
        }
        else{
            setViewForUpdate(documentId, updateOption, newClient);
        }
    }

    private void setView(Client newClient) {
        startOsSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent createServiceOrderActivity  = new Intent(getApplicationContext(), ServiceOrderActivity.class);

                createServiceOrderActivity.putExtra("novoCliente", newClient);
                createServiceOrderActivity.putExtra("novoCarro", vehicleController.returnNewVehicle(etBrand,etModel,
                        etChassi, etYear, etColor, etKm));

                startActivity(createServiceOrderActivity);
            }
        });
    }

    private void setViewForUpdate(String documentId, int updateOption, Client newClient) {

        DocumentReference idRef =
                db.collection("cliente")
                        .document("clienteTeste")
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
    }
}