package com.leonardomaito.autocommobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.leonardomaito.autocommobile.controllers.ServiceOrderController;
import com.leonardomaito.autocommobile.models.Client;
import com.leonardomaito.autocommobile.models.Vehicle;

import autocommobile.R;

public class NewOsThirdActivity extends AppCompatActivity {

    private Button returnMenuOs;
    private EditText etService;
    private EditText etObservation;
    private EditText etPaymentForm;
    private EditText etDate;
    private EditText etValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_os_third);

        ServiceOrderController serviceOrderController = new ServiceOrderController();

        Bundle data = getIntent().getExtras();
        Client newClient = data.getParcelable("novoCliente");
        Vehicle newVehicle =  data.getParcelable("novoCarro");

        returnMenuOs = findViewById(R.id.btReturnMenuOs);
        etService = findViewById(R.id.etServiceInput);
        etObservation = findViewById(R.id.etObservationInput);
        etPaymentForm = findViewById(R.id.etPaymentFormInput);
        etDate = findViewById(R.id.etDateInput);
        etDate.setText("0");
        etValue = findViewById(R.id.etValue);

        returnMenuOs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                serviceOrderController.returnNewServiceOrder(etService, etObservation, etPaymentForm, newClient,
                        newVehicle, etDate, etValue);

            }
        });
    }
}