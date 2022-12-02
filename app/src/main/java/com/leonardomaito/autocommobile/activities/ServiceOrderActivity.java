package com.leonardomaito.autocommobile.activities;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leonardomaito.autocommobile.controllers.ServiceOrderController;
import com.leonardomaito.autocommobile.controllers.UpdateOSController;
import com.leonardomaito.autocommobile.models.Client;
import com.leonardomaito.autocommobile.models.ClientOs;
import com.leonardomaito.autocommobile.models.ServiceDocument;
import com.leonardomaito.autocommobile.models.ServiceOrder;
import com.leonardomaito.autocommobile.models.Vehicle;
import com.santalu.maskara.widget.MaskEditText;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import autocommobile.R;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ServiceOrderActivity extends AppCompatActivity {

    private Button returnMenuOs;
    private EditText etService;
    private EditText etObservation;
    private EditText etPaymentForm;
    private MaskEditText etDate;
    private EditText etValue;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private LocalDate currentTime = LocalDate.now();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private ServiceOrderController serviceOrderController = new ServiceOrderController();

    private int updateOption;
    private String documentId;

    private ClientOs client;
    private Vehicle vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os_service);

        returnMenuOs = findViewById(R.id.btReturnMenuOs);

        etService = findViewById(R.id.etServiceInput);
        etObservation = findViewById(R.id.etObservationInput);
        etPaymentForm = findViewById(R.id.etPaymentFormInput);
        etDate = findViewById(R.id.etDateInput);
        etValue = findViewById(R.id.etValue);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        etValue.setText("0");
        etDate.setText(formatter.format(currentTime));

        try {
            Bundle data = getIntent().getExtras();
            documentId = data.getString("documentId");
            updateOption = data.getInt("updateOption");
            vehicle = data.getParcelable("newVehicle");
            client = data.getParcelable("newClient");

        } catch (Exception e) {
            Log.e("Bundles", "Sem bundle");
        }

        Log.e("Bundles", "" + updateOption);
        if (updateOption == 1) {
            setViewId(documentId);

        } else if (updateOption == 2) {
           setViewForUpdate(documentId, client, vehicle);

        } else {

            returnMenuOs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(serviceOrderController.checkAllServiceFields(etValue, etDate))
                    serviceOrderController.returnNewServiceOrder(etService, etObservation, etPaymentForm, client, vehicle,
                            etDate, etValue, 0, documentId);

                    Intent intent = new Intent(getApplicationContext(), OsRecyclerActivity.class);
                    startActivity(intent);
                    finish();

                }
            });

        }
    }

        private void setViewId (String documentId){

            DocumentReference docRef =
                    db.collection("userData")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .collection("ServiceOrder")
                            .document(documentId);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

            etService.setEnabled(false);
            etObservation.setEnabled(false);
            etPaymentForm.setEnabled(false);
            etDate.setEnabled(false);
            etValue.setEnabled(false);

            returnMenuOs.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {
                    VehicleActivity.self_intent.finish();
                    Intent intent = new Intent(ServiceOrderActivity.this, OsRecyclerActivity.class);
                    startActivity(intent);
                    finish();
                }
            });


        }

        private void setViewForUpdate(String documentId, ClientOs newClient, Vehicle newVehicle) {

          DocumentReference docRef =
                    db.collection("userData")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .collection("ServiceOrder")
                            .document(documentId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                    if (serviceOrderController.checkAllServiceFields(etValue, etDate)) {

                        serviceOrderController.returnNewServiceOrder(etService, etObservation, etPaymentForm, newClient, newVehicle,
                                etDate, etValue, 1, documentId);

                        VehicleActivity.self_intent.finish();
                        Intent intent = new Intent(getApplicationContext(), OsRecyclerActivity.class);
                        startActivity(intent);
                        finish();
                }
            }
        });
        }
}