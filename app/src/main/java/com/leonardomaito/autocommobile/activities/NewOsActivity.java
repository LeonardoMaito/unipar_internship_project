package com.leonardomaito.autocommobile.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.leonardomaito.autocommobile.controllers.ClientController;
import autocommobile.R;

public class NewOsActivity extends AppCompatActivity {

    public Button nextOsStep;
    public EditText etClientName;
    public EditText etClientCpf;
    private EditText etClientAddress;
    private EditText etClientTelephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_os);

        nextOsStep = findViewById(R.id.btNextOs);
        nextOsStep.setBackgroundColor(getResources().getColor(R.color.negative_button));

        etClientName = findViewById(R.id.etClientInput);
        etClientCpf = findViewById(R.id.etCPFInput);
        etClientAddress = findViewById(R.id.etAddressInput);
        etClientTelephone = findViewById(R.id.etTelephoneInput);

        ClientController clientController = new ClientController();

        nextOsStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(clientController.verifyClientCpf(etClientCpf, nextOsStep)
                && clientController.verifyClientName(etClientName, nextOsStep)) {

                    Intent newOsStepSecondIntent  = new Intent(getApplicationContext(), NewOsSecondActivity.class);

                    newOsStepSecondIntent.putExtra("novoCliente",clientController.returnNewClient(etClientName,etClientCpf,
                            etClientAddress,etClientTelephone, nextOsStep));

                    startActivity(newOsStepSecondIntent);

                }

            }
        });
    }
}
