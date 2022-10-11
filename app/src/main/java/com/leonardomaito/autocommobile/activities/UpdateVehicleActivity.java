package com.leonardomaito.autocommobile.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class UpdateVehicleActivity extends AppCompatActivity {

    private Button nextUpdateOs;
    private EditText etUpdateBrand;
    private EditText etUpdateModel;
    private EditText etUpdateChassi;
    private EditText etUpdateYear;
    private EditText etUpdateColor;
    private EditText etUpdateKm;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_os_second);

        Bundle data = getIntent().getExtras();
        Client newClient = data.getParcelable("novoCliente");
        String documentId = data.getString("documentId");

        nextUpdateOs = findViewById(R.id.btNextOs2);
        etUpdateBrand = findViewById(R.id.etBrandInput);
        etUpdateModel = findViewById(R.id.etModelNumber);
        etUpdateChassi = findViewById(R.id.etChassiInput);
        etUpdateYear = findViewById(R.id.etYearInput);
        etUpdateColor = findViewById(R.id.etColorInput);
        etUpdateKm = findViewById(R.id.etKmInput);

        VehicleController vehicleController = new VehicleController();

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
                        etUpdateBrand.setText(serviceDocument.getVehicle().getBrand());
                        etUpdateModel.setText(serviceDocument.getVehicle().getModel());
                        etUpdateChassi.setText(String.valueOf(serviceDocument.getVehicle().getChassi()));
                        etUpdateYear.setText(String.valueOf(serviceDocument.getVehicle().getYear()));
                        etUpdateColor.setText(serviceDocument.getVehicle().getColor());
                        etUpdateKm.setText(String.valueOf(serviceDocument.getVehicle().getKm()));

                    }
                }
            }
        });

        nextUpdateOs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent updateOs  = new Intent(getApplicationContext(), UpdateServiceOrderActivity.class);

                updateOs.putExtra("novoCliente", newClient);
                updateOs.putExtra("novoCarro", vehicleController.returnNewVehicle(etUpdateBrand,etUpdateModel,
                        etUpdateChassi, etUpdateYear, etUpdateColor, etUpdateKm));
                updateOs.putExtra("documentId", documentId);

                startActivity(updateOs);
            }
        });
    }
}