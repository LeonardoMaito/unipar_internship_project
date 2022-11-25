package com.leonardomaito.autocommobile.activities;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leonardomaito.autocommobile.controllers.ClientController;
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
        }catch(Exception e){
            Log.e("TAG", "Sem bundle");
        }

        btClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(clientController.checkAllClientFields(etClientName, etClientCpf)){

                 clientController.returnNewClient(etClientName,etClientCpf,
                            etClientAddress,etClientTelephone);
                    Intent newClientIntent = new Intent(ClientActivity.this, ClientMenuActivity.class);
                    startActivity(newClientIntent);
                     finish();
                }
            }
        });
    }
}
