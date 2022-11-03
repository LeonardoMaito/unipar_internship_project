package com.leonardomaito.autocommobile.controllers;


import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leonardomaito.autocommobile.models.Client;
import com.leonardomaito.autocommobile.models.ServiceOrder;
import com.leonardomaito.autocommobile.models.Vehicle;

import java.util.HashMap;
import java.util.Map;

public class ServiceOrderController {

    private String osService;
    private String osObservation;
    private String osPaymentForm;
    private double osValue;
    private double value;
    private long idValue;
    private Map<String, Object> data = new HashMap<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference docRef =
            db.collection("cliente")
                    .document("clienteTeste")
                    .collection("ServiceOrder");

    private DocumentReference idRef =
            db.collection("cliente")
                    .document("clienteTeste")
                    .collection("ServiceOrder")
                    .document("reservedId");

    public void returnNewServiceOrder(EditText etService, EditText etObservation, EditText etPaymentForm
    , Client newClient, Vehicle newVehicle, EditText etDate, EditText etValue){

        osService = etService.getText().toString();
        osObservation = etObservation.getText().toString();
        osPaymentForm = etPaymentForm.getText().toString();
        osValue = Double.parseDouble(etValue.getText().toString());

        ServiceOrder newServiceOrder = new ServiceOrder.ServiceOrderBuilder(newClient,
                newVehicle,osService,osPaymentForm, osValue, String.valueOf(etDate.getText()))
                .observation(osObservation)
                .id(0)
                .build();

        sendDataToFirestore(newServiceOrder);
    }

    public void sendDataToFirestore(ServiceOrder serviceOrder){

        idRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    idValue =  (long) document.get("id");

                }
            }
        });

        data.put("serviceOrder", serviceOrder);
        docRef.add(data).addOnSuccessListener(documentReference -> {
            String id = documentReference.getId();
            docRef.document(id).update("serviceOrder.id", FieldValue.increment(idValue));
            idRef.update("id", FieldValue.increment(1));

        });
    }

    public boolean checkAllServiceFields(EditText osValue){
        if(Double.parseDouble(osValue.getText().toString()) <= 0 ){
            osValue.setError("O valor precisa ser acima de 0");
            osValue.requestFocus();
            return false;
        }
        return true;
    }
}
