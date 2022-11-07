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
import com.leonardomaito.autocommobile.controllers.ServiceOrderController;
import com.leonardomaito.autocommobile.controllers.UpdateOSController;
import com.leonardomaito.autocommobile.models.Client;
import com.leonardomaito.autocommobile.models.ServiceDocument;
import com.leonardomaito.autocommobile.models.ServiceOrder;
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

    private int updateOption = 0;
    private String documentId;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ServiceOrderController serviceOrderController = new ServiceOrderController();;
    private UpdateOSController updateOSController = new UpdateOSController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_os_service);

        Bundle data = getIntent().getExtras();
        Client newClient = data.getParcelable("novoCliente");
        Vehicle newVehicle = data.getParcelable("novoCarro");
        updateOption = data.getInt("updateOption");
        documentId = data.getString("documentId");

        returnMenuOs = findViewById(R.id.btReturnMenuOs);
        returnMenuOs.setBackgroundColor(getResources().getColor(R.color.negative_button));

        etService = findViewById(R.id.etServiceInput);
        etObservation = findViewById(R.id.etObservationInput);
        etPaymentForm = findViewById(R.id.etPaymentFormInput);
        etDate = findViewById(R.id.etDateInput);
        etValue = findViewById(R.id.etValue);
        Log.e("OPTION", "updateOption = " + updateOption);


        if (updateOption == 0) {
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            etValue.setText("0");
            etDate.setText(formatter.format(currentTime));
            setView(newClient, newVehicle);
        } else {
            setViewForUpdate(documentId, newClient, newVehicle);
        }
    }

    private void setView(Client newClient, Vehicle newVehicle) {

        returnMenuOs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (serviceOrderController.checkAllServiceFields(etValue)) {

                    serviceOrderController.returnNewServiceOrder(etService, etObservation, etPaymentForm, newClient,
                            newVehicle, etDate, etValue);

                    Intent intent = new Intent(getApplicationContext(), OsRecyclerActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void setViewForUpdate(String documentId, Client newClient, Vehicle newVehicle) {
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
                        etService.setText(serviceDocument.getService());
                        etObservation.setText(serviceDocument.getObservation());
                        etPaymentForm.setText(serviceDocument.getPaymentForm());
                        etDate.setText(serviceDocument.getDate());
                        etValue.setText(String.valueOf(serviceDocument.getTotalValue()));
                    }
                }
            }
        });
        returnMenuOs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (serviceOrderController.checkAllServiceFields(etValue)) {

                    updateOSController.returnNewServiceOrder(etService, etObservation, etPaymentForm, newClient,
                            newVehicle, etDate, etValue, documentId);

                    Intent intent = new Intent(getApplicationContext(), OsRecyclerActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}