package com.leonardomaito.autocommobile.controllers;

import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leonardomaito.autocommobile.models.Client;
import com.leonardomaito.autocommobile.models.ServiceDocument;
import com.leonardomaito.autocommobile.models.ServiceOrder;
import com.leonardomaito.autocommobile.models.Vehicle;


public class UpdateOSController {

    private String osService;
    private String osObservation;
    private String osPaymentForm;
    private int id;
    private double osValue;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void returnNewServiceOrder(EditText etService, EditText etObservation, EditText etPaymentForm
            , Client newClient, Vehicle newVehicle, EditText etDate, EditText etValue, String docId){

        DocumentReference idRef =
                db.collection("cliente")
                        .document("clienteTeste")
                        .collection("ServiceOrder")
                        .document(docId);

        idRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ServiceOrder serviceDocument = document.toObject(ServiceDocument.class).serviceOrder;
                        id = serviceDocument.getId();
                        osService = etService.getText().toString();
                        osObservation = etObservation.getText().toString();
                        osPaymentForm = etPaymentForm.getText().toString();
                        osValue = Double.parseDouble(etValue.getText().toString());

                        Log.d("Result", "ID" + id);

                        ServiceOrder newServiceOrder = new ServiceOrder.ServiceOrderBuilder(newClient,
                                newVehicle, osService, osPaymentForm, osValue, String.valueOf(etDate.getText()))
                                .observation(osObservation)
                                .id(id)
                                .build();

                        sendDataToFirestore(newServiceOrder, docId, id);
                    }
                }
            }
        });


    }


    public void sendDataToFirestore(ServiceOrder serviceOrder, String docId, int id){

        DocumentReference idRef =
                db.collection("cliente")
                        .document("clienteTeste")
                        .collection("ServiceOrder")
                        .document(docId);

        idRef.update("serviceOrder", serviceOrder);

    }
}
