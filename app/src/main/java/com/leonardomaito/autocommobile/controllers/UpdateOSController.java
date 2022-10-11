package com.leonardomaito.autocommobile.controllers;

import android.widget.EditText;

import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.FirebaseFirestore;
import com.leonardomaito.autocommobile.models.Client;
import com.leonardomaito.autocommobile.models.ServiceOrder;
import com.leonardomaito.autocommobile.models.Vehicle;


public class UpdateOSController {

    private String osService;
    private String osObservation;
    private String osPaymentForm;
    private double osValue;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void returnNewServiceOrder(EditText etService, EditText etObservation, EditText etPaymentForm
            , Client newClient, Vehicle newVehicle, EditText etDate, EditText etValue, String docId){

        osService = etService.getText().toString();
        osObservation = etObservation.getText().toString();
        osPaymentForm = etPaymentForm.getText().toString();
        osValue = Double.parseDouble(etValue.getText().toString());

        ServiceOrder newServiceOrder = new ServiceOrder.ServiceOrderBuilder(newClient,
                newVehicle,osService,osPaymentForm, 0, osValue, String.valueOf(etDate.getText()))
                .observation(osObservation)
                .build();

        sendDataToFirestore(newServiceOrder, docId);
    }

    public void sendDataToFirestore(ServiceOrder serviceOrder, String docId){

        DocumentReference idRef =
                db.collection("cliente")
                        .document("clienteTeste")
                        .collection("ServiceOrder")
                        .document(docId);

        idRef.update("serviceOrder", serviceOrder);

    }
}
