package com.leonardomaito.autocommobile.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class ClientActivity extends AppCompatActivity {

    public Button nextOsStep;
    public EditText etClientName;
    public EditText etClientCpf;
    private EditText etClientAddress;
    private EditText etClientTelephone;

    private ClientController clientController = new ClientController();
    private int updateOption = 0;
    private String documentId;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os_client);

        nextOsStep = findViewById(R.id.btNextOs);
        nextOsStep.setBackgroundColor(getResources().getColor(R.color.negative_button));

        etClientName = findViewById(R.id.etClientInput);
        etClientCpf = findViewById(R.id.etCPFInput);
        etClientAddress = findViewById(R.id.etAddressInput);
        etClientTelephone = findViewById(R.id.etTelephoneInput);

        Bundle data = getIntent().getExtras();
        updateOption = data.getInt("updateOption");
        documentId = data.getString("documentId");

        if(updateOption == 0){
            setView();
        }
        else{
            setViewForUpdate(documentId, updateOption);
        }
    }

    private void setView(){
        nextOsStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(clientController.checkAllClientFields(etClientName, etClientCpf, nextOsStep)) {

                    Intent createVehicleActivity  = new Intent(getApplicationContext(), VehicleActivity.class);

                    createVehicleActivity.putExtra("novoCliente",clientController.returnNewClient(etClientName,etClientCpf,
                            etClientAddress,etClientTelephone, nextOsStep));

                    startActivity(createVehicleActivity);

                }
            }
        });
    }

    private void setViewForUpdate(String documentId, Integer updateOption){

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
                        etClientName.setText(serviceDocument.getClient().getName());
                        etClientCpf.setText(serviceDocument.getClient().getCpf());
                        etClientAddress.setText(serviceDocument.getClient().getAddress());
                        etClientTelephone.setText(serviceDocument.getClient().getTelephone());
                    }
                }
            }
        });
        nextOsStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(clientController.checkAllClientFields(etClientName, etClientCpf, nextOsStep)) {

                    Intent updateVehicle  = new Intent(getApplicationContext(), VehicleActivity.class);

                    updateVehicle.putExtra("novoCliente",clientController.returnNewClient(etClientName,etClientCpf,
                            etClientAddress,etClientTelephone, nextOsStep));
                    updateVehicle.putExtra("updateOption", updateOption);
                    updateVehicle.putExtra("documentId", documentId);
                    startActivity(updateVehicle);

                }
            }
        });
    }
}
