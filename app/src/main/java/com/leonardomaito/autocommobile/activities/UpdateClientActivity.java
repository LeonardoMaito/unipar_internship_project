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
import com.leonardomaito.autocommobile.controllers.ClientController;
import com.leonardomaito.autocommobile.models.ServiceDocument;
import com.leonardomaito.autocommobile.models.ServiceOrder;

import autocommobile.R;

public class UpdateClientActivity extends AppCompatActivity{

    public Button nextUpdateOsStep;
    public EditText etUpdateClientName;
    public EditText etUpdateClientCpf;
    private EditText etUpdateClientAddress;
    private EditText etUpdateClientTelephone;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_os);

        Bundle data = getIntent().getExtras();
        String documentId = (String) data.get("documentId");

        nextUpdateOsStep = findViewById(R.id.btNextOs);
        nextUpdateOsStep.setBackgroundColor(getResources().getColor(R.color.negative_button));

        etUpdateClientName = findViewById(R.id.etClientInput);
        etUpdateClientCpf = findViewById(R.id.etCPFInput);
        etUpdateClientAddress = findViewById(R.id.etAddressInput);
        etUpdateClientTelephone = findViewById(R.id.etTelephoneInput);

        ClientController clientController = new ClientController();

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
                        etUpdateClientName.setText(serviceDocument.getClient().getName());
                        etUpdateClientCpf.setText(serviceDocument.getClient().getCpf());
                        etUpdateClientAddress.setText(serviceDocument.getClient().getAddress());
                        etUpdateClientTelephone.setText(serviceDocument.getClient().getTelephone());
                    }
                }
            }
        });

        nextUpdateOsStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(clientController.checkAllClientFields(etUpdateClientName, etUpdateClientCpf, nextUpdateOsStep)) {

                    Intent updateVehicle  = new Intent(getApplicationContext(), UpdateVehicleActivity.class);

                    updateVehicle.putExtra("novoCliente",clientController.returnNewClient(etUpdateClientName,etUpdateClientCpf,
                            etUpdateClientAddress,etUpdateClientTelephone, nextUpdateOsStep));
                    updateVehicle.putExtra("documentId", documentId);

                    startActivity(updateVehicle);

                }
            }
        });
    }
}