package com.leonardomaito.autocommobile.controllers;

import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.leonardomaito.autocommobile.models.Client;
import com.leonardomaito.autocommobile.models.ServiceOrder;
import com.leonardomaito.autocommobile.models.Vehicle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ServiceOrderController {

    private String osService;
    private String osObservation;
    private String osPaymentForm;
    private double osValue;
    Map<String, Object> data = new HashMap<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference docRef =
            db.collection("cliente")
                    .document("clienteTeste")
                    .collection("ServiceOrder");

    public void returnNewServiceOrder(EditText etService, EditText etObservation, EditText etPaymentForm
    , Client newClient, Vehicle newVehicle, EditText etDate, EditText etValue){

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        osService = etService.getText().toString();
        osObservation = etObservation.getText().toString();
        osPaymentForm = etPaymentForm.getText().toString();
        etDate.setText(formatter.format(currentTime));
        osValue=  etValue.getText().toString().isEmpty() ? 0: Double.parseDouble(etValue.getText().toString());

        ServiceOrder newServiceOrder = new ServiceOrder.ServiceOrderBuilder(newClient,
                newVehicle,osService,osPaymentForm, 1, osValue, etDate.getText().toString())
                .observation(osObservation)
                .build();

        sendDataToFirestore(newServiceOrder);
    }

    public void sendDataToFirestore(ServiceOrder serviceOrder){

        data.put("serviceOrder", serviceOrder);
        docRef.add(data);

    }
}
