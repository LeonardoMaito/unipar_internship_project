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

public class UpdateServiceOrderActivity extends AppCompatActivity {

    private Button returnMenuOs;
    private Button updateOs;
    private EditText etUpdateService;
    private EditText etUpdateServiceObservation;
    private EditText etUpdateServicePaymentForm;
    private EditText etUpdateServiceDate;
    private EditText etUpdateServiceValue;
    private Date currentTime = Calendar.getInstance().getTime();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_os);

        Bundle data = getIntent().getExtras();
        Client newClient = data.getParcelable("novoCliente");
        Vehicle newVehicle =  data.getParcelable("novoCarro");
        String documentId = data.getString("documentId");

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        returnMenuOs = findViewById(R.id.btReturnMenuOs);
        updateOs = findViewById(R.id.btUpdateOs);
        etUpdateService = findViewById(R.id.etServiceInput);
        etUpdateServiceObservation = findViewById(R.id.etObservationInput);
        etUpdateServicePaymentForm = findViewById(R.id.etPaymentFormInput);
        etUpdateServiceDate = findViewById(R.id.etDateInput);
        etUpdateServiceDate.setText(formatter.format(currentTime));
        etUpdateServiceValue = findViewById(R.id.etValue);

        UpdateOSController serviceOrderController = new UpdateOSController();
        ServiceOrderController orderController = new ServiceOrderController();

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
                        etUpdateService.setText(serviceDocument.getService());
                        etUpdateServiceObservation.setText(serviceDocument.getObservation());
                        etUpdateServicePaymentForm.setText(serviceDocument.getPaymentForm());
                        etUpdateServiceDate.setText(serviceDocument.getDate());
                        etUpdateServiceValue.setText(String.valueOf(serviceDocument.getTotalValue()));
                    }
                }
            }
        });

        updateOs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (orderController.checkAllServiceFields(etUpdateServiceValue)) {

                    serviceOrderController.returnNewServiceOrder(etUpdateService, etUpdateServiceObservation, etUpdateServicePaymentForm, newClient,
                            newVehicle, etUpdateServiceDate, etUpdateServiceValue, documentId);

                    Intent intent = new Intent(getApplicationContext(), OsRecyclerActivity.class);
                    startActivity(intent);
                }
            }
        });

        returnMenuOs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (orderController.checkAllServiceFields(etUpdateServiceValue)) {

                    Intent intent = new Intent(getApplicationContext(), OsRecyclerActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}