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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leonardomaito.autocommobile.controllers.ClientController;
import com.leonardomaito.autocommobile.models.Client;
import com.leonardomaito.autocommobile.models.ClientDocument;
import com.santalu.maskara.widget.MaskEditText;

import autocommobile.R;

public class ClientActivity extends AppCompatActivity {

    public Button btClient;
    public EditText etClientName;
    public MaskEditText etClientCpf;
    private EditText etClientAddress;
    private EditText etClientTelephone;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ClientController clientController = new ClientController();
    private String documentId;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private int updateOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os_client);

        btClient = findViewById(R.id.btClient);
        etClientName = findViewById(R.id.etClientInput);
        etClientCpf = findViewById(R.id.etCPFInput);
        etClientAddress = findViewById(R.id.etAddressInput);
        etClientTelephone = findViewById(R.id.etTelephoneInput);

        try {
            Bundle data = getIntent().getExtras();
            documentId = data.getString("documentId");
            updateOption = data.getInt("updateOption");

        }catch(Exception e){
            Log.e("TAG", "Sem bundle");
        }
        Log.e("Updateoption", "" + updateOption);

        if(updateOption == 1){
            setViewId(documentId);
        }

        else if(updateOption == 2){
            setViewForUpdate(documentId);
        }

        else{
            btClient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(clientController.checkAllClientFields(etClientName, etClientCpf)){

                        clientController.returnNewClient(etClientName,etClientCpf,
                                etClientAddress,etClientTelephone, 0, null);
                        Intent newClientIntent = new Intent(ClientActivity.this, ClientMenuActivity.class);
                        startActivity(newClientIntent);
                        finish();
                    }
                }
            });
        }
    }

    private void setViewId(String documentId) {

        DocumentReference docRef =
                db.collection("userData")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("Client")
                        .document(documentId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Result", "DocumentSnapshot data: " + document.getData());
                        Client client = document.toObject(ClientDocument.class).client;
                        etClientName.setText(client.getName());
                        etClientCpf.setText(client.getCpf());
                        etClientTelephone.setText(client.getTelephone());
                        etClientAddress.setText(client.getAddress());

                    }
                }
            }
        });

        etClientName.setEnabled(false);
        etClientCpf.setEnabled(false);
        etClientTelephone.setEnabled(false);
        etClientAddress.setEnabled(false);

        btClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClientActivity.this, ClientMenuActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void setViewForUpdate(String documentId) {

        DocumentReference docRef =
                db.collection("userData")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("Client")
                        .document(documentId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Result", "DocumentSnapshot data: " + document.getData());
                        Client client = document.toObject(ClientDocument.class).client;
                        etClientName.setText(client.getName());
                        etClientCpf.setText(client.getCpf());
                        etClientTelephone.setText(client.getTelephone());
                        etClientAddress.setText(client.getAddress());

                        btClient.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (clientController.checkAllClientFields(etClientName, etClientCpf)) {

                                    clientController.returnNewClient(etClientName, etClientCpf,
                                            etClientAddress, etClientTelephone, 1, documentId);
                                }
                                Intent intent = new Intent(ClientActivity.this, ClientMenuActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }
            }
        });
    }
}
