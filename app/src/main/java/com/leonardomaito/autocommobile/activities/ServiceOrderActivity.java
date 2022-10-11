package com.leonardomaito.autocommobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.leonardomaito.autocommobile.controllers.ServiceOrderController;
import com.leonardomaito.autocommobile.models.Client;
import com.leonardomaito.autocommobile.models.Vehicle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import autocommobile.R;

public class ServiceOrderActivity extends AppCompatActivity {

    private Button returnMenuOs;
    private EditText etService;
    private EditText etObservation;
    private EditText etPaymentForm;
    private EditText etDate;
    private EditText etValue;
    private Date currentTime = Calendar.getInstance().getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_os_third);

        Bundle data = getIntent().getExtras();
        Client newClient = data.getParcelable("novoCliente");
        Vehicle newVehicle =  data.getParcelable("novoCarro");

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        returnMenuOs = findViewById(R.id.btReturnMenuOs);
        returnMenuOs.setBackgroundColor(getResources().getColor(R.color.negative_button));

        etService = findViewById(R.id.etServiceInput);
        etObservation = findViewById(R.id.etObservationInput);
        etPaymentForm = findViewById(R.id.etPaymentFormInput);
        etDate = findViewById(R.id.etDateInput);
        etDate.setText(formatter.format(currentTime));
        etValue = findViewById(R.id.etValue);
        etValue.setText("0");

        ServiceOrderController serviceOrderController = new ServiceOrderController();

        returnMenuOs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (serviceOrderController.checkAllServiceFields(etValue)) {

                    serviceOrderController.returnNewServiceOrder(etService, etObservation, etPaymentForm, newClient,
                            newVehicle, etDate, etValue);

                    Intent intent = new Intent(getApplicationContext(), OsRecyclerActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}